/**
 * openapi-framework
 * <p>
 * Copyright 2014 Acooly.cn, Inc. All rights reserved.
 *
 * @author zhangpu
 * @date 2020-02-01 01:41
 */
package com.acooly.openapi.framework.service.domain;

import com.acooly.core.common.domain.AbstractEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * 认证对应的访问权限（ACL）
 *
 * @author zhangpu
 * @date 2020-02-01 01:41
 */
@Entity
@Table(name = "api_auth_acl")
@Getter
@Setter
public class ApiAuthAcl extends AbstractEntity {

    /**
     * 认证编码
     */
    @NotBlank
    @Size(max = 32)
    private String authNo;

    /**
     * 访问码
     */
    private String accessKey;

    /**
     * 服务编码
     */
    @NotBlank
    private String serviceNo;
    /**
     * 服务名
     */
    @NotBlank
    private String name;
    /**
     * 服务版本
     */
    @NotBlank
    private String version;
    /**
     * 中文描述
     */
    private String title;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        } else {
            ApiAuthAcl that = (ApiAuthAcl) o;
            return this.serviceNo != null ? this.serviceNo.equals(that.serviceNo) : that.serviceNo == null;
        }
    }

    @Override
    public int hashCode() {
        return this.serviceNo != null ? this.serviceNo.hashCode() : 0;
    }

}
