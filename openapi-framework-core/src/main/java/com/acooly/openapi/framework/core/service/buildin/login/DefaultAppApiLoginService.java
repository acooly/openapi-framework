/**
 * create by zhangpu date:2016年1月10日
 */
package com.acooly.openapi.framework.core.service.buildin.login;

import com.acooly.openapi.framework.common.context.ApiContext;
import com.acooly.openapi.framework.common.message.builtin.LoginRequest;
import com.acooly.openapi.framework.service.domain.LoginDto;
import com.acooly.openapi.framework.service.service.AppApiLoginService;

/**
 * @author zhangpu
 * @date 2016年1月10日
 */
public class DefaultAppApiLoginService implements AppApiLoginService {

    @Override
    public LoginDto login(LoginRequest loginRequest, ApiContext apiContext) {
        LoginDto loginDto = new LoginDto();
        loginDto.setCustomerId("0");
        loginDto.ext("x", "o");
        return loginDto;
    }
}
