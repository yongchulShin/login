package com.medicalip.login.domains.commons.advice.exception;

public class UnauthorizedEmailException extends RuntimeException{

    private static final long serialVersionUID = 1L;

    public UnauthorizedEmailException(String msg, Throwable t){
        super(msg, t);
    }

    public UnauthorizedEmailException(String msg){
        super(msg);
    }

    public UnauthorizedEmailException(){
        super();
    }
    
}
