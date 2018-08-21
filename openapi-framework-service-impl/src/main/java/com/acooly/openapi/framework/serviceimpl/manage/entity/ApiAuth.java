/*
* acooly.cn Inc.
* Copyright (c) 2018 All Rights Reserved.
* create by qiubo
* date:2018-08-21
*/
package com.acooly.openapi.framework.serviceimpl.manage.entity;


import com.acooly.core.common.domain.AbstractEntity;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.Size;

/**
 * 认证授权信息管理 Entity
 *
 * @author qiubo
 * Date: 2018-08-21 14:31:06
 */
@Entity
@Table(name = "api_auth")
@Getter
@Setter
public class ApiAuth extends AbstractEntity {

	/** 访问帐号 */
	@NotEmpty
	@Size(max=45)
    private String accessKey;

	/** 访问秘钥 */
	@NotEmpty
	@Size(max=45)
    private String secretKey;

	/** 访问权限 */
	@Size(max=512)
    private String permissions;

	/** 备注 */
	@Size(max=128)
    private String comments;

}
