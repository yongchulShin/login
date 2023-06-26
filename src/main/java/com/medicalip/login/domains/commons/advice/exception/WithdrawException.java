package com.medicalip.login.domains.commons.advice.exception;

public class WithdrawException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public WithdrawException(String msg, Throwable t){
        super(msg,t);
    }
    public WithdrawException(String msg){
        super(msg);
    }
    public WithdrawException(){
        super();
    }

    
}
