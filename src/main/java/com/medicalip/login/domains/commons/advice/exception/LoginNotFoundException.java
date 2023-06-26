package com.medicalip.login.domains.commons.advice.exception;

public class LoginNotFoundException extends RuntimeException{
    
    private static final long serialVersionUID = -1L;

    public LoginNotFoundException(String msg, Throwable t){
        super(msg, t);
    }
    public LoginNotFoundException(String msg){
        super(msg);
    }
    public LoginNotFoundException(){
        super();
    }

}
