package com.medicalip.login.domains.match.entity;

import com.medicalip.login.domains.match.entity.pk.MatchUserProductPK;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "TB_MATCH_USER_PRODUCT")
@IdClass(MatchUserProductPK.class)
public class MatchUserProduct {
    @Id
    @Column(name = "USER_SEQ")
    private long userSeq;

    @Id
    @Column(name = "SERVICE_ID")
    private String serviceId;

    @Column(name = "FUNCTION_LEVEL_ID")
    private String functionLevelId;

    @Column(name = "LICENSE_TYPE")
    private String licenseType;

    @Column(name = "FUNCTION_START_DTTM")
    private LocalDateTime functionStartDttm;

    @Column(name = "FUNCTION_END_DTTM")
    private LocalDateTime functionEndDttm;

    @Column(name = "IS_UPDATE")
    private String isUpdate;

    @Column(name = "UPDATE_SERVICE_LIMIT_VERSION")
    private String updateServiceLimitVersion;

}
