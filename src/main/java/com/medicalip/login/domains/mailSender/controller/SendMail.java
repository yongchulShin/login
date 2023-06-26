package com.medicalip.login.domains.mailSender.controller;

import com.medicalip.login.domains.commons.advice.exception.InvalidInputValueException;
import com.medicalip.login.domains.commons.advice.exception.RequestParameterException;
import com.medicalip.login.domains.commons.response.CommonResult;
import com.medicalip.login.domains.commons.service.ResponseService;
import com.medicalip.login.domains.mailSender.dto.MailRequest;
import com.medicalip.login.domains.mailSender.entity.UserAuth;
import com.medicalip.login.domains.mailSender.service.SendMailService;
import com.medicalip.login.domains.users.dto.UserRequest;
import com.medicalip.login.domains.users.service.UserInfoService;
import com.medicalip.login.domains.users.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import java.security.NoSuchAlgorithmException;

@Slf4j
@Tag(name = "SendMail", description = "메일보내기 TEST")
@RestController
@RequestMapping(value = "mail")
public class SendMail {

    @Autowired
    private SendMailService sendMailService;

    @Autowired
    private ResponseService responseService;

    @Autowired
    private UserService userService;

    @Autowired
    private UserInfoService userInfoService;

    @Operation(summary = "인증메일 보내기", description = "회원가입 시 사용자의 메일로 인증링크를 발송한다.(한글/영문)")
    @PostMapping(value = "/sendMail")
    public CommonResult sendMail(
            @Parameter(name = "mailRequest", schema = @Schema(required = true, example =
                    "{\n" +
                     "\"userMail\":\"\",\n"
                    + "\"urlDelimiter\":\"\",\n"
                    + "\"locale\":\"\"\n"
                    + "}"))
            @RequestBody MailRequest mailRequest)throws NoSuchAlgorithmException{
        String userMail = mailRequest.getUserMail();
        String urlDelimiter = mailRequest.getUrlDelimiter();
        String locale = mailRequest.getLocale();
        String authKey =  "";
        log.info("userMail :: " + userMail);
        log.info("urlDelimiter :: " + urlDelimiter);
        log.info("locale :: " + locale);

    	if(locale.equals("KR")){
    		authKey = sendMailService.sendMailKr(userMail,urlDelimiter,locale);
    	}else {
    		authKey = sendMailService.sendMail(userMail,urlDelimiter,locale);
    	}

        mailRequest.setAuthKey(authKey);
        sendMailService.setAuthKey(mailRequest);
    	log.info("------------------------------");
    	log.info(authKey);
    	
    	return responseService.getSuccessResult();
    }

    @Operation(summary = "메일인증", description = "회원 메일 인증 후 enabled를 Y로 업데이트, 인증키를 삭제한다.")
    @GetMapping(value = "/Verification")
    public RedirectView getAuth(
            @RequestParam(name = "email", required = true) String userEmail,
            @RequestParam(name = "authKey", required = true) String authKey,
            @RequestParam(name = "urlDelimiter", required = false) String urlDelimiter,
            @RequestParam(name = "locale", required = false) String locale
    )
    {
        RedirectView returnUrl = new RedirectView();
        String redirectUrl = "https://medicalip.com";
        //urlDelimiter 확인 -> redirectUrl
        if (urlDelimiter != null && !urlDelimiter.equals("") && urlDelimiter.equals("tisepx")){
            redirectUrl = "http://tisepx.com";
        }

        if(userEmail.equals("") || authKey.equals(""))
            throw new RequestParameterException();

        UserAuth userAuth = sendMailService.getAuthInfo(userEmail);
        if(userAuth == null){
            returnUrl.setUrl(redirectUrl+"/VerificationFailed");
        }else{
            if(!userAuth.getAuthKey().equals(authKey) || !userAuth.getUserEmail().equals(userEmail)){
                if(locale.equals("KR")) {
                    returnUrl.setUrl(redirectUrl+"/KR/VerificationFailed");
                }else {
                    returnUrl.setUrl(redirectUrl+"/VerificationFailed");
                }
            }
        }

        UserRequest userRequest = new UserRequest();
        userRequest.setUserEmail(userEmail);
        //활성화 - Enabled : Y
        userInfoService.activationUser(userRequest);

        //UserAuth 삭제(TB_INFO_USER_AUTH)
        sendMailService.deleteAuthKey(userEmail);


        if(responseService.getSuccessResult().isSuccess()){
            if(locale.equals("KR")) {
                returnUrl.setUrl(redirectUrl+"/KR/Verification");
            }else {
                returnUrl.setUrl(redirectUrl+"/Verification");
            }
        }
        else{
            if(locale.equals("KR")) {
                returnUrl.setUrl(redirectUrl+"/KR/VerificationFailed");
            }else {
                returnUrl.setUrl(redirectUrl+"/VerificationFailed");
            }
        }
    
        return returnUrl;
        
    }
}
