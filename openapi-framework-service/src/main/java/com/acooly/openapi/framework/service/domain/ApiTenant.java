/*
 * acooly.cn Inc.
 * Copyright (c) 2018 All Rights Reserved.
 * create by qiubo
 * date:2018-08-21
 */
package com.acooly.openapi.framework.service.domain;


import com.acooly.core.common.domain.AbstractEntity;
import com.acooly.openapi.framework.common.enums.SecretType;
import com.acooly.openapi.framework.common.enums.SignType;
import lombok.Getter;
import lombok.Setter;
import javax.validation.constraints.NotBlank;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * 租户管理 Entity
 *
 * @author qiubo
 * Date: 2018-08-21 14:31:05
 */
@Entity
@Table(name = "api_tenant")
@Getter
@Setter
public class ApiTenant extends AbstractEntity {

    /**
     * 合作方编码
     */
    @NotBlank
    @Size(max = 32)
    private String partnerId;

    /**
     * 合作方名称
     */
    @NotBlank
    @Size(max = 32)
    private String partnerName;

    /**
     * 安全方案
     */
    @Enumerated(EnumType.STRING)
    @NotNull
    private SecretType secretType;

    /**
     * 签名类型
     */
    @Enumerated(EnumType.STRING)
    @NotNull
    private SignType signType;

    /**
     * 备注
     */
    @Size(max = 128)
    private String comments;

}
