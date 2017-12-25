/** create by zhangpu date:2016年1月10日 */
package com.acooly.openapi.framework.service;

import com.acooly.openapi.framework.common.context.ApiContext;
import com.acooly.openapi.framework.domain.LoginDto;
import com.acooly.openapi.framework.domain.LoginRequest;

/**
 * @author zhangpu
 * @date 2016年1月10日
 */
public interface AppApiLoginService {
  /**
   * 通过请求对象中的partnerId username password 判断用户登录信息是否正确，如果不正确，请抛出异常
   */
  LoginDto login(LoginRequest loginRequest, ApiContext apiContext);
}
