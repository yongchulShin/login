package com.medicalip.login.domains.commons.advice.exception;

public class UnauthorizedException extends RuntimeException{
	/**
	 * 
	 */
	private static final long serialVersionUID = 7586372174016293438L;

	public UnauthorizedException(String msg, Throwable t) {
        super(msg, t);
    }
	public UnauthorizedException(String msg) {
		super(msg);
	}
	
	public UnauthorizedException() {
		super();
	}
}
