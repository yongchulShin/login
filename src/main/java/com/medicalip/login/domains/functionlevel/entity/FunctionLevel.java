package com.medicalip.login.domains.functionlevel.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "TB_INFO_FUNCTION_LEVEL")
public class FunctionLevel {
    @Id
    @Column(name = "FUNCTION_LEVEL_ID")
    private String functionLevelId;

    @Column(name = "FUNCTION_LEVEL_NAME")
    private String functionLevelName;

    @Column(name = "FUNCTION_LEVEL_TYPE")
    private String functionLevelType;

    @Column(name = "COMMENT")
    private String comment;
}
