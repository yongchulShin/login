package com.medicalip.login.domains.mailSender.service.impl;

import com.medicalip.login.domains.commons.util.EncryptUtil;
import com.medicalip.login.domains.mailSender.dto.MailRequest;
import com.medicalip.login.domains.mailSender.entity.UserAuth;
import com.medicalip.login.domains.mailSender.repo.UserAuthRepository;
import com.medicalip.login.domains.mailSender.service.SendMailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

import javax.mail.internet.MimeMessage;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Random;


@Service
public class SendMailServiceImpl implements SendMailService {
    

	@Value("${loginapi.address}")
	private String serverAddress;

	@Autowired
	private JavaMailSender mailSender;

	@Autowired
	private UserAuthRepository userAuthRepository;
	@Autowired
	private SpringTemplateEngine templateEngine;
	private int size;
	private String tisepx_login = "https://tisepx.com/signIn";
	private String home_login = "https://medicalip.com/login/";
	private String tisepxENNewPassHtml = "email/tisepx_email_en";
	private String tisepxKRNewPassHtml = "email/tisepx_email_kr";
	private String homeENNewPassHtml = "email/email_en";
	private String homeKRNewPassHtml = "email/email_kr";

	public void setMailSender(JavaMailSender mailSender) {
		this.mailSender = mailSender;
	}

	@Override
	public String send(String userEmail, String urlDelimiter) throws NoSuchAlgorithmException{
		MimeMessage message = mailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message);

//		String host = "https://medicalip.net:8085";
		String host = serverAddress;
		String authKey = EncryptUtil.sha256(getKey(8));
		String subject = "[MEDICALIP] 회원가입을 위한 인증메일 안내입니다.";
		String content = "<p style='font-size:18px; color:#333; '>Please confirm that you want to use this as your MEDICAL IP account email address.</p><br/><br/>"
				+ "<a style='display:inline-block; background:#004C7E; color:white; padding: 10px 10px; border-radius: 100px; width:250px; height:30px; text-align:center; font-size:18px;vertical-align:middle' "
				+ "href ='"
				+ host + "/userInfo/Verification?&email=" // test용
				+ userEmail + "&authKey=" + authKey + "&urlDelimiter="+urlDelimiter+"'>Verify Your Email Address</a>";
		try {
			helper.setFrom("contact@medicalip.com");
			helper.setTo(userEmail);
			helper.setSubject(subject);
			helper.setText(content,true);

		} catch (Exception e) {
		}
		mailSender.send(message);

		return authKey;
	}

	@Override
	public String sendMail(String userEmail, String urlDelimiter, String locale) throws NoSuchAlgorithmException{
		MimeMessage message = mailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message);

