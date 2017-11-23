package com.acooly.openapi.framework.core.auth.realm.impl;

import com.acooly.openapi.framework.service.AuthInfoRealmService;

import javax.annotation.Resource;
import java.util.List;

/** @author qiubo@yiji.com */
public class DefaultAuthInfoRealm extends AnonymousSupportAuthInfoRealm {

  @Resource private AuthInfoRealmService authInfoRealmService;

  @Override
  public String doGetSecretKey(String accessKey) {
    return authInfoRealmService.getSercretKey(accessKey);
  }

  @Override
  public List<String> doGetAuthorizedServices(String accessKey) {
    return authInfoRealmService.getAuthorizationInfo(accessKey);
  }
}
