package com.medicalip.login.domains.commons.advice.exception;

public class FileStorageException extends RuntimeException{


    private static final long serialVersionUID = 7555825523478L;
	
	public FileStorageException(String msg, Throwable t) {
        super(msg, t);
    }
	public FileStorageException(String msg) {
		super(msg);
	}
	
	public FileStorageException() {
		super();
	}

    
}
