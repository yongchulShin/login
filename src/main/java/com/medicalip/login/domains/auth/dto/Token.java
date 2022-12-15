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
	
	@Column(name = "EXPIRE_DT")
	private Date expireDt;
	
	@ManyToOne
	@JoinColumn(name = "USER_SEQ")
	private Users users;
	
	@Builder
	public Token(String accessToken, String refreshToken, Users users, Date expireDt) {
		this.accessToken = accessToken;
		this.refreshToken = refreshToken;
		this.users = users;
		this.expireDt = expireDt;
	}
	public void refreshUpdate(String refreshToken) {
		this.refreshToken = refreshToken;
	}
	public void accessUpdate(String accessToken) {
		this.accessToken = accessToken;
	}
}
