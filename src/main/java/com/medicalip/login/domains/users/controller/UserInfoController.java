package com.medicalip.login.domains.users.controller;

import com.medicalip.login.domains.commons.advice.exception.RequestParameterException;
import com.medicalip.login.domains.commons.advice.exception.UnauthorizedException;
import com.medicalip.login.domains.commons.controller.BaseController;
import com.medicalip.login.domains.commons.jwt.TokenUtils;
import com.medicalip.login.domains.commons.response.CommonResult;
import com.medicalip.login.domains.commons.service.ResponseService;
import com.medicalip.login.domains.commons.util.Constants;
import com.medicalip.login.domains.commons.util.EncryptUtil;
import com.medicalip.login.domains.mailSender.service.SendMailService;
import com.medicalip.login.domains.match.entity.MatchUserRole;
import com.medicalip.login.domains.redis.service.RedisService;
import com.medicalip.login.domains.users.dto.UserRequest;
import com.medicalip.login.domains.users.entity.Users;
import com.medicalip.login.domains.users.service.UserInfoService;
import com.medicalip.login.domains.users.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/userInfo")
@Tag(name = "2. UserInfo", description = "UserInfo Controller")
public class UserInfoController extends BaseController {
	
	private final UserService userService; // user Service
	private final UserInfoService userInfoService; // user Service
	private final ResponseService responseService;
	private final TokenUtils tokenUtils; // JWT 토큰을 생성 및 검증 모듈 클래스
	private final RedisService redisService;
	private final SendMailService sendMailService;
	@Operation(summary = "사용자 정보조회", description = "사용자의 정보를 조회한다.")
	@PostMapping(value = "/getUserInfo")
	public CommonResult getUserInfo(@RequestHeader Map<String, String> headers, @RequestBody(required = false) UserRequest userRequest) {
		log.info("[getUserInfo]");
		MatchUserRole matchUserRole =  redisService.getRedis(Long.parseLong(headers.get("userseq")));
		return responseService.getSingleResult(
				userService.findBySeq(userRequest == null || userRequest.getUserSeq().equals("") ?
						matchUserRole.getUsers().getUserSeq() : Long.parseLong(userRequest.getUserSeq())
				)
		);
	}

	@Operation(summary = "사용자 조회", description = "사용자를 조회한다.")
	@PostMapping(value = "/getUserList")
	public CommonResult getUserList(@RequestHeader Map<String, String> headers) {
		MatchUserRole matchUserRole =  redisService.getRedis(Long.parseLong(headers.get("userseq")));
		chkRole(new String[] {Constants.ROLE_ADMIN, Constants.ROLE_LICENSE}, matchUserRole.getUserRoles());

		return responseService.getSingleResult(userService.getUserInfo());
	}

	@Operation(summary = "사용자 정보 업데이트", description = "사용자 정보를 업데이트한다.")
	@PostMapping(value = "/updateUserInfo")
	public CommonResult updateUserInfo(
			@Parameter(name = "userRequest", schema = @Schema(required = true, example =
					"{\n" +
					"\"userSeq\":\"\",\n" +
					"\"nationCode\":\"\",\n" +
					"\"userName\":\"\",\n" +
					"\"userNum\":\"\",\n" +
					"\"institude\":\"\",\n" +
					"\"enabled\":\"Y\",\n" +
					"\"isDrop\":\"N\",\n" +
					"\"dropDttm\":\"\"\n" +
					"}")) @RequestBody UserRequest userRequest, @RequestHeader Map<String, String> headers) {
		log.info(headers.get("userseq"));
		MatchUserRole matchUserRole =  redisService.getRedis(Long.parseLong(headers.get("userseq")));
		Users users = matchUserRole.getUsers();

		//userSeq == null 은 홈페이지, userSeq != null 은 adminConsole)
		if(userRequest.getUserSeq() == null){
			userRequest.setUserEmail(users.getUserEmail());
			userRequest.setUserSeq(String.valueOf(users.getUserSeq()));
		}else{
			log.info("chk Role");
			chkRole(new String[] { Constants.ROLE_ADMIN }, matchUserRole.getUserRoles());
		}

		// isDrop = Y or y인 경우 dropDttm은 서버 시간 기준으로 등록.
		if (userRequest.getIsDrop() != null && userRequest.getIsDrop().equalsIgnoreCase("Y"))
			userRequest.setDropDttm(LocalDateTime.now());

		userRequest.setDropDttm(null);

		userInfoService.updateUserInfo(userRequest);
		return responseService.getSuccessResult();
	}

