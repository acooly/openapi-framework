/*
 * acooly.cn Inc.
 * Copyright (c) 2016 All Rights Reserved.
 * create by zhangpu
 * date:2016年3月17日
 */
package com.acooly.openapi.framework.core.auth.realm.impl;

import com.acooly.core.utils.Profiles;
import com.acooly.openapi.framework.core.auth.permission.Permission;
import com.acooly.openapi.framework.core.auth.permission.PermissionResolver;
import com.acooly.openapi.framework.core.auth.realm.AuthInfoRealm;
import com.acooly.openapi.framework.core.auth.realm.SimpleAuthInfoRealm;
import com.acooly.openapi.framework.core.common.cache.CacheManager;
import com.acooly.openapi.framework.core.common.cache.impl.NOOPCacheManager;
import com.acooly.openapi.framework.core.exception.impl.ApiServiceAuthenticationException;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;

import javax.annotation.Resource;
import java.util.List;

/**
 * 缓存实现
 *
 * @author zhangpu
 * @author Bohr.Qiu <qiubo@qq.com>
 */
public abstract class CacheableAuthInfoRealm
    implements AuthInfoRealm, SimpleAuthInfoRealm, InitializingBean {

  protected static final String AUTHC_CACHE_KEY_POSTFIX = "authc";
  protected static final String AUTHZ_CACHE_KEY_POSTFIX = "authz";
  protected Logger logger = LoggerFactory.getLogger(this.getClass());
  @Resource(name = "simpleMemeryCacheManager")
  CacheManager cacheManager;
  @Resource PermissionResolver permissionResolver;

  @Override
  public void afterPropertiesSet() throws Exception {

    // 非线上环境禁用缓存
    try {
      if (Profiles.isTest()) {
        cacheManager = new NOOPCacheManager();
        logger.info("openapi-不启用权限缓存.");
        return;
      }
    } catch (Exception e) {

    }
    logger.info("openapi-启用权限缓存.");
  }

  @Override
  public Object getAuthenticationInfo(String partnerId) {
    String key = partnerId + AUTHC_CACHE_KEY_POSTFIX;
    Object value = cacheManager.get(key);
    if (value == null) {
      synchronized (key) {
        value = cacheManager.get(key);
        if (value == null) {
          value = getSecretKey(partnerId);
          if (value != null) {
            cacheManager.add(key, value);
          } else {
            throw new ApiServiceAuthenticationException("获取认证信息失败或不存在");
          }
        }
      }
    }
    return value;
  }

  @SuppressWarnings("unchecked")
  @Override
  public Object getAuthorizationInfo(String partnerId) {
    String key = partnerId + AUTHZ_CACHE_KEY_POSTFIX;
    List<Permission> value = (List<Permission>) cacheManager.get(key);
    if (value == null) {
      synchronized (key) {
        value = (List<Permission>) cacheManager.get(key);
        if (value == null) {
          List<String> permStrList = getAuthorizedServices(partnerId);
          // 如果没有查询到权限信息,不设置缓存,有可能是网络或者权限系统内部错误
          if (permStrList == null || permStrList.isEmpty()) {
            return null;
          }
          List<Permission> perms = Lists.newArrayList();
          for (String permStr : permStrList) {
            if (!Strings.isNullOrEmpty(permStr)) {
              perms.add(permissionResolver.resolvePermission(permStr));
            }
          }
          value = perms;
          cacheManager.add(key, value);
        }
      }
    }
    return value;
  }

  @Override
  public abstract String getSecretKey(String accessKey);

  /**
   * 获取产品名称列表
   *
   * @param partnerId
   * @return
   */
  public abstract List<String> getAuthorizedServices(String partnerId);
}
