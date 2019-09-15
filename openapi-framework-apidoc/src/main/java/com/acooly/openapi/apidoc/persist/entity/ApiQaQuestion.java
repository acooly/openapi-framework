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
import javax.validation.constraints.NotEmpty;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * 问题记录表 Entity
 *
 * @author zhike
 * Date: 2019-02-20 17:10:00
 */
@Entity
@Table(name = "api_qa_question")
@Getter
@Setter
public class ApiQaQuestion extends AbstractEntity {

	/** 问题 */
	@Size(max=255)
    private String problem;

	/** 解决方案 */
	@NotEmpty
    private String solution;

	/** 权重 */
    private Integer weight = 0;

	/** 类ID */
    private Long classifyId;

	@Transient
	private ApiQaClassify apiQaClassify;

}
