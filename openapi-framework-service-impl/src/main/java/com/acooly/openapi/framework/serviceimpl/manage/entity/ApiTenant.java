/*
* acooly.cn Inc.
* Copyright (c) 2018 All Rights Reserved.
* create by qiubo
* date:2018-08-21
*/
package com.acooly.openapi.framework.serviceimpl.manage.entity;


import com.acooly.core.common.domain.AbstractEntity;
import com.acooly.openapi.framework.serviceimpl.manage.enums.SecretTypeEnum;
import com.acooly.openapi.framework.serviceimpl.manage.enums.SignTypeEnum;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.NotEmpty;

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

	/** 合作方编码 */
	@NotEmpty
	@Size(max=32)
    private String partnerId;

	/** 合作方名称 */
	@NotEmpty
	@Size(max=32)
    private String partnerName;

	/** 安全方案 */
    @Enumerated(EnumType.STRING)
	@NotNull
    private SecretTypeEnum secretType;

	/** 签名类型 */
    @Enumerated(EnumType.STRING)
	@NotNull
    private SignTypeEnum signType;

	/** 备注 */
	@Size(max=128)
    private String comments;

}
