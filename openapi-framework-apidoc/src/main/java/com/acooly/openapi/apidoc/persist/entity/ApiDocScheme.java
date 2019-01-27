/*
 * acooly.cn Inc.
 * Copyright (c) 2017 All Rights Reserved.
 * create by acooly
 * date:2017-12-05
 */
package com.acooly.openapi.apidoc.persist.entity;


import com.acooly.core.common.domain.AbstractEntity;
import com.acooly.openapi.apidoc.enums.SchemeTypeEnum;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;
import javax.validation.constraints.Size;

/**
 * 服务方案 Entity
 *
 * @author acooly
 * Date: 2017-12-05 12:34:38
 */
@Getter
@Setter
@Entity
@Table(name = "api_doc_scheme")
public class ApiDocScheme extends AbstractEntity {
    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = 1L;


    /**
     * 方案编码
     */
    @NotEmpty
    @Size(max = 64)
    private String schemeNo;

    /**
     * 标题
     */
    @NotEmpty
    @Size(max = 64)
    private String title;

    /**
     * 作者
     */
    @Size(max = 64)
    private String author;

    /**
     * 说明
     */
    @Size(max = 128)
    private String note;

    /**
     * 方案类型
     */
    @Enumerated(EnumType.STRING)
    private SchemeTypeEnum schemeType = SchemeTypeEnum.common;

    /**
     * 排序值
     */
    private Long sortTime = System.currentTimeMillis();


    /**
     * 备注
     */
    @Size(max = 255)
    private String comments;

    public ApiDocScheme() {

    }

    public ApiDocScheme(String schemeNo, String title) {
        this.schemeNo = schemeNo;
        this.title = title;
    }
}
