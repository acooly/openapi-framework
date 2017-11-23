package com.acooly.openapi.framework.core.auth.realm.impl;

import com.acooly.openapi.framework.core.OpenAPIProperties;
import com.acooly.openapi.framework.core.auth.realm.AppUserService;
import com.acooly.openapi.framework.service.ApiPartnerService;
import com.acooly.openapi.framework.service.ApiPartnerServiceService;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Resource;
import java.util.List;

/** @author qiubo@yiji.com */
public class DefaultAuthInfoRealm extends AnonymousSupportAuthInfoRealm {
  @Resource private ApiPartnerService apiPartnerService;

  @Resource private ApiPartnerServiceService apiPartnerServiceService;

  @Autowired(required = false)
  private AppUserService appUserService;

  @Autowired private OpenAPIProperties openAPIProperties;

  @Override
  public String doGetSecretKey(String accessKey) {
    if (accessKey.contains("#") && appUserService != null) {
      return appUserService.findSecretKeyByAccessKey(accessKey);
    } else {
      return apiPartnerService.getPartnerSercretKey(accessKey);
    }
  }

  @Override
  public List<String> doGetAuthorizedServices(String accessKey) {
    int idx = accessKey.indexOf("#");
    if (idx > 0 && appUserService != null) {
      String partenerId = accessKey.substring(idx);
      return apiPartnerServiceService.getAuthorizedServices(partenerId);
    } else {
      return apiPartnerServiceService.getAuthorizedServices(accessKey);
    }
  }
}