	@Operation(summary = "사용자 패스워드 변경", description = "사용자의 패스워드를 변경한다.")
	@PostMapping(value = "/resetPassword")
	public CommonResult resetPassword(
		  @Parameter(name = "userRequest", schema = @Schema(required = true, example =
		  "{\"currentPw\":\"\",\n"
		  + "\"newPw\":\"\"\n" + "}")) @RequestBody UserRequest userRequest,
		  @RequestHeader Map<String, String> headers) throws NoSuchAlgorithmException {
		MatchUserRole matchUserRole =  redisService.getRedis(Long.parseLong(headers.get("userseq")));
		Users users = matchUserRole.getUsers();

		String currentPw = EncryptUtil.sha512(userRequest.getCurrentPw());
		if (!users.getUserPw().equals(currentPw)) {
			throw new UnauthorizedException();
		}

		userRequest.setUserPw(userRequest.getNewPw());
		userRequest.setUserEmail(users.getUserEmail());
		userInfoService.resetPassword(userRequest);

		return responseService.getSuccessResult();
	}

	@Operation(summary = "사용자 검색", description = "사용자를 검색한다.")
	@PostMapping(value = "/searchUser")
	public CommonResult searchUser(@Parameter(name = "userRequest", schema = @Schema(required = false, example = "{\n"+
			"\"userEmail\":\"\",\n"+
			"\"userName\":\"\"\n"+"}"
	))@RequestBody UserRequest userRequest,
	   @RequestHeader Map<String, String> headers
	)
	{
		log.info(headers.get("userseq"));
		MatchUserRole matchUserRole =  redisService.getRedis(Long.parseLong(headers.get("userseq")));
		if(!matchUserRole.getUserRoles().getRoleName().equals("ROLE_ADMIN"))
			throw new UnauthorizedException();
		//email, name 둘 다 값이 없으면 예외처리.
		if(userRequest.getUserEmail().isEmpty()&&userRequest.getUserName().isEmpty())
			throw new RequestParameterException();
		return responseService.getListResult(userInfoService.searchUser(userRequest));
	}

	@Operation(summary = "사용자 리스트 다운로드", description = "사용자들의 리스트를 다운로드한다.")
	@GetMapping(value = "/downloadUserList")
	public ResponseEntity<String> downloadUserList(@RequestHeader Map<String, String> headers) throws Exception{
		log.info(headers.get("userseq"));
		MatchUserRole matchUserRole =  redisService.getRedis(Long.parseLong(headers.get("userseq")));
		if(!matchUserRole.getUserRoles().getRoleName().equals("ROLE_ADMIN"))
			throw new UnauthorizedException();
		//파일명 셋팅
		SimpleDateFormat date = new SimpleDateFormat("yyMMddHHmmSS");
		String fileName = "userList"+date.format(new Date())+".csv";

		//csvFile 만들기
		StringBuffer csvFile = userInfoService.downloadUserList();
		//헤더 추가
		HttpHeaders header = new HttpHeaders();
		header.add("Content-Disposition", "attachment; filename=\"" + fileName + "\"");
		header.add("Content-Type", "text/csv; charset=UTF8");
		return new ResponseEntity<String>(csvFile.toString(), header, HttpStatus.CREATED);
	}

