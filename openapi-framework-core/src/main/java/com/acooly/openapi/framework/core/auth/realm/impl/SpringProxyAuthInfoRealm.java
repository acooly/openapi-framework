/*
 * acooly.cn Inc.
 * Copyright (c) 2016 All Rights Reserved.
 * create by zhangpu
 * date:2016年3月17日
 */
package com.acooly.openapi.framework.core.auth.realm.impl;

import com.acooly.openapi.framework.core.auth.realm.SimpleAuthInfoRealm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * 认证授权 realm代理实现（默认实现）
 *
 * <p>自动获取spring容器内AuthInfoRealm的集成工程实现,如无定制实现则使用框架提供默认实现. 实现realm实现与集成工程环境以来解偶.
 *
 * @author acooly
 */
@Component
public class SpringProxyAuthInfoRealm extends CacheableAuthInfoRealm {

  @Autowired private ApplicationContext applicationContext;

  @Resource(name = "databaseSimpleAuthInfoRealm", description = "框架默认实现")
  private SimpleAuthInfoRealm defaultSimpleAuthInfoRealm;

  /** SimpleAuthInfoRealm 实现 */
  private SimpleAuthInfoRealm simpleAuthInfoRealm;

  @Override
  public String getSecretKey(String accessKey) {
    return simpleAuthInfoRealm.getSecretKey(accessKey);
  }

  @Override
  public List<String> getAuthorizedServices(String accessKey) {
    return simpleAuthInfoRealm.getAuthorizedServices(accessKey);
  }

  @Override
  public void afterPropertiesSet() throws Exception {
    Map<String, SimpleAuthInfoRealm> simpleAuthInfoRealms =
        applicationContext.getBeansOfType(SimpleAuthInfoRealm.class);
    if (simpleAuthInfoRealms == null || simpleAuthInfoRealms.size() == 0) {
      this.simpleAuthInfoRealm = defaultSimpleAuthInfoRealm;
    }

    for (Map.Entry<String, SimpleAuthInfoRealm> air : simpleAuthInfoRealms.entrySet()) {
      if (air.getValue() != defaultSimpleAuthInfoRealm
          && air.getValue() != this
          && !air.getKey().equals(APP_CLIENT_REALM)) {
        this.simpleAuthInfoRealm = air.getValue();
        break;
      }
    }
    if (this.simpleAuthInfoRealm == null) {
      this.simpleAuthInfoRealm = defaultSimpleAuthInfoRealm;
    }
    logger.info(
        "代理接口:{},实现:{}",
        SimpleAuthInfoRealm.class.getName(),
        this.simpleAuthInfoRealm.getClass().getName());
    super.afterPropertiesSet();
  }
}
