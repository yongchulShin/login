package com.medicalip.login.domains.commons.advice.exception;

public class DuplicateValueInputException extends RuntimeException {
	/**
	 * 
	 */
	private static final long serialVersionUID = -7378458071919975077L;

	public DuplicateValueInputException(String msg, Throwable t) {
        super(msg, t);
    }
	public DuplicateValueInputException(String msg) {
		super(msg);
	}
	
	public DuplicateValueInputException() {
		super();
	}
}