	@Operation(summary = "사용자 활성화", description = "사용자의 활성화를 처리한다.")
	@PostMapping(value = "/activationUser")
	public CommonResult	activationUser(
			@Parameter(name = "userRequest", schema = @Schema(required = true, example =
					"{\n"+
					"\"userEmail\":\"필수입력\"\n"+
					"}"))
			@RequestBody UserRequest userRequest,
			@RequestHeader Map<String, String> headers) throws NoSuchAlgorithmException
	{
		MatchUserRole matchUserRole =  redisService.getRedis(Long.parseLong(headers.get("userseq")));
		if(!matchUserRole.getUserRoles().getRoleName().equals("ROLE_ADMIN"))
			throw new UnauthorizedException();

		//	if( 0 >= userInfoMgmtReq.getUserSeq())
		if(userRequest.getUserEmail() == null || userRequest.getUserEmail().equals(""))
		{
			log.info("userEmail is empty");
			throw new RequestParameterException();
		}
		userInfoService.activationUser(userRequest);
		return responseService.getSuccessResult();
	}
	@Operation(summary = "사용자 탈퇴/탈퇴취소", description = "사용자의 탈퇴/탈퇴취소를 처리한다.")
	@PostMapping(value = "/secessionUser")
	public CommonResult	secessionUser(
			@Parameter(name = "userRequest", schema = @Schema(required = true, example =
			"{\n"+
			"\"userEmail\":\"필수입력\"\n"+
			"}"))
			@RequestBody UserRequest userRequest,
			@RequestHeader Map<String, String> headers) throws NoSuchAlgorithmException
	{
		MatchUserRole matchUserRole =  redisService.getRedis(Long.parseLong(headers.get("userseq")));
		if(!matchUserRole.getUserRoles().getRoleName().equals("ROLE_ADMIN"))
			throw new UnauthorizedException();

		//	if( 0 >= userInfoMgmtReq.getUserSeq())
		if(userRequest.getUserEmail() == null || userRequest.getUserEmail().equals(""))
		{
			log.info("userEmail is empty");
			throw new RequestParameterException();
		}
		userInfoService.secessionUser(userRequest);
		return responseService.getSuccessResult();
	}

	@Operation(summary = "사용자 계정 삭제", description = "사용자 계정을 삭제한다.")
	@PostMapping(value = "/deleteUser")
	public CommonResult	deleteUser(
			@Parameter(name = "userRequest", schema = @Schema(required = true, example =
					"{\n"+
							"\"userEmail\":\"필수입력\"\n"+
							"}"))
			@RequestBody UserRequest userRequest,
			@RequestHeader Map<String, String> headers) throws NoSuchAlgorithmException
	{
		MatchUserRole matchUserRole =  redisService.getRedis(Long.parseLong(headers.get("userseq")));
		if(!matchUserRole.getUserRoles().getRoleName().equals("ROLE_ADMIN"))
			throw new UnauthorizedException();

		//	if( 0 >= userInfoMgmtReq.getUserSeq())
		if(userRequest.getUserEmail() == null || userRequest.getUserEmail().equals(""))
			throw new RequestParameterException();

		userInfoService.deleteUser(userRequest);
		return responseService.getSuccessResult();
	}
	@Operation(summary = "비밀번호 분실", description = "비밀번호 분실 시 비밀번호 초기화 이메일을 전송한다.")
	@PostMapping(value = "/findPass")
	public CommonResult findPass(
			@RequestParam(name = "userEmail", required = true)String userEmail,
			@RequestParam(name = "userName", required = true) String userName,
			@RequestParam(name = "locale", required = false) String locale,
			@RequestParam(name = "urlDelimiter", required = false) String urlDelimiter
	) throws NoSuchAlgorithmException {
		System.out.println("findPass");
		Users users = userService.findByEmail(userEmail).get();

		//실패메세지
		String msg = "";
		if(users == null){
			System.out.println("가입 XXX");
			msg = "가입되지 않은 회원입니다.";
			return responseService.getFailResult(400, msg);
		}
		if(!users.getUserName().equals(userName)){
			System.out.println("정보 불일치");
			System.out.println("테스트 :: " + userName);
			System.out.println("테스트 :: " + users.getUserName());
			msg = "회원정보가 일치하지 않습니다.";
			return responseService.getFailResult(403, msg);
		}

		String resetPass = sendMailService.resetPass(userEmail, locale, urlDelimiter);
		log.info("resetPass :: " + resetPass);
		UserRequest userRequest = new UserRequest();
		userRequest.setUserEmail(userEmail);
		userRequest.setUserPw(resetPass);
		userInfoService.resetPassword(userRequest);
		return responseService.getSuccessResult();
	}
}
