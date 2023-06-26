package com.medicalip.login.domains.match.entity;

import com.medicalip.login.domains.match.entity.pk.MatchUserCreditPK;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "TB_MATCH_USER_CREDIT")
@IdClass(MatchUserCreditPK.class)
public class MatchUserCredit {
    @Id
    @Column(name = "USER_SEQ")
    private long userSeq;

    @Id
    @Column(name = "SERVICE_ID")
    private String serviceId;

    @Id
    @Column(name = "FUNCTION_LEVEL_ID")
    private String functionLevelId;

    @Column(name = "USABLE_COUNT")
    private int usableCount;

    @Column(name = "USED_COUNT")
    private int usedCount;

    @Column(name = "ENABLED")
    private String enabled;

    @Column(name = "REG_DTTM")
    private LocalDateTime regDttm;

    @Column(name = "UPD_DTTM")
    private LocalDateTime updDttm;
}
