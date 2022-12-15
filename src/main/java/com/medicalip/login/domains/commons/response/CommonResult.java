package com.medicalip.login.domains.commons.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommonResult {
	
    private int resultCode;
    private String resultMessage;
    
    public CommonResult(int code, String message) {
    	this.resultCode = code;
    	this.resultMessage = message;
    }
}
