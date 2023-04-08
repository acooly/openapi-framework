/*
 * acooly.com Inc.
 * Copyright (c) 2014 All Rights Reserved
 */

/*
 * 修订记录:
 * qiubo@qq.com 2017-07-27 18:11 创建
 *
 */
package com.acooly.openapi.framework.core.auth.permission;

import static com.acooly.openapi.framework.common.ApiConstants.PERM_PATTERN;

/**
 * @author qiubo@qq.com
 */
public interface Permission {

    /**
     * 判断设置的权限是否串是否合法
     *
     * @param perm
     */
    static void permMatch(String perm) {
        if (!PERM_PATTERN.matcher(perm).matches()) {
            throw new IllegalStateException("权限字符串的格式应该为partnerId:serviceName,可以支持*:，(例如：*:*,代表拥有所有权限; 配置123:*,代表可以访问partnerId=123的所有服务; 配置123:xxx*,代表可以访问partnerId=123,xxx开头的服务。perm：" + perm);
        }
//    Assert.state(PERM_PATTERN.matcher(perm).matches(), "权限字符串的格式应该为partnerId:serviceName,可以支持*:，(例如：*:*,代表拥有所有权限; 配置123:*,代表可以访问partnerId=123的所有服务; 配置123:xxx*,代表可以访问partnerId=123,xxx开头的服务");
    }

    /**
     * 判断资源是否有权限.
     *
     * @param resource
     * @return
     */
    boolean implies(String resource);

}
