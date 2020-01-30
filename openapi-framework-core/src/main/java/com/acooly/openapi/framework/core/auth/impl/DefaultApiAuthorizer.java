/*
 * acooly.cn Inc.
 * Copyright (c) 2016 All Rights Reserved.
 * create by zhangpu
 * date:2016年3月17日
 */
package com.acooly.openapi.framework.core.auth.impl;

import com.acooly.openapi.framework.common.context.ApiContext;
import com.acooly.openapi.framework.core.auth.ApiAuthorizer;
import com.acooly.openapi.framework.core.auth.permission.Permission;
import com.acooly.openapi.framework.core.exception.impl.ApiServiceAuthorizationException;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 默认认证器实现
 *
 * @author qiubo@qq.com
 * @author zhangpu
 */
@Component
public class DefaultApiAuthorizer implements ApiAuthorizer {

    @Override
    public void authorize(ApiContext apiContext, List<Permission> permissionList) {
        String resource = apiContext.getCanonicalAccessKey() + ":" + apiContext.getServiceName();
        if (!hasPermission(permissionList, resource)) {
            throw new ApiServiceAuthorizationException("服务[" + apiContext.getServiceName() + "]未授权");
        }
    }


    protected boolean hasPermission(List<Permission> permissionList, String resource) {
        if (permissionList != null) {
            for (Permission perm : permissionList) {
                if (perm.implies(resource)) {
                    return true;
                }
            }
        }
        return false;
    }
}
