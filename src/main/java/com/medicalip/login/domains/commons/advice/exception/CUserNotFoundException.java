package com.medicalip.login.domains.commons.advice.exception;

public class CUserNotFoundException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8419786546572767426L;

	public CUserNotFoundException(String msg, Throwable t) {
        super(msg, t);
    }
	public CUserNotFoundException(String msg) {
		super(msg);
	}
	
	public CUserNotFoundException() {
		super();
	}
}
