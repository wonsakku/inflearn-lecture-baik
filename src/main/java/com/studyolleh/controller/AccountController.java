package com.studyolleh.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AccountController {

	@GetMapping("/sign-up")
	public String signUpFrom(Model model) {
		return "account/sign-up";
	}
}
