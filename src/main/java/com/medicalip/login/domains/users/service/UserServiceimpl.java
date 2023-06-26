package com.medicalip.login.domains.users.service;

import com.medicalip.login.domains.auth.entity.Token;
import com.medicalip.login.domains.auth.repo.TokenRepository;
import com.medicalip.login.domains.commons.advice.exception.CUserNotFoundException;
import com.medicalip.login.domains.commons.advice.exception.LoginNotFoundException;
import com.medicalip.login.domains.commons.advice.exception.UnauthorizedEmailException;
import com.medicalip.login.domains.commons.advice.exception.WithdrawException;
import com.medicalip.login.domains.commons.jwt.TokenUtils;
import com.medicalip.login.domains.commons.response.TokenResponse;
import com.medicalip.login.domains.commons.util.Constants;
import com.medicalip.login.domains.commons.util.EncryptUtil;
import com.medicalip.login.domains.match.entity.MatchUserCredit;
import com.medicalip.login.domains.match.entity.MatchUserProduct;
import com.medicalip.login.domains.match.entity.MatchUserRole;
import com.medicalip.login.domains.match.repo.MatchUserCreditRepository;
import com.medicalip.login.domains.match.repo.MatchUserProductRepository;
import com.medicalip.login.domains.match.repo.MatchUserRoleRepository;
import com.medicalip.login.domains.newpromotion.entity.NewPromotion;
import com.medicalip.login.domains.newpromotion.repo.NewPromotionRepository;
import com.medicalip.login.domains.users.dto.LoginRequest;
import com.medicalip.login.domains.users.dto.UserRequest;
import com.medicalip.login.domains.users.entity.UserRole;
import com.medicalip.login.domains.users.entity.Users;
import com.medicalip.login.domains.users.repo.UserRoleRepository;
import com.medicalip.login.domains.users.repo.UsersRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService{
	
	private final TokenUtils tokenUtils;
	private final UsersRepository usersRepository;
	private final UserRoleRepository userRoleRepository;
	private final TokenRepository tokenRepository;
	private final MatchUserRoleRepository matchUserRoleRepository;
	private final AuthenticationManager authenticationManager;
	private final NewPromotionRepository newPromotionRepository;
	private final MatchUserProductRepository matchUserProductRepository;
	private final MatchUserCreditRepository matchUserCreditRepository;

	public Users signUp(UserRequest userRequest) {
		System.out.println("signUp Function");
		Users users = null;
		try {
			users = usersRepository.save(
					Users.builder()
							.nationCode(userRequest.getNationCode())
							.userEmail(userRequest.getUserEmail())
							.userPw(EncryptUtil.sha512(userRequest.getUserPw()))
							.userName(userRequest.getUserName())
							.userNum(userRequest.getUserNum())
							.institude(userRequest.getInstitude())
							.enabled("N")
							.isDrop("N")
							.isSubscribe(userRequest.getIsSubscribe())
							.subscribeEmail(userRequest.getIsSubscribe().equals("N") ?
									null : userRequest.getUserEmail())
							.updDttm(LocalDateTime.now())
							.regDttm(LocalDateTime.now())
							.userSeq(usersRepository.count() + 1).build()
			);
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
//		CommonResult result = new CommonResult();
//		result.setCode(200);
//		result.setMsg("회원가입에 성공하였습니다.");
		return users;
	}
	
	public TokenResponse signIn(LoginRequest.Login login) {
        try {
			System.out.println("[service sign in]");
        	Users users = usersRepository.findByUserEmail(login.getUserEmail()).get();

			//이메일 인증이 안된
			if(users.getEnabled().equals("N"))
				throw new UnauthorizedEmailException();

			//삭제 된
			if(users.getIsDrop().equals("Y"))
				throw new WithdrawException();

//        	tokenUtils.generateJwtToken(users);
        	// 1. Login ID/PW 를 기반으로 Authentication 객체 생성
        	// 이때 authentication 는 인증 여부를 확인하는 authenticated 값이 false
        	UsernamePasswordAuthenticationToken authenticationToken = login.toAuthentication();
        	System.out.println("authenticationToken :: " + authenticationToken);

        	// 2. 실제 검증 (사용자 비밀번호 체크)이 이루어지는 부분
            // authenticate 매서드가 실행될 때 CustomUserDetailsService 에서 만든 loadUserByUsername 메서드가 실행
        	Authentication authentication = authenticationManager.authenticate(authenticationToken);

            System.out.println("authentication :: " + authentication.getPrincipal());
        	// 3. 인증 정보를 기반으로 JWT 토큰 생성
        	Token tokenInfo = tokenUtils.generateToken(authentication);
        	Token updateOp = tokenRepository.findByUsers(users);
			List<MatchUserRole> matchUserRole = matchUserRoleRepository.findByUsers(users.getUserSeq());
//					.stream().filter(h -> h.getUsers().equals(users))
//					.collect(Collectors.toList())
//					;
        	// 4. RefreshToken 저장 (expirationTime 설정을 통해 자동 삭제 처리)
        	if(updateOp != null) {
        		System.out.println("Token Update");
				updateOp.setRefreshToken(tokenInfo.getRefreshToken());
				updateOp.setAccessToken(tokenInfo.getAccessToken());
				updateOp.setRefreshTokenExpireDt(tokenUtils.createExpireDate(Constants.REFRESH_TOKEN_VALID_TIME));
        		tokenRepository.save(updateOp);
        	}else {
        		System.out.println("최초 Login");
        		//db 등록
        		tokenRepository.save( // 신규(최초)
    				Token.builder()
        				.accessToken(tokenInfo.getAccessToken())
        				.refreshToken(tokenInfo.getRefreshToken())
        				.users(users)
        				.refreshTokenExpireDt(tokenUtils.createExpireDate(Constants.REFRESH_TOKEN_VALID_TIME))
        				.build());
        	}

        	return new TokenResponse(HttpStatus.OK
					,"로그인에 성공했습니다."
        			,tokenInfo.getAccessToken()
        			,tokenInfo.getRefreshToken()
					,matchUserRole
			);
        }catch (BadCredentialsException e) { // 패스워드 불일치
			throw new LoginNotFoundException();
    	}catch (NoSuchElementException e) { //가입되지 않은
			throw new CUserNotFoundException();
		}
	}

	public List<Users> findUsers() {
	  return usersRepository.findAll(Sort.by(Sort.Direction.DESC, "userSeq"));
	}
		
	public Optional<Users> findByEmail(String email) {
		return usersRepository.findByUserEmail(email);
	}

	@Override
	public Optional<Users> findBySeq(long seq) {
		return usersRepository.findByUserSeq(seq);
	}

	public UserRole saveUserRole(UserRole userRole) {
		log.info("Saving new role {} to the db", userRole.getRoleName());
        return userRoleRepository.save(userRole);
	}

	@Override
	public void newUserPromotion(UserRequest userRequest) {
		Users users = usersRepository.findByUserEmail(userRequest.getUserEmail()).get();
		//TB_INFO_NEWUSER_PROMOTION 전체 검색
		List<NewPromotion> promotions = newPromotionRepository.findAll();

		//Function Level Type : PRODUCT
		List<NewPromotion> products = promotions.stream()
				.filter(p -> p.getFunctionLevelType().equals("PRODUCT"))
				.collect(Collectors.toList());

		//Function Level Type : CREDIT
		List<NewPromotion> credits = promotions.stream()
				.filter(p -> p.getFunctionLevelType().equals("CREDIT"))
				.collect(Collectors.toList());

//		// TB_MATCH_USER_PRODUCT INSERT
		products.forEach(p ->
						matchUserProductRepository.save(
								MatchUserProduct.builder()
										.userSeq(users.getUserSeq())
										.serviceId(p.getServiceId())
										.functionLevelId(p.getFunctionLevelId())
										.functionStartDttm(LocalDateTime.now())
										.functionEndDttm(userRequest.getIsEducation().equals("N")?
												LocalDateTime.now().plusDays(p.getPromotionPeriodDay())
												: LocalDateTime.now().plusDays(p.getPromotionPeriodDayForTrainee()))
										.licenseType(p.getLicenseType())
										.isUpdate("Y")
										.updateServiceLimitVersion("3.0")
										.build()
						));
//		// TB_MATCH_USER_CREDIT INSERT
		credits.forEach(c ->
						matchUserCreditRepository.save(
								MatchUserCredit.builder()
										.userSeq(users.getUserSeq())
										.serviceId(c.getServiceId())
										.functionLevelId(c.getFunctionLevelId())
										.usableCount(c.getCount())
										.usedCount(0)
										.enabled("Y")
										.regDttm(LocalDateTime.now())
										.updDttm(LocalDateTime.now())
										.build()
						)
				);
	}

	@Override
	public void saveMatchUserRole(Users users) {
//		Optional<Users> users = usersRepository.findByUserEmail(userEmail);
//		if(!users.isPresent())
//			throw new CUserNotFoundException();
		matchUserRoleRepository.save(
				MatchUserRole.builder()
						.userRoles(userRoleRepository.findByRoleName("ROLE_USER"))
						.users(users)
						.build()
		);
	}

	@Override
	public List<Users> getUserInfo() {
		return usersRepository.findAll();
	}
}
