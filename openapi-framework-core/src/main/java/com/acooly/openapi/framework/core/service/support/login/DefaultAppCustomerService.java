package com.acooly.openapi.framework.core.service.support.login;

import com.acooly.core.common.enums.EntityStatus;
import com.acooly.openapi.framework.common.login.AppCustomerDto;
import com.acooly.openapi.framework.common.login.AppCustomerService;
import com.google.common.collect.Maps;

import java.util.Map;

/**
 * @author qiuboboy@qq.com
 * @date 2017-11-22 20:49
 */
public class DefaultAppCustomerService implements AppCustomerService {
  private Map<String, AppCustomerDto> map = Maps.newConcurrentMap();

  @Override
  public AppCustomerDto loadAppCustomer(String accessKey, EntityStatus status) {
    return map.get(accessKey);
  }

  @Override
  public AppCustomerDto createAppCustomer(AppCustomerDto appCustomer) {
    return map.put(appCustomer.getAccessKey(), appCustomer);
  }

  @Override
  public AppCustomerDto updateSecretKey(AppCustomerDto appCustomer) {
    return appCustomer;
  }
}
