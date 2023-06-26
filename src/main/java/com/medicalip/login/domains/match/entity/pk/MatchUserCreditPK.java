package com.medicalip.login.domains.match.entity.pk;

import com.medicalip.login.domains.functionlevel.entity.FunctionLevel;
import com.medicalip.login.domains.servicelevel.entity.ServiceLevel;
import com.medicalip.login.domains.users.entity.Users;
import lombok.Data;

import java.io.Serializable;

@Data
public class MatchUserCreditPK implements Serializable {
     private long userSeq;
     private String serviceId;
     private String functionLevelId;
}
