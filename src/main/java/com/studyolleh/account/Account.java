package com.studyolleh.account;

import java.time.LocalDateTime;
import java.util.UUID;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;

import lombok.Setter;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Setter
@EqualsAndHashCode(of = "id")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Account {

	@Id
	@GeneratedValue
	private long id;
	@Column(unique = true)
	private String email;
	@Column(unique = true)
	private String nickname;
	private String password;
	private boolean emailVerified;
	private String emailCheckToken;
	private LocalDateTime emailCheckTokenGeneratedAt;
	private LocalDateTime joinedAt;
	private String bio;
	private String url;
	private String occupation;
	private String location; // varchar(255)
	
	@Lob @Basic(fetch = FetchType.EAGER) //@Lob => text
	private String profileImage;
	private boolean studyCreatedByEmail;
	private boolean studyCreatedByWeb;
	private boolean studyEnrollmentResultByEmail;
	private boolean studyEnrollmentResultByWeb;
	private boolean studyUpdatedByEmail;
	private boolean studyUpdatedByWeb;
	
	
	public void generateEmailCheckToken() {
		this.emailCheckToken = UUID.randomUUID().toString();
		this.emailCheckTokenGeneratedAt = LocalDateTime.now();
	}
	
	
	public void completeSignUp() {
		this.emailVerified = true;
		this.joinedAt = LocalDateTime.now();
	}


	public boolean isValidToken(String token) {
		return this.emailCheckToken.equals(token);
	}
	
    public boolean canSendConfirmEmail() {
        return this.emailCheckTokenGeneratedAt.isBefore(LocalDateTime.now().minusHours(1));
    }
}






