//		String host = "https://medicalip.net:8085";
		String host = serverAddress;
		String authKey = EncryptUtil.sha256(getKey(8));
		String subject = "[MEDICALIP] This is a certification email guide for membership registration.";

		String content = "";
		System.out.println("urlDelimiter :: " + urlDelimiter);
		if(urlDelimiter == null || urlDelimiter.equals("")) { // 홈페이지 인증메일
			content =
					"<head>"
							+"    <meta charset=UTF-8>"
							+"    <meta http-equiv=X-UA-Compatible content=IE=edge>"
							+"    <meta http-equiv=Content-Type content=text/html; charset=utf-8 >"
							+"    <meta name=viewport content='width=device-width, initial-scale=1.0'>"
							+"    <link href=https://fonts.googleapis.co004C7Em/css2?family=Noto+Sans+KR:wght@100;300;400;500;700;900&display=swap rel=stylesheet>"
							+"    <link href=https://fonts.googleapis.com/css2?family=Roboto:ital,wght@0,100;0,300;0,400;0,500;0,700;0,900;1,100;1,300;1,400;1,500;1,700;1,900&display=swap rel=stylesheet>"
							+"    <title>Home_mail_en</title>"
							+"</head>"
							+"<body style='margin:0;'>"
							+"<div class=wrap style='max-width:460px;margin:40px auto;font-family:Roboto !important;-webkit-text-size-adjust: none;-moz-text-size-adjust: none; -ms-text-size-adjust: none; -o-text-size-adjust: none;'>                                                     "
							+"    <div class=content style='background-color:#fff;margin:0 8px;'>                                                                                                                                                                                                             "
							+"        <div class=top style='border-top:5px solid #004C7E;'>                                                                                                                                                                                                                   "
							+"            <div class=top_gap style='padding:28px;'>                                                                                                                                                                                                                           "
							+"                <img src='https://medicalip.com/images/logos/logo_black.png' alt=logo>                                                                                                                                                                                                                             "
							+"            </div>                                                                                                                                                                                                                                                                "
							+"        </div>                                                                                                                                                                                                                                                                    "
							+"        <div class=center style='padding:28px;'>                                                                                                                                                                                                                                "
							+"            <h2 style='font-size:20px;margin:0;color:#30363A;'>Welcome to MEDICAL IP!</h2>                                                                                                                                                                                        "
							+"            <p style='width:100%;padding:28px 0;margin:0;color:#757575;line-height:1.5;'>Thank you for signing up to MEDICAL IP.<br>We've sent you this email to verify your email address to provide our services.<br>Simply click the button below to activate your account.</p>"
							+"            <div class=mail style='width:100%;font-size:20px;font-weight:900;margin:0 0 28px 0;'>                                                                                                                                                                               "
							+"                <a href='mailto:" + userEmail + "' style='margin:0;word-break: break-all;text-decoration:none;color:#000;'>" + userEmail + "</a>"
							+"            </div>                                                                                                                                                                                                                                                                "
							+"            <a style='display:inline-block; padding:12px 24px; background-color:#004C7E;border:0;border-radius:24px;color:#fff;font-size:16px;cursor:pointer;text-decoration:none;'"
							+ 					"href ='"
							+ 						host + "/mail/Verification?&email=" // test용                                                                                                                                                                                                                                 "
							+ 						userEmail + "&authKey=" + authKey + "&urlDelimiter="+ urlDelimiter +"&locale="+locale+"'>Verify your email</a>"
							+"        </div>                                                                                                                                                                                                                                                                    "
							+"        <div class=bottom style='padding:28px;font-size:12px;color:#757575;background-color:#F5F5F5;'>                                                                                                                                                                          "
							+"            <p style='margin:0;'>If you need any help, please email <br><a href='mailto:contact@medicalip.com' style='color:#2156e7;font-weight:600;text-decoration:none;'>contact@medicalip.com</a>We will get back to you shortly.</p>                                                                     "
							+"        </div>                                                                                                                                                                                                                                                                    "
							+"    </div>                                                                                                                                                                                                                                                                        "
							+"</div></body>";
		}else if(urlDelimiter.equals("tisepx")) { //tisepx 인증메일
			content =
					"<head>"
					+"    <meta charset=UTF-8>"
					+"    <meta http-equiv=X-UA-Compatible content=IE=edge>"
					+"    <meta http-equiv=Content-Type content=text/html; charset=utf-8 >"
					+"    <meta name=viewport content=width=device-width, initial-scale=1.0>"
					+"    <link href='https://fonts.googleapis.co004C7Em/css2?family=Noto+Sans+KR:wght@100;300;400;500;700;900&display=swap' rel=stylesheet>"
					+"    <link href='https://fonts.googleapis.com/css2?family=Roboto:ital,wght@0,100;0,300;0,400;0,500;0,700;0,900;1,100;1,300;1,400;1,500;1,700;1,900&display=swap' rel=stylesheet>"
					+"    <title>TiSepX_mail_en</title>"
					+"</head>"
					+"<body style='margin:0;'>"
					+"    <div class=wrap style='max-width:460px;margin:40px auto;font-family:Roboto,Noto Sans KR !important;-webkit-text-size-adjust: none;-moz-text-size-adjust: none; -ms-text-size-adjust: none; -o-text-size-adjust: none;'>"
					+"        <div class=content style='background-color:#fff;margin:0 8px;'>"
					+"            <div class=top style='border-top:5px solid #000000;'> "
					+"                <div class=top_gap style='padding:28px;'>"
					+"                    <img src='https://medicalip.com/images/logos/logo_black.png' alt=logo>"
					+"                </div>"
					+"            </div>"
					+"            <div class=center style='padding:28px;'>"
					+"                <h2 style='font-size:20px;margin:0;color:#30363A;'>Welcome to TiSepX!</h2>"
					+"                <p style='width:100%;padding:28px 0;margin:0;color:#757575;line-height:1.5;'>Thank you for signing up to TiSepX,<br>x-ray analysis solution.<br>We've sent you this email to verify your email address to provide our services.<br>Simply click the button below to activate your account.</p>"
					+"                <div class=mail style='width:100%;font-size:20px;font-weight:900;margin:0 0 28px 0;'>"
					+"                    <a href='mailto:" + userEmail + "' style='margin:0;word-break: break-all;text-decoration:none;color:#000;'>" + userEmail + "</a>"
					+"                </div>"
					+"                <a style='display:inline-block; padding:12px 24px;background-color:#000;border:0;border-radius:24px;color:#fff;font-size:16px;font-family:Roboto;cursor: pointer;text-decoration:none;'"
					+ 					"href ='"
					+ 						host + "/mail/Verification?&email=" // test용                                                                                                                                                                                                                                 "
					+ 						userEmail + "&authKey=" + authKey + "&urlDelimiter="+urlDelimiter+"&locale="+locale+"'>Verify your email</a>"
					+"            </div>"
					+"            <div class=bottom style='padding:28px;font-size:12px;color:#757575;background-color:#F5F5F5;'>"
					+"                <p style='margin:0;'>If you need any help, please email <br><a href='mailto:contact@medicalip.com' style='color:#2156e7;font-weight:600;text-decoration:none;'>contact@medicalip.com</a>We will get back to you shortly.</p>"
					+"            </div>"
					+"        </div>"
					+"    </div>"
					+"</body>";
		}

		try {
			helper.setFrom("contact@medicalip.com");
			helper.setTo(userEmail);
			helper.setSubject(subject);
			helper.setText(content,true);

		} catch (Exception e) {
			e.printStackTrace();
		}
		mailSender.send(message);

		return authKey;
	}

	@Override
	public String sendMailKr(String userEmail, String urlDelimiter, String locale) throws NoSuchAlgorithmException{
		MimeMessage message = mailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message);

