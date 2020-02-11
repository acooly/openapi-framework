/**
 * create by zhangpu date:2016年1月10日
 */
package com.acooly.openapi.framework.service.service;

import com.acooly.openapi.framework.common.context.ApiContext;
import com.acooly.openapi.framework.common.message.builtin.LoginRequest;
import com.acooly.openapi.framework.service.domain.LoginDto;

/**
 * @author zhangpu
 * @date 2016年1月10日
 */
public interface AppApiLoginService {

    /**
     * 登录认证
     * <p>
     * 通过请求对象中的partnerId username password 判断用户登录信息是否正确，如果不正确，请抛出异常
     *
     * @param loginRequest
     * @param apiContext
     * @return
     */
    LoginDto login(LoginRequest loginRequest, ApiContext apiContext);
}
