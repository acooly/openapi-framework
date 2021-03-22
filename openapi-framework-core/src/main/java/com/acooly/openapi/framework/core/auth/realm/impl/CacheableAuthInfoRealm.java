/*
 * acooly.cn Inc.
 * Copyright (c) 2016 All Rights Reserved.
 * create by zhangpu
 * date:2016年3月17日
 */
package com.acooly.openapi.framework.core.auth.realm.impl;

import com.acooly.core.common.boot.Apps;
import com.acooly.core.utils.Collections3;
import com.acooly.openapi.framework.common.cache.OpenApiCacheManager;
import com.acooly.openapi.framework.common.enums.ApiServiceResultCode;
import com.acooly.openapi.framework.common.exception.ApiServiceException;
import com.acooly.openapi.framework.core.OpenAPIProperties;
import com.acooly.openapi.framework.core.auth.permission.Permission;
import com.acooly.openapi.framework.core.auth.permission.PermissionResolver;
import com.acooly.openapi.framework.core.auth.realm.AuthInfoRealm;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Resource;
import java.util.List;
import java.util.Set;

/**
 * 缓存实现
 *
 * @author Bohr.Qiu <qiubo@qq.com>
 * @author zhangpu on 2020-02-11 for 缓存key支持多个OpenApi公用一个分布式缓存(redis)不冲突
 */
public abstract class CacheableAuthInfoRealm implements AuthInfoRealm {
    protected Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private OpenApiCacheManager openApiCacheManager;

    @Resource
    PermissionResolver permissionResolver;

    @Autowired
    private OpenAPIProperties openAPIProperties;

    @Override
    public Object getAuthenticationInfo(String accessKey) {
        String key = authenticationKey(accessKey);
        Object value = openApiCacheManager.get(key);
        if (value == null) {
            value = getSecretKey(accessKey);
            if (value != null) {
                openApiCacheManager.add(key, value);
            } else {
                throw new ApiServiceException(ApiServiceResultCode.ACCESS_KEY_NOT_EXIST, "获取认证信息失败或不存在");
            }
        }
        return value;
    }

    @SuppressWarnings("unchecked")
    @Override
    public Object getAuthorizationInfo(String accessKey) {
        String key = authorizationKey(accessKey);
        List<Permission> value = (List<Permission>) openApiCacheManager.get(key);
        if (value == null) {
            Set<String> permStrList = getAuthorizedServices(accessKey);
            // 如果没有查询到权限信息,不设置缓存,有可能是网络或者权限系统内部错误
            if (permStrList == null || permStrList.isEmpty()) {
                return null;
            }

            List<Permission> perms = Lists.newArrayList();
            for (String permStr : permStrList) {
                if (Strings.isNullOrEmpty(permStr)) {
                    continue;
                }

                // 判断是否支持script配置
                boolean isScriptPermi = com.acooly.core.utils.Strings.contains(permStr, "*");
                if (isScriptPermi && !openAPIProperties.getPermi().getScriptEnable()) {
                    continue;
                }

                perms.add(permissionResolver.resolvePermission(permStr));
            }
            value = perms;
            openApiCacheManager.add(key, value);
        }
        return value;
    }

    @Override
    public Set<String> getIpWhitelist(String accessKey) {
        String key = authIpKey(accessKey);
        Set<String> value = (Set<String>) openApiCacheManager.get(key);
        if (value == null) {
            value = getAuthIpWhitelist(accessKey);
            // 如果没有查询到权限信息,不设置缓存,有可能是网络或者权限系统内部错误
            if (Collections3.isEmpty(value)) {
                return null;
            }
            openApiCacheManager.add(key, value);
        }
        return value;
    }

    private String authIpKey(String accessKey) {
        return Apps.getAppName() + ":" + AUTHIP_CACHE_KEY_PREFIX + accessKey;
    }

    private String authorizationKey(String accessKey) {
        return Apps.getAppName() + ":" + AUTHZ_CACHE_KEY_PREFIX + accessKey;
    }

    private String authenticationKey(String accessKey) {
        return Apps.getAppName() + ":" + AUTHC_CACHE_KEY_PREFIX + accessKey;
    }

    public void removeCache(String accessKey) {
        openApiCacheManager.cleanup(authenticationKey(accessKey));
        openApiCacheManager.cleanup(authorizationKey(accessKey));
        openApiCacheManager.cleanup(authIpKey(accessKey));
    }

    /**
     * 获取安全码
     *
     * @param accessKey
     * @return
     */
    public abstract String getSecretKey(String accessKey);

    /**
     * 获取产品名称列表
     *
     * @param accessKey
     * @return
     */
    public abstract Set<String> getAuthorizedServices(String accessKey);

    public abstract Set<String> getAuthIpWhitelist(String accessKey);
}
