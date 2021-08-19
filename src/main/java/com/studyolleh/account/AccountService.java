package com.studyolleh.account;

import java.util.List;

import javax.transaction.Transactional;
import javax.validation.Valid;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AccountService implements UserDetailsService{

	private final AccountRepository accountRepository;
	private final JavaMailSender javaMailSender;
	private final PasswordEncoder passwordEncoder;

	@Transactional
	public Account processNewAccount(@Valid SignUpForm signUpForm) {
		Account newAccount = saveNewAccount(signUpForm);
		newAccount.generateEmailCheckToken();
		sendSignUpConfirmEmail(newAccount);
		return newAccount;
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

	public void sendSignUpConfirmEmail(Account newAccount) {
		SimpleMailMessage mailMessage = new SimpleMailMessage();
		mailMessage.setTo(newAccount.getEmail());
		mailMessage.setSubject("스터디 롤래, 회원 가입 인증");
		mailMessage.setText("/check-email-token?token=" + newAccount.getEmailCheckToken()
								+ "&email=" + newAccount.getEmail());
		
		javaMailSender.send(mailMessage);
	}
	
	
	
	public void login(Account account) {
		UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
					new UserAccount(account), 
					account.getPassword(), 
					List.of(new SimpleGrantedAuthority("ROLE_USER"))
				);
		
		SecurityContext context = SecurityContextHolder.getContext();
		context.setAuthentication(token);
	}
	
	
	@Override
	public UserDetails loadUserByUsername(String emailOrNickname) throws UsernameNotFoundException {
        
		Account account = accountRepository.findByEmail(emailOrNickname);
    	if (account == null) {
    		account = accountRepository.findByNickname(emailOrNickname);
    	}
    	
    	if (account == null) {
    		throw new UsernameNotFoundException(emailOrNickname);
    	}

        return new UserAccount(account);
    }
}
















