package com.acooly.openapi.framework.core.service.support.login;

import com.acooly.core.common.exception.BusinessException;
import com.acooly.openapi.framework.common.ApiConstants;
import com.acooly.openapi.framework.common.annotation.OpenApiService;
import com.acooly.openapi.framework.common.context.ApiContextHolder;
import com.acooly.openapi.framework.common.enums.ResponseType;
import com.acooly.openapi.framework.common.exception.ApiServiceException;
import com.acooly.openapi.framework.core.OpenAPIProperties;
import com.acooly.openapi.framework.core.auth.realm.impl.CacheableAuthInfoRealm;
import com.acooly.openapi.framework.core.service.base.BaseApiService;
import com.acooly.openapi.framework.service.domain.LoginDto;
import com.acooly.openapi.framework.service.domain.LoginRequest;
import com.acooly.openapi.framework.service.domain.LoginResponse;
import com.acooly.openapi.framework.service.service.AppApiLoginService;
import com.acooly.openapi.framework.service.service.AuthInfoRealmManageService;
import com.google.common.base.Strings;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Resource;

/**
 * 用户登录
 *
 * @author zhangpu
 * @note
 *     <p>用户登录服务需要目标项目根据需求实现ApiLoginService接口
 */
@OpenApiService(
  name = ApiConstants.LOGIN_SERVICE_NAME,
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
  private AuthInfoRealmManageService authInfoRealmManageService;

  @Resource
  private CacheableAuthInfoRealm cacheableAuthInfoRealm;

  @Override
  protected void doService(LoginRequest request, LoginResponse response) {
    try {
      LoginDto dto = appApiLoginService.login(request, ApiContextHolder.getApiContext());
      response.setCustomerId(dto.getCustomerId());
      String accessKey = request.getPartnerId() + "#" + request.getUsername();
      String sercretKey = authInfoRealmManageService.getSercretKey(accessKey);
      if (Strings.isNullOrEmpty(sercretKey)) {
        sercretKey = RandomStringUtils.randomAlphanumeric(32);
        authInfoRealmManageService.createAuthenticationInfo(accessKey, sercretKey);
        String defaultAuthorizationInfo = request.getPartnerId() + ":*";
        authInfoRealmManageService.createAuthorizationInfo(accessKey, defaultAuthorizationInfo);
      } else {
        if (openAPIProperties.getLogin().isSecretKeyDynamic()) {
          sercretKey = RandomStringUtils.randomAlphanumeric(32);
          authInfoRealmManageService.updateAuthenticationInfo(accessKey, sercretKey);
          cacheableAuthInfoRealm.removeCache(accessKey);
        }
      }
      response.getExt().putAll(dto.getExt());
      response.setCustomerId(dto.getCustomerId());
      response.setAccessKey(accessKey);
      response.setSecretKey(sercretKey);
    } catch (Exception e) {
      if (e instanceof BusinessException) {
        throw (BusinessException) e;
      }else{
        throw new ApiServiceException("LOGIN_FAIL", e.getMessage());
      }
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
