package com.studyolleh.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.then;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.studyolleh.account.Account;
import com.studyolleh.account.AccountRepository;



@SpringBootTest
@AutoConfigureMockMvc
class AccountControllerTest {

	@Autowired private MockMvc mockMvc;
	@Autowired private AccountRepository accountRepository;
	
	@MockBean // 외부 연동은 mocking으로 테스트
	JavaMailSender javaMailSender;
	
	
	@DisplayName("회원 가입 화면 보이는지 테스트")
	@Test
	void signUpForm() throws Exception{
		mockMvc.perform(get("/sign-up"))
			.andExpect(status().isOk())
			.andExpect(view().name("account/sign-up"))
			.andDo(print())
			.andExpect(model().attributeExists("signUpForm"))
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
                .andExpect(view().name("redirect:/"));

        Account account =accountRepository.findByEmail("sakku@email.com");
//        assertThat(account.getPassword()).isNotEqualTo("12345678");
//        assertTrue(accountRepository.existsByEmail("sakku@email.com"));
        assertNotNull(account);
        assertNotEquals(account.getPassword(), "12345678");
        assertNotNull(account.getEmailCheckToken());
        then(javaMailSender).should().send(any(SimpleMailMessage.class));
    }
}





















