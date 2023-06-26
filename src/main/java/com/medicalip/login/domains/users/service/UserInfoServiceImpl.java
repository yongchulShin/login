package com.medicalip.login.domains.users.service;

import com.medicalip.login.domains.commons.jwt.TokenUtils;
import com.medicalip.login.domains.commons.util.Constants;
import com.medicalip.login.domains.commons.util.EncryptUtil;
import com.medicalip.login.domains.users.dto.UserRequest;
import com.medicalip.login.domains.users.entity.Users;
import com.medicalip.login.domains.users.repo.UsersRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserInfoServiceImpl implements UserInfoService{
	
	private final TokenUtils tokenUtils;
	private final UsersRepository usersRepository;

	@Override
	public void updateUserInfo(UserRequest userRequest) {
		//해당 DB검색
		Users users = null;
		if(userRequest.getUserSeq() != null){
			users = usersRepository.findByUserSeq(Long.parseLong(userRequest.getUserSeq())).get();
		}else{
			users = usersRepository.findByUserEmail(userRequest.getUserEmail()).get();
		}

		//Update
		users.setUserPw(userRequest.getUserPw() == null ? users.getUserPw() : userRequest.getUserPw());
		users.setUserName(userRequest.getUserName() == null ? users.getUserName() : userRequest.getUserName());
		users.setNationCode(userRequest.getNationCode() == null ? users.getNationCode() : userRequest.getNationCode());
		users.setEnabled(userRequest.getEnabled() == null ? users.getEnabled() : userRequest.getEnabled());
		users.setIsDrop(userRequest.getIsDrop() == null ? users.getIsDrop() : userRequest.getIsDrop());
		users.setUserNum(userRequest.getUserNum() == null ? users.getUserNum() : userRequest.getUserNum());
		users.setInstitude(userRequest.getInstitude() == null ? users.getInstitude() : userRequest.getInstitude());
		users.setDropDttm(userRequest.getDropDttm());
		users.setUpdDttm(LocalDateTime.now());
	}

	@Override
	public StringBuffer downloadUserList() {
		List<Users> userList = usersRepository.findAll();
		String dataColumn [] = {"num","이름","국가코드","이메일","휴대폰번호","기관","활성화여부","가입일","수정일","탈퇴일","탈퇴여부"};
		StringBuffer csvFile = new StringBuffer();
		for (String csvColumn : dataColumn){
			csvFile.append(csvColumn);
			csvFile.append(',');
		}
		csvFile.append('\n');
		for (int i = 0; i< userList.size(); i++){
			csvFile.append("\t"+(i+1));
			csvFile.append(',');
			csvFile.append("\t"+userList.get(i).getUserName().replace(",", "/"));
			csvFile.append(',');
			csvFile.append(userList.get(i).getNationCode());
			csvFile.append(',');
			csvFile.append(userList.get(i).getUserEmail());
			csvFile.append(',');
			csvFile.append("\t"+userList.get(i).getUserNum());
			csvFile.append(',');
			csvFile.append("\t"+userList.get(i).getInstitude().replace(",", "/"));
			csvFile.append(',');
			csvFile.append(userList.get(i).getEnabled());
			csvFile.append(',');
			csvFile.append("\t"+userList.get(i).getRegDttm());
			csvFile.append(',');
			csvFile.append("\t"+userList.get(i).getUpdDttm());
			csvFile.append(',');
			csvFile.append("\t"+userList.get(i).getDropDttm());
			csvFile.append(',');
			csvFile.append(userList.get(i).getIsDrop());
			csvFile.append('\n');
		}
		return csvFile;
	}

	@Override
	public List<Users> searchUser(UserRequest userRequest) {
		List<Users> usersList = usersRepository.findAll();
		return usersList.stream()
				.filter(u -> u.getUserEmail().contains(userRequest.getUserEmail()))
				.filter(u -> u.getUserName().contains(userRequest.getUserName()))
				.collect(Collectors.toList());
	}

	@Override
	public void activationUser(UserRequest userRequest) {
		//Users 검색
		Users users = usersRepository.findByUserEmail(userRequest.getUserEmail()).get();
		users.setEnabled(Constants.YES);
		users.setUpdDttm(LocalDateTime.now());
	}

	@Override
	public void resetPassword(UserRequest userRequest) throws NoSuchAlgorithmException {
		Users users = usersRepository.findByUserEmail(userRequest.getUserEmail()).get();
		log.info("userRequest.getNewPw() :: " + userRequest.getUserPw());
		users.setUserPw(EncryptUtil.sha512(userRequest.getUserPw()));
		users.setUpdDttm(LocalDateTime.now());
	}

	@Override
	public void secessionUser(UserRequest userRequest) {
		Users users = usersRepository.findByUserEmail(userRequest.getUserEmail()).get();

		if(users.getIsDrop().equals(Constants.YES)){
			users.setIsDrop(Constants.NO);
		}else{
			users.setIsDrop(Constants.YES);
		}
		users.setDropDttm(LocalDateTime.now());
	}

	@Override
	public void deleteUser(UserRequest userRequest) {
		Users users = usersRepository.findByUserEmail(userRequest.getUserEmail()).get();
		usersRepository.deleteById(users.getUserSeq());
	}
}
