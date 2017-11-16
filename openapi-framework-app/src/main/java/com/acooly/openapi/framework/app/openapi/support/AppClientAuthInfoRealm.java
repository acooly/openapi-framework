package com.acooly.openapi.framework.app.openapi.support;

import com.acooly.openapi.framework.app.AppProperties;
import com.acooly.openapi.framework.app.biz.domain.AppCustomer;
import com.acooly.openapi.framework.app.biz.enums.EntityStatus;
import com.acooly.openapi.framework.app.biz.service.AppCustomerService;
import com.acooly.openapi.framework.core.auth.realm.impl.CacheableAuthInfoRealm;
import com.acooly.openapi.framework.core.exception.impl.ApiServiceAuthenticationException;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.acooly.openapi.framework.core.auth.realm.AuthInfoRealm.APP_CLIENT_REALM;


/**
 * APP-API认证和授权realm实现
 *
 * @author zhangpu
 */
@Component(APP_CLIENT_REALM)
public class AppClientAuthInfoRealm extends CacheableAuthInfoRealm {

  @Autowired private AppCustomerService appCustomerService;
  @Autowired private AppProperties appProperties;

  @Override
  public String getSecretKey(String partnerId) {
    if (partnerId.equals(appProperties.getAnonymous().getAccessKey())) {
      return appProperties.getAnonymous().getSecretKey();
    } else {
      AppCustomer appCustomer = appCustomerService.loadAppCustomer(partnerId, EntityStatus.Enable);
      if (appCustomer == null) {
        throw new ApiServiceAuthenticationException("app认证用户信息不存在，partnerId=" + partnerId);
      }
      return appCustomer.getSecretKey();
    }
  }

  @Override
  public List<String> getAuthorizedServices(String partnerId) {
    if (partnerId.equals(appProperties.getAnonymous().getAccessKey())) {
      return appProperties.getAnonymous().getServices();
    } else {
      return Lists.newArrayList("*");
    }
  }
}
