/** create by zhangpu date:2016年1月10日 */
package com.acooly.openapi.framework.core.service.support.login;

import com.acooly.openapi.framework.common.context.ApiContext;
import com.acooly.openapi.framework.common.login.AppApiLoginService;
import com.acooly.openapi.framework.common.login.LoginDto;
import com.acooly.openapi.framework.common.login.LoginRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author zhangpu
 * @date 2016年1月10日
 */
public class DefaultAppApiLoginService implements AppApiLoginService {

  private static final Logger logger = LoggerFactory.getLogger(DefaultAppApiLoginService.class);

  @Override
  public LoginDto login(LoginRequest loginRequest, ApiContext apiContext) {
    LoginDto loginDto = new LoginDto();
    loginDto.setCustomerId("0");
    loginDto.setExtJson(loginRequest.getExtJson());
    return loginDto;
  }
}
