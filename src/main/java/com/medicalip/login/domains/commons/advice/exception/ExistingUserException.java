package com.medicalip.login.domains.commons.advice.exception;

public class ExistingUserException extends RuntimeException{
		/**
	 * 
	 */
	private static final long serialVersionUID = 8424957630914835580L;
	
	public ExistingUserException(String msg, Throwable t) {
        super(msg, t);
    }
	public ExistingUserException(String msg) {
		super(msg);
	}
	
	public ExistingUserException() {
		super();
	}
}
