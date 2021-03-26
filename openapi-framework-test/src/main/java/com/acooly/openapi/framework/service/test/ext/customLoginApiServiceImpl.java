/**
 * openapi-framework
 * <p>
 * Copyright 2014 Acooly.cn, Inc. All rights reserved.
 *
 * @author zhangpu
 * @date 2021-03-26 15:40
 */
package com.acooly.openapi.framework.service.test.ext;

import com.acooly.openapi.framework.common.context.ApiContext;
import com.acooly.openapi.framework.common.message.builtin.LoginRequest;
import com.acooly.openapi.framework.service.domain.LoginDto;
import com.acooly.openapi.framework.service.service.AppApiLoginService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

/**
 *
 * @author zhangpu
 * @date 2021-03-26 15:40
 */
@Slf4j
@Component
@Primary
public class customLoginApiServiceImpl implements AppApiLoginService {
    @Override
    public LoginDto login(LoginRequest loginRequest, ApiContext apiContext) {
        String username = loginRequest.getUsername();
        String password = loginRequest.getPassword();
        String requestIp = apiContext.getRequestIp();
        // do auth...
        // String customerId = yourAuthService.auth(username,password,requestIp);
        LoginDto loginDto = new LoginDto();
        loginDto.setCustomerId("000000");
        loginDto.ext("x", "o");
        return loginDto;
    }
}
