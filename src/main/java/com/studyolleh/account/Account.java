package com.studyolleh.account;

import java.time.LocalDateTime;

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
	private String emailVerified;
	private String emailCheckToken;
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
}






























