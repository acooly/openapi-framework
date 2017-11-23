package com.acooly.openapi.framework.common.login;

import com.acooly.core.common.enums.EntityStatus;

/**
 * @author qiuboboy@qq.com
 * @date 2017-11-22 18:06
 */
public interface AppCustomerService {
  AppCustomerDto loadAppCustomer(String accessKey, EntityStatus status);

  AppCustomerDto createAppCustomer(AppCustomerDto appCustomer);

  AppCustomerDto updateSecretKey(AppCustomerDto appCustomer);
}
