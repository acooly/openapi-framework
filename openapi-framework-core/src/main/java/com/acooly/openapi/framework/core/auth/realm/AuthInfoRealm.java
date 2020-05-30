/*
 * acooly.com Inc.
 * Copyright (c) 2014 All Rights Reserved
 */
package com.acooly.openapi.framework.core.auth.realm;

import java.util.Set;

/**
 * @author zhangpu
 * @date 2014年6月27日
 */
public interface AuthInfoRealm {
    String AUTHC_CACHE_KEY_PREFIX = "api_authc:";
    String AUTHZ_CACHE_KEY_PREFIX = "api_authz:";
    String AUTHIP_CACHE_KEY_PREFIX = "api_authip:";

    /**
     * 获取认证信息
     *
     * @param accessKey
     * @return
     */
    Object getAuthenticationInfo(String accessKey);

    /**
     * 获取授权信息
     *
     * @param accessKey
     * @return
     */
    Object getAuthorizationInfo(String accessKey);

    /**
     * 获取IP白名单
     *
     * @param accessKey
     * @return
     */
    Set<String> getIpWhitelist(String accessKey);
}
