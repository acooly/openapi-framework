/*
* acooly.cn Inc.
* Copyright (c) 2019 All Rights Reserved.
* create by zhike
* date:2019-02-20
*/
package com.acooly.openapi.apidoc.persist.entity;


import com.acooly.core.common.domain.AbstractEntity;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.Size;

/**
 * 服务方案描述 Entity
 *
 * @author zhike
 * Date: 2019-02-20 15:39:16
 */
@Entity
@Table(name = "api_doc_scheme_desc")
@Getter
@Setter
public class ApiDocSchemeDesc extends AbstractEntity {

	/** 方案编码 */
	@NotEmpty
	@Size(max=64)
    private String schemeNo = "";

	/** 方案描述 */
    private String schemeDesc;

}
