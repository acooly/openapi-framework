package com.acooly.openapi.framework.app.openapi.service;

import com.acooly.openapi.framework.app.AppProperties;
import com.acooly.openapi.framework.app.biz.domain.AppCustomer;
import com.acooly.openapi.framework.app.biz.enums.EntityStatus;
import com.acooly.openapi.framework.app.biz.service.AppCustomerService;
import com.acooly.openapi.framework.app.openapi.AppApiErrorCode;
import com.acooly.openapi.framework.app.openapi.enums.ApiOwners;
import com.acooly.openapi.framework.app.openapi.message.LoginRequest;
import com.acooly.openapi.framework.app.openapi.message.LoginResponse;
import com.acooly.openapi.framework.app.openapi.support.AppApiLoginService;
import com.acooly.openapi.framework.app.openapi.support.LoginDto;
import com.acooly.openapi.framework.app.openapi.support.login.AnonymousAppApiLoginService;
import com.acooly.openapi.framework.common.annotation.OpenApiService;
import com.acooly.openapi.framework.common.enums.ResponseType;
import com.acooly.openapi.framework.common.exception.ApiServiceException;
import com.acooly.openapi.framework.core.service.base.BaseApiService;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.AbstractEnvironment;

import java.util.Map;

/**
 * 用户登录
 *
 * @author zhangpu
 * @note
 *     <p>用户登录服务需要目标项目根据需求实现ApiLoginService接口
 */
@OpenApiService(
  name = "login",
  desc = "用户登录",
  responseType = ResponseType.SYN,
  owner = ApiOwners.COMMON
)
@Slf4j
public class LoginApiService extends BaseApiService<LoginRequest, LoginResponse>
    implements InitializingBean {
  @Autowired private AppProperties appProperties;

  @Autowired private AppApiLoginService appApiLoginService;
  @Autowired private AppCustomerService appCustomerService;

  @Override
  protected void doService(LoginRequest request, LoginResponse response) {
    try {
      // 登录验证
      Map<String, Object> context = Maps.newHashMap();
      context.put("request", request);
      LoginDto dto =
          appApiLoginService.login(request.getUsername(), request.getPassword(), context);
      response.setCustomerId(dto.getCustomerId());
      AppCustomer appCustomer =
          appCustomerService.loadAppCustomer(dto.getAccessKey(), EntityStatus.Enable);
      // 如果明确是app client， 生成动态安全码
      if (request.isAppClient()) {
        if (appCustomer == null) {
          appCustomer = new AppCustomer();
          appCustomer.setUserName(dto.getAccessKey());
          appCustomer.setAccessKey(dto.getAccessKey());
          appCustomer.setDeviceId(request.getDeviceId());
          appCustomer.setDeviceType(request.getDeviceType());
          appCustomer.setDeviceModel(request.getDeviceModel());
          appCustomer = appCustomerService.createAppCustomer(appCustomer);
        } else {
          String activeProfile =
              System.getProperty(AbstractEnvironment.ACTIVE_PROFILES_PROPERTY_NAME);
          if ("online".equals(activeProfile)
              && appProperties.isDeviceIdCheck()
              && !StringUtils.equals(request.getDeviceId(), appCustomer.getDeviceId())) {
            throw new RuntimeException("设备ID与绑定的设备ID不符");
          }

          if (appProperties.isSecretKeyDynamic()) {
            appCustomer = appCustomerService.updateSecretKey(appCustomer);
          }
        }
        response.setAccessKey(appCustomer.getAccessKey());
        response.setSecretKey(appCustomer.getSecretKey());
        response.setExtJson(dto.getExtJson());
      }

    } catch (Exception e) {
      throw new ApiServiceException(AppApiErrorCode.LOGIN_FAIL, e.getMessage());
    }
  }

  @Override
  public void afterPropertiesSet() throws Exception {
    if (appApiLoginService.getClass() == AnonymousAppApiLoginService.class) {
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
