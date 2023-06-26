package com.medicalip.login.domains.newpromotion.entity;

import com.medicalip.login.domains.functionlevel.entity.FunctionLevel;
import com.medicalip.login.domains.servicelevel.entity.ServiceLevel;
import lombok.Data;

import java.io.Serializable;

@Data
public class NewPromotionPK implements Serializable {
     private String serviceId;
     private String functionLevelId;
}
