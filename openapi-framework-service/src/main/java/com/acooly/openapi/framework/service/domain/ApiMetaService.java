/*
 * acooly.cn Inc.
 * Copyright (c) 2017 All Rights Reserved.
 * create by shuijing
 * date:2017-11-27
 */
package com.acooly.openapi.framework.service.domain;

import com.acooly.core.common.domain.AbstractEntity;
import com.acooly.core.utils.Strings;
import com.acooly.openapi.framework.common.enums.ApiBusiType;
import com.acooly.openapi.framework.common.enums.ResponseType;
import com.google.common.base.MoreObjects;
import lombok.Getter;
import lombok.Setter;
import javax.validation.constraints.NotEmpty;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;
import javax.validation.constraints.Size;

/**
 * ApiService meta
 *
 * @author qiubo
 */
@Getter
@Setter
@Entity
@Table(name = "api_meta_service")
public class ApiMetaService extends AbstractEntity {

    /**
     * 服务名称
     */
    @NotEmpty
    @Size(max = 64)
    private String serviceName;

    /**
     * 版本号
     */
    @Size(max = 32)
    private String version;

    /**
     * 服务描述
     */
    @Size(max = 128)
    private String serviceDesc;

    /**
     * 服务响应类型
     */
    @Size(max = 16)
    @Enumerated(EnumType.STRING)
    private ResponseType responseType;

    /**
     * 所属系统
     */
    @Size(max = 32)
    private String owner;

    /**
     * 服务名称
     */
    @Size(max = 16)
    @Enumerated(EnumType.STRING)
    private ApiBusiType busiType;

    /**
     * 服务介绍
     */
    @Size(max = 255)
    private String note;

    /**
     * 服务类
     */
    @Size(max = 128)
    private String serviceClass;

    /**
     * 请求类
     */
    @Size(max = 128)
    private String requestClass;

    /**
     * 同步响应类
     */
    @Size(max = 128)
    private String responseClass;

    /**
     * 异步响应类
     */
    @Size(max = 128)
    private String notifyClass;

    public String getServiceNo() {
        return getServiceName() + "_" + getVersion();
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
            ApiMetaService that = (ApiMetaService) o;
            return Strings.equals(that.getServiceNo(), this.getServiceNo());
        }
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper("")
                .add("serviceName", serviceName)
                .add("version", version)
                .add("serviceDesc", serviceDesc)
                .add("responseType", responseType)
                .toString();
    }
}
