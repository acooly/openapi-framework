package com.acooly.openapi.framework.core.service.support.login;

import com.acooly.core.common.boot.Env;
import com.acooly.core.common.enums.EntityStatus;
import com.acooly.openapi.framework.common.ApiConstants;
import com.acooly.openapi.framework.common.annotation.OpenApiService;
import com.acooly.openapi.framework.common.context.ApiContextHolder;
import com.acooly.openapi.framework.common.enums.ResponseType;
import com.acooly.openapi.framework.common.exception.ApiServiceException;
import com.acooly.openapi.framework.common.login.*;
import com.acooly.openapi.framework.core.OpenAPIProperties;
import com.acooly.openapi.framework.core.service.base.BaseApiService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 用户登录
 *
 * @author zhangpu
 * @note
 *     <p>用户登录服务需要目标项目根据需求实现ApiLoginService接口
 */
@OpenApiService(
  name =  ApiConstants.LOGIN_SERVICE_NAME,
  desc = "用户登录",
  responseType = ResponseType.SYN,
  owner = "公共"
)
@Slf4j
public class LoginApiService extends BaseApiService<LoginRequest, LoginResponse>
    implements InitializingBean {
  @Autowired private OpenAPIProperties openAPIProperties;

  @Autowired(required = false)
  private AppApiLoginService appApiLoginService;

  @Autowired(required = false)
  private AppCustomerService appCustomerService;

  @Override
  protected void doService(LoginRequest request, LoginResponse response) {
    try {
      LoginDto dto = appApiLoginService.login(request, ApiContextHolder.getApiContext());
      response.setCustomerId(dto.getCustomerId());
      String accessKey = request.getPartnerId() + "#" + request.getUsername();
      AppCustomerDto appCustomer =
          appCustomerService.loadAppCustomer(accessKey, EntityStatus.enable);
      if (appCustomer == null) {
        appCustomer = new AppCustomerDto();
        appCustomer.setUserName(request.getUsername());
        appCustomer.setAccessKey(accessKey);
        appCustomer.setDeviceId(request.getDeviceId());
        appCustomer.setDeviceType(request.getDeviceType());
        appCustomer.setDeviceModel(request.getDeviceModel());
        appCustomer.setSecretKey(RandomStringUtils.randomAlphanumeric(32));
        appCustomer = appCustomerService.createAppCustomer(appCustomer);
      } else {
        if (Env.isOnline()
            && openAPIProperties.getLogin().isDeviceIdCheck()
            && !StringUtils.equals(request.getDeviceId(), appCustomer.getDeviceId())) {
          throw new RuntimeException("设备ID与绑定的设备ID不符");
        }
        if (openAPIProperties.getLogin().isSecretKeyDynamic()) {
          appCustomer.setSecretKey(RandomStringUtils.randomAlphanumeric(32));
          appCustomer = appCustomerService.updateSecretKey(appCustomer);
        }
      }
      response.setCustomerId(dto.getCustomerId());
      response.setAccessKey(appCustomer.getAccessKey());
      response.setSecretKey(appCustomer.getSecretKey());
      response.setExtJson(dto.getExtJson());
    } catch (Exception e) {
      throw new ApiServiceException("LOGIN_FAIL", e.getMessage());
    }
  }

  @Override
  public void afterPropertiesSet() throws Exception {
    if (!openAPIProperties.getLogin().isEnable()) {
      return;
    }
    if (appApiLoginService.getClass() == DefaultAppApiLoginService.class) {
      log.warn(
          "*****************************************************************************************************************************");
      log.warn("应用系统没有提供AppApiLoginService bean实现，默认启用匿名实现，即登录时不验证用户名密码，请业务开发者考虑app是否需要登录时验证密码！");
      log.warn(
          "*****************************************************************************************************************************");
    } else {
      log.info("app登录验证实现类:{}", appApiLoginService.getClass());
    }
  }
}
