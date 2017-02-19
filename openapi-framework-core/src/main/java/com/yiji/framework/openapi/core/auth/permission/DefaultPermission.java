/*
 * www.yiji.com Inc.
 * Copyright (c) 2014 All Rights Reserved
 */

/*
 * 修订记录:
 * qzhanbo@yiji.com 2014-07-27 18:17 创建
 *
 */
package com.yiji.framework.openapi.core.auth.permission;

import com.yiji.framework.openapi.core.OpenApiConstants;

/**
 * 匹配一个星号的授权.<p/>
 * 比如:商户系统可以配置*,代表此商户拥有所有权限.<p/>
 * 配置xxx*,代表仅开通xxx开头的服务<p/>
 * 预期openapi以后会有服务分级的概念.<p/>
 *
 * @author qzhanbo@yiji.com
 */
public class DefaultPermission implements Permission {

    /**
     * 权限字符串
     */
    private String perm;

    public DefaultPermission(String perm) {
        this.perm = perm;
    }

    @Override
    public boolean implies(String resource) {
        if (perm.equals(resource) || perm.equals(OpenApiConstants.WILDCARD_TOKEN)) {
            return true;
        }
        int idx = perm.indexOf(OpenApiConstants.WILDCARD_TOKEN);
        if (idx < 0) {
            return false;
        } else {
            String pp = perm.substring(0, idx);
            String pe = perm.substring(idx + 1);
            return resource.startsWith(pp) && resource.endsWith(pe);
        }
    }
}
