package com.medicalip.login.domains.auth.dto;

import lombok.Data;

@Data
public class TokenRequest {
    private String accessToken;
    private String refreshToken;
}
