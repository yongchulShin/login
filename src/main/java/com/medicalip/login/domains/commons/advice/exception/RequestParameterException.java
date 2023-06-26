package com.medicalip.login.domains.commons.advice.exception;

public class RequestParameterException extends RuntimeException{

	/**
	 * 
	 */
	private static final long serialVersionUID = 342186105619106331L;

	public RequestParameterException(String msg, Throwable t) {
        super(msg, t);
    }
	public RequestParameterException(String msg) {
		super(msg);
	}
	
	public RequestParameterException() {
		super();
	}
}
