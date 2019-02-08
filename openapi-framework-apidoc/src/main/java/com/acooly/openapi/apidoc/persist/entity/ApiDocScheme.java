/*
 * acooly.cn Inc.
 * Copyright (c) 2017 All Rights Reserved.
 * create by acooly
 * date:2017-12-05
 */
package com.acooly.openapi.apidoc.persist.entity;


import com.acooly.core.common.domain.AbstractEntity;
import com.acooly.openapi.apidoc.ApiDocProperties;
import com.acooly.openapi.apidoc.enums.SchemeTypeEnum;
import com.google.common.collect.Lists;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.util.List;

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
    private String author = ApiDocProperties.DEF_SCHEME_AUTHOR;

    /**
     * 说明
     */
    @Size(max = 128)
    private String note;

    /**
     * 方案类型
     */
    @Enumerated(EnumType.STRING)
    private SchemeTypeEnum schemeType = SchemeTypeEnum.auto;

    /**
     * 排序值
     */
    private Long sortTime = System.currentTimeMillis();

    @Transient
    private List<ApiDocSchemeService> apiDocSchemeServices = Lists.newArrayList();

    /**
     * 备注
     */
    @Size(max = 255)
    private String comments;

    public ApiDocScheme() {

    }

    public void append(ApiDocSchemeService apiDocSchemeService) {
        this.apiDocSchemeServices.add(apiDocSchemeService);
    }

    public ApiDocScheme(String schemeNo, String title) {
        this.schemeNo = schemeNo;
        this.title = title;
        this.setSchemeType(SchemeTypeEnum.auto);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        if (!super.equals(o)) {
            return false;
        }

        ApiDocScheme that = (ApiDocScheme) o;

        return schemeNo.equals(that.schemeNo);
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + schemeNo.hashCode();
        return result;
    }
}
