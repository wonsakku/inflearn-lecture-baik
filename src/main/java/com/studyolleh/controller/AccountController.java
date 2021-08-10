package com.studyolleh.controller;

import javax.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PostMapping;

import com.studyolleh.account.SignUpForm;
import com.studyolleh.account.SignUpFormValidator;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
public class AccountController {

	private final SignUpFormValidator signUpFormValidator;
	
	
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
		// TODO 회원 가입 처리
		return "redirect:/";
	}
	
}





















