package com.studyolleh.account;

import java.time.LocalDateTime;

import javax.validation.Valid;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PostMapping;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
public class AccountController {

	private final SignUpFormValidator signUpFormValidator;
	private final AccountService accountService;
	private final AccountRepository accountRepository;
	
	@InitBinder("signUpForm")
	public void initBinder(WebDataBinder webDataBinder) {
		webDataBinder.addValidators(signUpFormValidator);
	}
	
	@GetMapping("/sign-up")
	public String signUpForm(Model model) {
//		model.addAttribute("signUpForm", new SignUpForm());
		model.addAttribute(new SignUpForm());
		return "account/sign-up";
	}
	
	@PostMapping("/sign-up")
	public String signUpSubmit(@Valid SignUpForm signUpForm, Errors errors) {
		if(errors.hasErrors()) {
			return "account/sign-up";
		}
		
		Account account =  accountService.processNewAccount(signUpForm);
		accountService.login(account);
		return "redirect:/";
	}

	
	@GetMapping("/check-email-token")
	public String checkEmailToken(String token, String email, Model model) {
		
		String view = "account/checked-email";
		
		Account account = accountRepository.findByEmail(email);
		if(account == null) {
			model.addAttribute("error", "wrong.email");
			return view;
		}
		
		if(!account.isValidToken(token)) {
			model.addAttribute("error", "wrong.token");
			return view;
		}
		
		
		account.completeSignUp();
		accountService.login(account);
		model.addAttribute("numberOfUser", accountRepository.count());
		model.addAttribute("nickname", account.getNickname());
		
		return view;
	}
	
	
    @GetMapping("/check-email")
    public String checkEmail(@CurrentUser Account account, Model model) {
        model.addAttribute("email", account.getEmail());
        return "account/check-email";
    }

    @GetMapping("/resend-confirm-email")
    public String resendConfirmEmail(@CurrentUser Account account, Model model) {
        if (!account.canSendConfirmEmail()) {
            model.addAttribute("error", "?????? ???????????? 1????????? ????????? ????????? ??? ????????????.");
            model.addAttribute("email", account.getEmail());
            return "account/check-email";
        }

        accountService.sendSignUpConfirmEmail(account);
        return "redirect:/"; // refresh??? ??? ?????? ????????? ?????? ???????????? ?????? ?????? ??????
    }

}





















