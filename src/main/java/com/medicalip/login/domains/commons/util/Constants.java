package com.medicalip.login.domains.commons.util;

public final class Constants {
	
	public static final int	UNKNOWN_CODE	= 9999;
    public static final String	UNKNOWN_MSG	= "알 수 없는 오류가 발생하였습니다.";
    public static final int	USERNOTFOUND_CODE	= 1000;
    public static final String	USERNOTFOUND_MSG	= "존재하지 않는 회원입니다.";
    public static final int	EMAILSIGNINFAILED_CODE	= 1001;
    public static final String	EMAILSIGNINFAILED_MSG	= "계정이 존재하지 않거나 이메일 또는 비밀번호가 정확하지 않습니다.";
    public static final int	USERFOUND_CODE	= 1002;
    public static final String	USERFOUND_MSG	= "이미 가입된 회원입니다.";
    
    public static final String SECRET_KEY = "secretKey";
    public static final String REFRESH_KEY = "refreshKey";
    public static final String DATA_KEY = "email";
    public static final String AUTHORITIES_KEY = "auth";
	
//    public static final long ACCESS_TOKEN_VALID_TIME = 1000 * 60 * 60; //1시간
    public static final long REFRESH_TOKEN_VALID_TIME = 1000L * 60 * 60; //1시간
    public static final long ACCESS_TOKEN_VALID_TIME = 1000L * 60 * 60 * 24 * 30; // 1달
    
}
