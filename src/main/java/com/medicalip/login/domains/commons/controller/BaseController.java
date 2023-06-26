package com.medicalip.login.domains.commons.controller;

import com.medicalip.login.domains.commons.advice.exception.UnauthorizedException;
import com.medicalip.login.domains.users.entity.UserRole;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

@Controller
public class BaseController {
    protected void chkRole(String[] roles, UserRole userRole)
    {
        boolean result = false;
        for(String role:roles)
        {
            if(userRole.getRoleName().equals(role))
                result = true;
        }
        if(!result)
            throw new UnauthorizedException();
    }
}
