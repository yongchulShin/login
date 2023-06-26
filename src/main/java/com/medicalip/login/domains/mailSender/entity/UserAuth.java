package com.medicalip.login.domains.mailSender.entity;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.medicalip.login.domains.newpromotion.entity.NewPromotionPK;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "TB_INFO_USER_AUTH")
public class UserAuth {
    @Id
    @Column(name = "USER_EMAIL")
    private String userEmail;

    @Column(name = "AUTH_KEY")
    private String authKey;

    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @Column(name = "REG_DTTM")
    private LocalDateTime regDttm; //
}
