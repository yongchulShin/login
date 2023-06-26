package com.medicalip.login.domains.mailSender.service;

import com.medicalip.login.domains.mailSender.dto.MailRequest;
import com.medicalip.login.domains.mailSender.entity.UserAuth;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;

import java.security.NoSuchAlgorithmException;

@Service
public interface SendMailService {

    String send(String userEmail, String urlHost) throws NoSuchAlgorithmException;
    String getKey(int size);
	String sendMail(String userEmail, String urlDelimiter, String locale) throws NoSuchAlgorithmException;
	String sendMailKr(String userEmail, String urlDelimiter, String locale) throws NoSuchAlgorithmException;
    void resetPassKR(String userEmail, String urlDelimiter, Context context);
    void resetPassEN(String userEmail, String urlDelimiter, Context context);
    String resetPass(String userEmail, String locale, String urlDelimiter) throws  NoSuchAlgorithmException;

    void setAuthKey(MailRequest mailRequest);

    UserAuth getAuthInfo(String userEmail);

    void deleteAuthKey(String userEmail);
}
