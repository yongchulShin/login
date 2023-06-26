package com.medicalip.login.domains.users.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
public class UserRequest {
    private String userSeq;
	private String nationCode;
    private String userEmail;
    private String userPw;
    private String userName;
    private String userNum;
    private String institude;
    private String isSubscribe;
    private String isDrop;
    private String enabled;
    private String isEducation;
    private LocalDateTime dropDttm;

    private String serviceId;
    private int		updateServiceLevel;					// update service level(0 : unlimited, 1 : blocked, 2 : period limited)
    private int		updateServicePeriod;				// update service period(default : 12 => 기본 12개월 할당, updateServiceLevel이 2인 경우 적용되는 옵션.)
    private String	updateServiceLimitVersion;			// update service limit version(default : 3.0 => 3.0 미만의 업데이트만 받을 수 있음, updateServiceLevel이 0,2인 경우 해당 옵션 적용됨.)

    private String currentPw;
    private String newPw;

    private String ip;
    private String ip2;
    private String browser;
    private String location;
    private String ts;
}
