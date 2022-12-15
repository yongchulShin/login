package com.medicalip.login.domains.users.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
public class UserRequest {
	private String nationCode;
    private String userEmail;
    private String userPw;
    private String userName;
    private String userNum;
    private String institude;
    private String isSubscribe;
}