//		String host = "https://medicalip.net:8085";
		String host = serverAddress;
		String authKey = EncryptUtil.sha256(getKey(8));
		String subject = "[MEDICALIP] 회원가입을 위한 인증메일 안내입니다.";
		String content =
				"<head>"
						+"    <meta charset=UTF-8>"
						+"    <meta http-equiv=X-UA-Compatible content=IE=edge>"
						+"    <meta http-equiv=Content-Type content=text/html; charset=utf-8 >"
						+"    <meta name=viewport content=width=device-width, initial-scale=1.0>"
						+"    <link href='https://fonts.googleapis.co004C7Em/css2?family=Noto+Sans+KR:wght@100;300;400;500;700;900&display=swap' rel=stylesheet>"
						+"    <link href='https://fonts.googleapis.com/css2?family=Roboto:ital,wght@0,100;0,300;0,400;0,500;0,700;0,900;1,100;1,300;1,400;1,500;1,700;1,900&display=swap' rel=stylesheet>"
						+"    <title>Home_mail_kr</title>"
						+"</head>"
						+"<body style=margin:0;>"
						+"    <div class=wrap style='max-width:460px;margin:40px auto;font-family:Noto Sans KR,Roboto !important;-webkit-text-size-adjust: none;-moz-text-size-adjust: none; -ms-text-size-adjust: none; -o-text-size-adjust: none;'>"
						+"        <div class=content style='background-color:#fff;margin:0 8px;'>"
						+"            <div class=top style='border-top:5px solid #004C7E;'>"
						+"                <div class=top_gap style='padding:28px;'>"
						+"                    <img src='https://medicalip.com/images/logos/logo_black.png' alt=logo>"
						+"                </div>"
						+"            </div>"
						+"            <div class=center style='padding:28px;'>"
						+"                <h2 style='font-size:20px;margin:0;color:#30363A;'>메디컬아이피 가입을 환영합니다!</h2>"
						+"                <p style='width:100%;padding:28px 0;margin:0;color:#757575;'>메디컬아이피 홈페이지에 가입해 주셔서 감사합니다.<br>서비스 이용을 위한 이메일 주소 인증을 요청하셨습니다.<br>아래 <b>[이메일 인증 확인]</b> 버튼 클릭 시<br>회원가입이 완료됩니다.</p>"
						+"                <div class=mail style='width:100%;font-size:20px;font-weight:900;margin:0 0 28px 0;'>"
						+"                    <a href='mailto:" + userEmail +"' style='margin:0;word-break: break-all;text-decoration:none;color:#000;'>"+ userEmail +"</a>"
						+"                </div>"
						+"                <a style='display:inline-block;padding:12px 24px;background-color:#004C7E;border:0;border-radius:24px;color:#fff;font-size:16px;font-family:Noto Sans KR;cursor: pointer;text-decoration:none;'"
						+ 					"href ='"
						+ 						host + "/mail/Verification?&email=" // test용                                                                                                                                                                                                                                 "
						+ 						userEmail + "&authKey=" + authKey + "&urlDelimiter="+urlDelimiter+"&locale="+locale+"'>이메일 인증 확인</a>"
						+"            </div>"
						+"            <div class=bottom style='padding:28px;font-size:12px;color:#757575;background-color:#F5F5F5;'>"
						+"                <p style='margin:0;'>회원가입 관련 문의사항이 있으신가요?<br><a href='mailto:contact@medicalip.com' style='color:#2156e7;font-weight:600;text-decoration:none;'>contact@medicalip.com</a>으로 연락주시면 신속하게 답변드리겠습니다.</p>"
						+"            </div>"
						+"        </div>"
						+"    </div>"
						+"</body>";
		try {
			helper.setFrom("contact@medicalip.com");
			helper.setTo(userEmail);
			helper.setSubject(subject);
			helper.setText(content,true);

		} catch (Exception e) {
		}
		mailSender.send(message);

		return authKey;
	}

	public void makeMailByHtml(String userEmail, String subject, String html){
		MimeMessage message = mailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message, "UTF-8");
		try {
			helper.setFrom("contact@medicalip.com");
			helper.setTo(userEmail);
			helper.setSubject(subject);
			helper.setText(html, true);
		} catch (Exception e) {
		}
		mailSender.send(message);
	}

	//인증키 생성
	@Override
	public String getKey(int size){
		this.size = size;
		return getAuthcode();
	}
	private String getAuthcode(){
		Random random = new Random();
		StringBuffer authCode = new StringBuffer();

		while(authCode.length()<size){
			if(random.nextBoolean()){
				authCode.append((char)((int)(random.nextInt(26))+65));
			}
			else{
				authCode.append(random.nextInt(10));
			}
		}
		return authCode.toString();
	}


	@Override
	public void resetPassKR(String userEmail, String urlDelimiter, Context context){
		String subject = "Medicalip 비밀번호 초기화";
		if (urlDelimiter.equals("tisepx")){
			context.setVariable("host", tisepx_login);
			String html = templateEngine.process(tisepxKRNewPassHtml, context);
			makeMailByHtml(userEmail, subject, html);
		}else{
			context.setVariable("host", home_login);
			String html = templateEngine.process(homeKRNewPassHtml, context);
			makeMailByHtml(userEmail, subject, html);
		}
	}


	@Override
	public void resetPassEN(String userEmail, String urlDelimiter, Context context){
		String subject = "Medicalip Password initialization";

		if (Objects.equals(urlDelimiter, "tisepx")){
			context.setVariable("host", tisepx_login);
			String html = templateEngine.process(tisepxENNewPassHtml, context);
			makeMailByHtml(userEmail, subject, html);
		}
		else {
			context.setVariable("host", home_login);
			String html = templateEngine.process(homeENNewPassHtml, context);
			makeMailByHtml(userEmail, subject, html);
		}
	}

	@Override
	public String resetPass(String userEmail, String locale, String urlDelimiter) throws NoSuchAlgorithmException {
		String newPass = getKey(8);
		Context context = new Context();
		context.setVariable("newPass", newPass);

		if(locale.toUpperCase().equals("EN")) {
			resetPassEN(userEmail, urlDelimiter, context);
		} else {
			resetPassKR(userEmail, urlDelimiter, context);
		}
		return newPass;
	}

	@Override
	public void setAuthKey(MailRequest mailRequest) {
		userAuthRepository.save(
				UserAuth.builder()
						.userEmail(mailRequest.getUserMail())
						.authKey(mailRequest.getAuthKey())
						.regDttm(LocalDateTime.now()).build()
		);
	}

	@Override
	public UserAuth getAuthInfo(String userEmail) { return userAuthRepository.findByUserEmail(userEmail); }

	@Override
	public void deleteAuthKey(String userEmail) {
		userAuthRepository.deleteById(userEmail);
	}
}


