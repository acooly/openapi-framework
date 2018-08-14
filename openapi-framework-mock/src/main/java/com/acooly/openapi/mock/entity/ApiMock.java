/*
* acooly.cn Inc.
* Copyright (c) 2018 All Rights Reserved.
* create by shuijing
* date:2018-08-14
*/
package com.acooly.openapi.mock.entity;


import com.acooly.core.common.domain.AbstractEntity;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.Size;

/**
 * 服务类 Entity
 *
 * @author shuijing
 * Date: 2018-08-14 17:35:43
 */
@Entity
@Table(name = "api_mock")
@Getter
@Setter
public class ApiMock extends AbstractEntity {

	/** 服务名称 */
	@NotEmpty
	@Size(max=128)
    private String serviceName;

	/** 版本号 */
	@Size(max=32)
    private String version;

	/** 期望参数 */
	@Size(max=256)
    private String expect;

	/** 同步响应 */
	@Size(max=256)
    private String response;

	/** 异步响应 */
	@Size(max=256)
    private String notify;

}
