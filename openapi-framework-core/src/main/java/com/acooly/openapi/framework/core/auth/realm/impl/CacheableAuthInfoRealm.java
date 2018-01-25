/*
 * acooly.cn Inc.
 * Copyright (c) 2016 All Rights Reserved.
 * create by zhangpu
 * date:2016年3月17日
 */
package com.acooly.openapi.framework.core.auth.realm.impl;

import com.acooly.openapi.framework.core.auth.permission.Permission;
import com.acooly.openapi.framework.core.auth.permission.PermissionResolver;
import com.acooly.openapi.framework.core.auth.realm.AuthInfoRealm;
import com.acooly.openapi.framework.core.common.cache.CacheManager;
import com.acooly.openapi.framework.core.exception.impl.ApiServiceAuthenticationException;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Resource;
import java.util.List;

/**
 * 缓存实现
 *
 * @author zhangpu
 * @author Bohr.Qiu <qiubo@qq.com>
 */
public abstract class CacheableAuthInfoRealm implements AuthInfoRealm {
  protected Logger logger = LoggerFactory.getLogger(this.getClass());

  @Autowired private CacheManager cacheManager;

  @Resource PermissionResolver permissionResolver;

  @Override
  public Object getAuthenticationInfo(String accessKey) {
    String key = authenticationKey(accessKey);
    Object value = cacheManager.get(key);
    if (value == null) {
      value = getSecretKey(accessKey);
      if (value != null) {
        cacheManager.add(key, value);
      } else {
        throw new ApiServiceAuthenticationException("获取认证信息失败或不存在");
      }
    }
    return value;
  }

  @SuppressWarnings("unchecked")
  @Override
  public Object getAuthorizationInfo(String accessKey) {
    String key = authorizationKey(accessKey);
    List<Permission> value = (List<Permission>) cacheManager.get(key);
    if (value == null) {
      List<String> permStrList = getAuthorizedServices(accessKey);
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
    return value;
  }

  private String authorizationKey(String accessKey) {
    return AUTHZ_CACHE_KEY_PREFIX + accessKey;
  }

  private String authenticationKey(String accessKey) {
    return AUTHC_CACHE_KEY_PREFIX + accessKey;
  }

  public void removeCache(String accessKey) {
    cacheManager.cleanup(authenticationKey(accessKey));
    cacheManager.cleanup(authorizationKey(accessKey));
  }

  public abstract String getSecretKey(String accessKey);

  /**
   * 获取产品名称列表
   *
   * @param accessKey
   * @return
   */
  public abstract List<String> getAuthorizedServices(String accessKey);
}
