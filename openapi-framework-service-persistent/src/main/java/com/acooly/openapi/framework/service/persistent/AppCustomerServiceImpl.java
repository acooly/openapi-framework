package com.acooly.openapi.framework.service.persistent;

import com.acooly.core.common.enums.EntityStatus;
import com.acooly.openapi.framework.common.login.AppCustomerDto;
import com.acooly.openapi.framework.common.login.AppCustomerService;
import org.springframework.stereotype.Service;

/**
 * @author qiuboboy@qq.com
 * @date 2017-11-22 23:29
 */
@Service
public class AppCustomerServiceImpl implements AppCustomerService {
  @Override
  public AppCustomerDto loadAppCustomer(String accessKey, EntityStatus status) {
    return null;
  }

  @Override
  public AppCustomerDto createAppCustomer(AppCustomerDto appCustomer) {
    return null;
  }

  @Override
  public AppCustomerDto updateSecretKey(AppCustomerDto appCustomer) {
    return null;
  }
}
