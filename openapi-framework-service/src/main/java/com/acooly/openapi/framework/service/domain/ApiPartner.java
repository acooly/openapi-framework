/*
 * acooly.cn Inc.
 * Copyright (c) 2018 All Rights Reserved.
 * create by qiubo
 * date:2018-08-21
 */
package com.acooly.openapi.framework.service.domain;


import com.acooly.core.common.domain.AbstractEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * 租户管理 Entity
 * <p>
 * 关系：Tenant -(n)-> Merchant -(n)-> Partner
 *
 * @author zhangpu
 * Date: 2018-08-21 14:31:05
 */
@Entity
@Table(name = "api_partner")
@Getter
@Setter
public class ApiPartner extends AbstractEntity {

    /**
     * 合作方编码
     */
    @NotBlank
    @Size(max = 32)
    private String partnerId;

    /**
     * 商户会员号
     */
    @NotBlank
    @Size(max = 64)
    private String merchantNo;

    /**
     * 合作方名称
     */
    @NotBlank
    @Size(max = 32)
    private String partnerName;

    /**
     * 租户编码
     * 预留用于支持多租户
     */
    @Size(max = 64)
    private String tenantId;

    /**
     * 租户名称
     * 预留用于支持多租户
     */
    @Size(max = 64)
    private String tenantName;

    /**
     * 备注
     */
    @Size(max = 128)
    private String comments;

}
