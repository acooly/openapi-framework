/*
 * acooly.cn Inc.
 * Copyright (c) 2018 All Rights Reserved.
 * create by qiubo
 * date:2018-08-21
 */
package com.acooly.openapi.framework.service.domain;


import com.acooly.core.common.domain.AbstractEntity;
import com.acooly.core.utils.Strings;
import com.google.common.collect.Lists;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.List;

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

    /**
     * 合作方编码
     */
    @NotBlank
    @Size(max = 32)
    private String partnerId;

    /**
     * 访问帐号
     */
    @NotBlank
    @Size(max = 45)
    private String accessKey;

    /**
     * 访问秘钥
     */
    @NotBlank
    @Size(max = 45)
    private String secretKey;

    /**
     * 访问权限
     */
    @Size(max = 512)
    private String permissions;

    /**
     * 备注
     */
    @Size(max = 128)
    private String comments;


    @Transient
    public List<String> getAllowServices() {
        List<String> services = Lists.newArrayList();
        String[] acls = Strings.split(this.permissions, ",");
        if (acls == null || acls.length == 0) {
            return services;
        }
        String service = null;
        for (String acl : acls) {
            service = Strings.split(acl, ":")[1];
            if (!Strings.contains(service, "*") && !Strings.contains(service, "+")) {
                services.add(service);
            }
        }
        return services;
    }
}
