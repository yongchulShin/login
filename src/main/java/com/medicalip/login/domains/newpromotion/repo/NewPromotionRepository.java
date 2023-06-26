package com.medicalip.login.domains.newpromotion.repo;

import com.medicalip.login.domains.functionlevel.entity.FunctionLevel;
import com.medicalip.login.domains.newpromotion.entity.NewPromotion;
import com.medicalip.login.domains.newpromotion.entity.NewPromotionPK;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NewPromotionRepository extends JpaRepository<NewPromotion, NewPromotionPK>{
}
