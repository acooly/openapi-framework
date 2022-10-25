/*
 * acooly.cn Inc.
 * Copyright (c) 2018 All Rights Reserved.
 * create by qiubo
 * date:2018-08-21
 */
package com.acooly.openapi.framework.service.domain;


import com.acooly.core.common.domain.AbstractEntity;
import com.acooly.core.utils.Strings;
import com.acooly.core.utils.arithmetic.tree.TreeNode;
import com.acooly.core.utils.enums.SimpleStatus;
import com.acooly.core.utils.enums.WhetherStatus;
import com.acooly.openapi.framework.common.enums.SecretType;
import com.acooly.openapi.framework.common.enums.SignType;
import com.google.common.collect.Lists;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
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
public class ApiAuth extends AbstractEntity implements TreeNode<ApiAuth> {

    /**
     * 父节点
     */
    private Long parentId;

    /**
     * 认证编码
     */
    @NotBlank
    @Size(max = 32)
    private String authNo;

    /**
     * 合作方编码
     */
    @NotBlank
    @Size(max = 32)
    private String partnerId;

    /**
     * 安全方案
     */
    @Enumerated(EnumType.STRING)
    @NotNull
    private SecretType secretType;

    /**
     * 签名类型
     */
    @Enumerated(EnumType.STRING)
    @NotNull
    private SignType signType;

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
     * 白名单检查开关
     */
    private WhetherStatus whitelistCheck;

    /**
     * 白名单
     */
    private String whitelist;

    /**
     * 备注
     */
    @Size(max = 128)
    private String comments;

    /**
     * 状态
     */
    @Enumerated(EnumType.STRING)
    private SimpleStatus status;

    @Transient
    private List<ApiAuth> children;


    /**
     * 视图状态（非持久化）
     */
    @Transient
    public String getState() {
        return Strings.contains(accessKey, "#") ? "open" : "closed";
    }

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
