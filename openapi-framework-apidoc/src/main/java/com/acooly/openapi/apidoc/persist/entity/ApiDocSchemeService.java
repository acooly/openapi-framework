/*
* acooly.cn Inc.
* Copyright (c) 2017 All Rights Reserved.
* create by acooly
* date:2017-12-05
*/
package com.acooly.openapi.apidoc.persist.entity;


import com.acooly.core.common.domain.AbstractEntity;
import com.acooly.core.utils.Strings;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.Size;

/**
 * 方案服务列表 Entity
 *
 * @author acooly
 * Date: 2017-12-05 12:34:39
 */
@Getter
@Setter
@Entity
@Table(name = "api_doc_scheme_service")
public class ApiDocSchemeService extends AbstractEntity {
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
     * 服务编码
     */
    @NotEmpty
    @Size(max = 64)
    private String serviceNo;

    /**
     * 排序值
     */
    private Long sortTime = System.currentTimeMillis();


    /**
     * 备注
     */
    @Size(max = 255)
    private String comments;


    public ApiDocSchemeService() {
    }

    public ApiDocSchemeService(String schemeNo, String serviceNo) {
        this.schemeNo = schemeNo;
        this.serviceNo = serviceNo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        } else if (!(o instanceof AbstractEntity)) {
            return false;
        } else {
            ApiDocSchemeService that = (ApiDocSchemeService) o;
            return Strings.equals(that.getServiceNo(), this.getServiceNo()) && Strings.equals(that.getSchemeNo(), this.getSchemeNo());
        }
    }
}
