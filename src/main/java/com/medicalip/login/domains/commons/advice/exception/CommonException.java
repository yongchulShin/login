package com.medicalip.login.domains.commons.advice.exception;

public class CommonException extends RuntimeException{

	/**
	 * 
	 */
	private static final long serialVersionUID = -7809693032938216887L;

	public CommonException(String msg, Throwable t) {
        super(msg, t);
    }
	public CommonException(String msg) {
		super(msg);
	}
	
	public CommonException() {
		super();
	}
}
