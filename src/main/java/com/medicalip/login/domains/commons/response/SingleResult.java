package com.medicalip.login.domains.commons.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SingleResult {
	
	private int resultCode;
    private String resultMessage;
    private String token;
    
    public SingleResult(int code, String message, String token) {
    	this.resultCode = code;
    	this.resultMessage = message;
    	this.token = token;
    }

}
