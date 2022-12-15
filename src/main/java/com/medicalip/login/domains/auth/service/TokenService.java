package com.medicalip.login.domains.auth.service;

import com.medicalip.login.domains.commons.response.SingleResult;

public interface TokenService {

	SingleResult generateJwtToken(String email);

}
