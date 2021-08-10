package com.studyolleh.account;

import javax.validation.Valid;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AccountService {

	private final AccountRepository accountRepository;
	private final JavaMailSender javaMailSender;
	private final PasswordEncoder passwordEncoder;

	public void processNewAccount(@Valid SignUpForm signUpForm) {
		Account newAccount = saveNewAccount(signUpForm);
		newAccount.generateEmailCheckToken();
		sendSignUpConfirmEmail(newAccount);
	}

	
	private Account saveNewAccount(SignUpForm signUpForm) {
		Account account = Account.builder()
							.email(signUpForm.getEmail())
							.nickname(signUpForm.getNickname())
							.password(passwordEncoder.encode(signUpForm.getPassword()))  
							.studyCreatedByWeb(true)
							.studyEnrollmentResultByWeb(true)
							.studyUpdatedByWeb(true)
							.build();
		
		
		Account newAccount = accountRepository.save(account);
		return newAccount;
	}

	private void sendSignUpConfirmEmail(Account newAccount) {
		SimpleMailMessage mailMessage = new SimpleMailMessage();
		mailMessage.setTo(newAccount.getEmail());
		mailMessage.setSubject("스터디 롤래, 회원 가입 인증");
		mailMessage.setText("/check-email-token?token=" + newAccount.getEmailCheckToken()
								+ "&email=" + newAccount.getEmail());
		
		javaMailSender.send(mailMessage);
	}
}
