package com.studyolleh.controller;

import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.then;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.unauthenticated;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import javax.transaction.Transactional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.web.servlet.MockMvc;

import com.studyolleh.account.Account;
import com.studyolleh.account.AccountRepository;


@Transactional
@SpringBootTest
@AutoConfigureMockMvc
class AccountControllerTest {

	@Autowired private MockMvc mockMvc;
	@Autowired private AccountRepository accountRepository;
	
	@MockBean // 외부 연동은 mocking으로 테스트
	JavaMailSender javaMailSender;
	
	
	@DisplayName("인증 메일 확인 - 입력값 오류")
	@Test
	void checkEmailToken_with_wrong_input() throws Exception{
		mockMvc.perform(get("/check-email-token")
				.param("nickname", "sakku")
					.param("token", "sadfsldkj")
					.param("email", "sakku@email.com")
				)
		.andExpect(status().isOk())
		.andExpect(model().attributeExists("error"))
		.andExpect(view().name("account/checked-email"))
		.andExpect(unauthenticated())
		;
	}
	
	
	@DisplayName("인증 메일 확인 - 입력값 정상")
	@Test
	void checkEmailToken() throws Exception{
		
		Account account = Account.builder()
				.email("test@email.com")
				.password("12345678")
				.nickname("sakku")
				.build();
		
		Account newAccount = accountRepository.save(account);
		newAccount.generateEmailCheckToken(); // test 클래스명 위에 Transactional 추가
		
		
		mockMvc.perform(get("/check-email-token")
				.param("token", newAccount.getEmailCheckToken())
				.param("email", newAccount.getEmail())
//				.with(csrf())
				)
		.andExpect(status().isOk())
		.andExpect(model().attributeDoesNotExist("error"))
		.andExpect(model().attributeExists("nickname"))
		.andExpect(model().attributeExists("numberOfUser"))
		.andExpect(view().name("account/checked-email"))
		.andExpect(authenticated().withUsername("sakku"))
		;
	}
	
	
	@DisplayName("회원 가입 화면 보이는지 테스트")
	@Test
	void signUpForm() throws Exception{
		mockMvc.perform(get("/sign-up"))
			.andExpect(status().isOk())
			.andExpect(view().name("account/sign-up"))
			.andDo(print())
			.andExpect(model().attributeExists("signUpForm"))
			.andExpect(unauthenticated())
			.andExpect(unauthenticated())
			;
	}

	
	@DisplayName("회원가입 처리 - 입력값 오류")
	@Test
	void signUpSubmit_with_wrong_input() throws Exception{
		mockMvc.perform(post("/sign-up")
					.param("nickname", "sakku")
					.param("email", "email...")
					.param("password", "12345")
					.with(csrf())
					)
			.andExpect(status().isOk())
			.andExpect(view().name("account/sign-up"))
			.andExpect(unauthenticated())
		;
		
	}
	
    @DisplayName("회원 가입 처리 - 입력값 정상")
    @Test
    void signUpSubmit_with_correct_input() throws Exception {
        mockMvc.perform(post("/sign-up")
                .param("nickname", "sakku")
                .param("email", "sakku@email.com")
                .param("password", "12345678")
                .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(authenticated())
                .andExpect(view().name("redirect:/"))
                .andExpect(authenticated().withUsername("sakku"))
                ;

        Account account =accountRepository.findByEmail("sakku@email.com");
//        assertThat(account.getPassword()).isNotEqualTo("12345678");
//        assertTrue(accountRepository.existsByEmail("sakku@email.com"));
        assertNotNull(account);
        assertNotEquals(account.getPassword(), "12345678");
        assertNotNull(account.getEmailCheckToken());
        then(javaMailSender).should().send(any(SimpleMailMessage.class));
    }
}





















