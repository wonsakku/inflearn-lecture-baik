package com.studyolleh.account;

import java.util.List;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import lombok.Getter;

@Getter
public class UserAccount extends User{

	private Account account;
	
	 public UserAccount(Account account) {
		 super(account.getNickname(), account.getPassword(), List.of(new SimpleGrantedAuthority("ROLE_USER")));
		 this.account = account;
	}
	 
	 
}