package com.studyolleh.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.studyolleh.account.SignUpForm;

@Controller
public class AccountController {

	@GetMapping("/sign-up")
	public String signUpFrom(Model model) {
//		model.addAttribute("signUpForm", new SignUpForm());
		model.addAttribute(new SignUpForm());
		return "account/sign-up";
	}
}





















