package com.medicalip.login.domains.auth.dto;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.medicalip.login.domains.users.dto.Users;

import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
@Table(name = "TB_INFO_TOKEN")
@Entity
public class Token {
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Id
	@Column(name = "TOKEN_ID")
	private Long tokenId;
	
	@Column(name = "ACCESS_TOKEN")
	private String accessToken;
	
	@Column(name = "REFRESH_TOKEN")
	private String refreshToken;
	
//	@Column(name = "ACCESS_TOKEN_EXPIRE_DT")
//	private Date accessTokenExpireDt;
	
	@Column(name = "REFRESH_TOKEN_EXPIRE_DT")
	private Date refreshTokenExpireDt;
	
	@ManyToOne
	@JoinColumn(name = "USER_SEQ")
	@NotNull
	private Users users;
	
	@Builder
	public Token(
			Users users, String refreshToken, Date refreshTokenExpireDt, String accessToken) {
		this.accessToken = accessToken;
		this.refreshToken = refreshToken;
		this.users = users;
//		this.accessTokenExpireDt = accessTokenExpireDt;
		this.refreshTokenExpireDt = refreshTokenExpireDt;
	}
	public void refreshUpdate(String refreshToken) {
		this.refreshToken = refreshToken;
	}
	public void accessUpdate(String accessToken) {
		this.accessToken = accessToken;
	}
//	public void accessExpireUpdate(Date accessTokenExpireDt) {
//		this.accessTokenExpireDt = accessTokenExpireDt;
//	}
	public void refreshExpireUpdate(Date refreshTokenExpireDt) {
		this.refreshTokenExpireDt = refreshTokenExpireDt;
	}
}
