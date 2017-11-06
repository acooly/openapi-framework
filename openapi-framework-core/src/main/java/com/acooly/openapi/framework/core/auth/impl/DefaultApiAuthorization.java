/*
 * acooly.cn Inc.
 * Copyright (c) 2016 All Rights Reserved.
 * create by zhangpu
 * date:2016年3月17日
 */
package com.acooly.openapi.framework.core.auth.impl;

import com.acooly.openapi.framework.common.exception.ApiServiceException;
import com.acooly.openapi.framework.common.message.ApiRequest;
import com.acooly.openapi.framework.core.auth.ApiAuthorization;
import com.acooly.openapi.framework.core.auth.ApiAuthorizer;
import com.acooly.openapi.framework.core.auth.permission.Permission;
import com.acooly.openapi.framework.core.auth.realm.AuthInfoRealm;
import com.acooly.openapi.framework.core.exception.impl.ApiServiceAuthorizationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

/**
 * 默认认证实现
 *
 * @author zhangpu
 */
@Component
public class DefaultApiAuthorization implements ApiAuthorization {

  private static final Logger logger = LoggerFactory.getLogger(DefaultApiAuthorization.class);
  @Resource protected AuthInfoRealm authInfoRealm;
  @Resource protected ApiAuthorizer apiAuthorizer;

  @SuppressWarnings("unchecked")
  @Override
  public void authorize(ApiRequest apiRequest) {
    try {
      List<Permission> permissionList =
          (List<Permission>) authInfoRealm.getAuthorizationInfo(apiRequest.getPartnerId());
      apiAuthorizer.authorize(apiRequest.getService(), permissionList);
    } catch (ApiServiceException asae) {
      throw asae;
    } catch (Exception e) {
      logger.warn("授权检查 内部错误 {}", e);
      throw new ApiServiceAuthorizationException("内部错误");
    }
  }

  public void setAuthInfoRealm(AuthInfoRealm authInfoRealm) {
    this.authInfoRealm = authInfoRealm;
  }

  public void setApiAuthorizer(ApiAuthorizer apiAuthorizer) {
    this.apiAuthorizer = apiAuthorizer;
  }
}
