package com.medicalip.login.domains.commons.advice.exception;

public class InvalidInputValueException  extends RuntimeException{
	/**
	 * 
	 */
	private static final long serialVersionUID = -3308744162751822118L;

	public InvalidInputValueException(String msg, Throwable t) {
        super(msg, t);
    }
	public InvalidInputValueException(String msg) {
		super(msg);
	}
	
	public InvalidInputValueException() {
		super();
	}
}
