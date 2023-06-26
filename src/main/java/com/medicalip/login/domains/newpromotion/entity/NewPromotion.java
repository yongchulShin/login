package com.medicalip.login.domains.newpromotion.entity;

import com.medicalip.login.domains.functionlevel.entity.FunctionLevel;
import com.medicalip.login.domains.servicelevel.entity.ServiceLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "TB_INFO_NEWUSER_PROMOTIONS")
@IdClass(NewPromotionPK.class)
public class NewPromotion {
    @Id
    @Column(name = "SERVICE_ID")
    private String serviceId;

    @Id
    @Column(name = "FUNCTION_LEVEL_ID")
    private String functionLevelId;

    @Column(name = "FUNCTION_LEVEL_TYPE")
    private String functionLevelType;

    @Column(name = "PROMOTION_PERIOD_DAY")
    private int promotionPeriodDay;

    @Column(name = "PROMOTION_PERIOD_DAY_FOR_TRAINEE")
    private int promotionPeriodDayForTrainee;

    @Column(name = "SELLING_METHOD")
    private String sellingMethod;

    @Column(name = "COUNT")
    private int count;

    @Column(name = "ENABLED")
    private String enabled;

    @Column(name = "LICENSE_TYPE")
    private String licenseType;
}
