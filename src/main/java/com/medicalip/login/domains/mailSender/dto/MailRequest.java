package com.medicalip.login.domains.mailSender.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MailRequest {
    private String userMail;
    private String urlDelimiter;
    private String locale;
    private String authKey;
}
