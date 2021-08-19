package com.studyolleh.main;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.unauthenticated;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import com.studyolleh.account.AccountRepository;
import com.studyolleh.account.AccountService;
import com.studyolleh.account.SignUpForm;

@SpringBootTest
@AutoConfigureMockMvc
class MainControllerTest {

	@Autowired MockMvc mockMvc;
	@Autowired AccountService accountService;
	@Autowired AccountRepository accountRepository;
	
	
	@BeforeEach
	void BeforeEach() {
		SignUpForm signUpForm = new SignUpForm();
		signUpForm.setNickname("sakku");
		signUpForm.setEmail("sakku@email.com");
		signUpForm.setPassword("123456789");
		accountService.processNewAccount(signUpForm);
	}
	
	@AfterEach
	void afterEach() {
		accountRepository.deleteAll();
	}
	
	
	@DisplayName("이메일로 로그인 성공")
	@Test
	void login_with_email() throws Exception{
		
		
		mockMvc.perform(post("/login")
					.param("username", "sakku@email.com")
					.param("password", "123456789")
					.with(csrf())
				)
			.andExpect(status().is3xxRedirection())
			.andExpect(redirectedUrl("/"))
			.andExpect(authenticated().withUsername("sakku"))
			;
	}

	@DisplayName("nickname으로 로그인 성공")
	@Test
	void login_with_nickname() throws Exception{
		
		mockMvc.perform(post("/login")
				.param("username", "sakku")
				.param("password", "123456789")
				.with(csrf())
				)
		.andExpect(status().is3xxRedirection())
		.andExpect(redirectedUrl("/"))
		.andExpect(authenticated().withUsername("sakku"))
		;
	}
	
	@DisplayName("로그인 실패")
	@Test
	void login_fail() throws Exception{
		
		mockMvc.perform(post("/login")
				.param("username", "sakku")
				.param("password", "1234567891")
				.with(csrf())
				)
		.andExpect(status().is3xxRedirection())
		.andExpect(redirectedUrl("/login?error"))
		.andExpect(unauthenticated())
		;
	}
	
//	@WithMockUser // user가 존재하게...
	@DisplayName("로그아웃")
	@Test
	void logout() throws Exception{
		
		mockMvc.perform(post("/logout")
				.with(csrf())
				)
		.andExpect(status().is3xxRedirection())
		.andExpect(redirectedUrl("/"))
		.andExpect(unauthenticated())
		;
	}
	
}




















