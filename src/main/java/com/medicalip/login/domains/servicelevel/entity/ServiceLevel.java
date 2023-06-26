package com.medicalip.login.domains.servicelevel.entity;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Getter
@Setter
@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "TB_INFO_SERVICE")
public class ServiceLevel {
    @Id
    @Column(name = "SERVICE_ID")
    private String serviceId;

    @Column(name = "SERVICE_NAME")
    private String serviceName;

}
