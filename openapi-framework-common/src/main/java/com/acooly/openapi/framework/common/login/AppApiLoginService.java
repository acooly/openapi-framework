/** create by zhangpu date:2016年1月10日 */
package com.acooly.openapi.framework.common.login;

import com.acooly.openapi.framework.common.context.ApiContext;

/**
 * @author zhangpu
 * @date 2016年1月10日
 */
public interface AppApiLoginService {

  LoginDto login(LoginRequest loginRequest, ApiContext apiContext);
}
