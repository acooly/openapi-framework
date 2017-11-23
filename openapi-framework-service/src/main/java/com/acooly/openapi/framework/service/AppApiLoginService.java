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

  LoginDto login(LoginRequest loginRequest, ApiContext apiContext);
}
