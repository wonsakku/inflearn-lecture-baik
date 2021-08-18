package com.studyolleh.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.studyolleh.account.Account;
import com.studyolleh.account.CurrentUser;

@Controller
public class MainController {
	
	@GetMapping("/")
	public String home(@CurrentUser Account account, Model model) {
		
		if(account != null) {
			model.addAttribute(account);
		}
		return "index";
	}

}
