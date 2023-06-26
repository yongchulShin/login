package com.medicalip.login.domains.commons.advice.exception;

public class UserExistsException extends RuntimeException{
	/**
	 * 
	 */
	private static final long serialVersionUID = 8695722482525535612L;

	public UserExistsException(String msg, Throwable t) {
        super(msg, t);
    }
	public UserExistsException(String msg) {
		super(msg);
	}
	
	public UserExistsException() {
		super();
	}
}
