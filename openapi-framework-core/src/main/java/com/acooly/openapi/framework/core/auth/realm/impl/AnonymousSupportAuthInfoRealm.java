package com.acooly.openapi.framework.core.auth.realm.impl;

import com.acooly.openapi.framework.core.OpenAPIProperties;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/** @author qiubo@yiji.com */
public abstract class AnonymousSupportAuthInfoRealm extends CacheableAuthInfoRealm {
  @Autowired private OpenAPIProperties openAPIProperties;

  @Override
  public String getSecretKey(String accessKey) {
    if (openAPIProperties.getAnonymous().isEnable()) {
      if (accessKey.equals(openAPIProperties.getAnonymous().getAccessKey())) {
        return openAPIProperties.getAnonymous().getSecretKey();
      }
    }
    return doGetSecretKey(accessKey);
  }

  protected abstract String doGetSecretKey(String accessKey);

  @Override
  public List<String> getAuthorizedServices(String accessKey) {
    if (openAPIProperties.getAnonymous().isEnable()) {
      if (accessKey.equals(openAPIProperties.getAnonymous().getAccessKey())) {
        return openAPIProperties.getAnonymous().getServices();
      }
    }
    return doGetAuthorizedServices(accessKey);
  }

  protected abstract List<String> doGetAuthorizedServices(String accessKey);
}
