/*
* acooly.cn Inc.
* Copyright (c) 2017 All Rights Reserved.
* create by acooly
* date:2017-12-05
*/
package com.acooly.openapi.apidoc.persist.entity;


import com.acooly.core.common.domain.AbstractEntity;
import com.acooly.core.utils.Strings;
import com.acooly.openapi.framework.common.ApiConstants;
import com.acooly.openapi.framework.common.enums.ApiBusiType;
import com.acooly.openapi.framework.common.enums.ResponseType;
import com.google.common.collect.Lists;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.NotEmpty;

import javax.annotation.Signed;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

/**
 * 服务 Entity
 *
 * @author acooly
 * Date: 2017-12-05 12:34:39
 */
@Getter
@Setter
@Entity
@Table(name = "api_doc_service")
public class ApiDocService extends AbstractEntity {
    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = 1L;


    /**
     * 服务编号
     */
    @Signed
    @NotEmpty
    @Size(max = 255)
    private String serviceNo;

    /**
     * 服务名称
     */
    @NotEmpty
    @Size(max = 128)
    private String name;

    /**
     * 服务版本
     */
    @Size(max = 32)
    private String version = ApiConstants.VERSION_DEFAULT;

    /**
     * 服务标题
     */
    @Signed
    @NotEmpty
    @Size(max = 64)
    private String title;

    /**
     * 所属系统
     */
    @Size(max = 64)
    private String owner;

    /**
     * 服务说明
     */
    @Signed
    @Size(max = 512)
    private String note;

    /**
     * 手工说明
     */
    @Size(max = 512)
    private String manualNote;

    /**
     * 服务类型
     */
    @Signed
    @Enumerated(EnumType.STRING)
    @NotNull
    private ResponseType serviceType = ResponseType.SYN;

    /**
     * 业务类型
     */
    @Signed
    @Enumerated(EnumType.STRING)
    private ApiBusiType busiType;

    /**
     * 排序值
     */
    private Long sortTime = System.currentTimeMillis();

    /**
     * 备注
     */
    @Size(max = 255)
    private String comments;


    /**
     * 签名
     */
    @Size(max = 128)
    private String signature;

    @Transient
    private List<ApiDocMessage> apiDocMessages = Lists.newArrayList();


    public ApiDocService() {
    }

    public ApiDocService(String serviceNo) {
        this.serviceNo = serviceNo;
    }

    @Override
    public int hashCode() {
        if (Strings.isNoneBlank(getServiceNo())) {
            return getServiceNo().hashCode();
        }
        return super.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        } else if (!(o instanceof AbstractEntity)) {
            return false;
        } else {
            ApiDocService that = (ApiDocService) o;
            return Strings.equals(that.getServiceNo(), this.getServiceNo());
        }
    }
}
