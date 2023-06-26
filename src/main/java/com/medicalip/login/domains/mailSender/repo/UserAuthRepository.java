package com.medicalip.login.domains.mailSender.repo;

import com.medicalip.login.domains.mailSender.entity.UserAuth;
import com.medicalip.login.domains.newpromotion.entity.NewPromotion;
import com.medicalip.login.domains.newpromotion.entity.NewPromotionPK;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserAuthRepository extends JpaRepository<UserAuth, String>{
    UserAuth findByUserEmail(String userEmail);
}
