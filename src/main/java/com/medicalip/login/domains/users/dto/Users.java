package com.medicalip.login.domains.users.dto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicUpdate;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@RequiredArgsConstructor
@AllArgsConstructor // 인자를 모두 갖춘 생성자를 자동으로 생성한다.
@Builder
@Entity
@Setter
@Getter
@DynamicUpdate
@Table(name = "TB_INFO_USER")
public class Users {
	@Id //primaryKey 임을 알린다.
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "USER_SEQ")
    // primaryKey 생성 전략을 DB에 위임한다는 뜻.
    // primaryKey 필드를 auto_increment로 설정해 놓은 경우와 같다.
    private long userSeq;
	
	@Column(name = "NATION_CODE")
	private String nationCode;
	
    // email을 명시한다. 필수 입력, 중복 안됨, 길이는 100제한
	@Column(name = "USER_EMAIL")
    private String userEmail;
	
	@Column(name = "USER_PW")
    private String userPw;
	
	@Column(name = "USER_NAME")
    private String userName; // name을 명시, 필수 입력, 길이 100 제한
	
	@Column(name = "USER_NUM")
	private String userNum; 
	
	@Column(name = "INSTITUDE")
	private String institude;
	
	@Column(name = "ENABLED")
	private String enabled;
	
	@Column(name = "IS_DROP")
	private String isDrop;
	
	@Column(name = "MAC_ADDRESS")
	private String macAddress;
	
	@Column(name = "IS_SUBSCRIBE")
	private String isSubscribe;
	
	@Column(name = "SUBSCRIBE_EMAIL")
	private String subscribeEmail;
	
	@ManyToOne(fetch = FetchType.EAGER, targetEntity = UserRole.class)
	@JoinColumn(name = "USER_ROLE_ID")
//    private List<String> roles = new ArrayList<>();
    private UserRole roles;
    
    @Column(name = "UPD_DTTM")
    private LocalDateTime updDttm; //
    
    @Column(name = "REG_DTTM")
    private LocalDateTime regDttm; //
    
	public Collection<? extends GrantedAuthority> getAuthorities() {
		// TODO Auto-generated method stub
		List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
        String auth = roles.getRoleName();
        				
        authorities.add(new SimpleGrantedAuthority(auth));
        return authorities;
	}
}
