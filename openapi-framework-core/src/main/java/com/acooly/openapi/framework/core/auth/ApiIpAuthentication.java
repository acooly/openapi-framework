/*
 * acooly.com Inc.
 * Copyright (c) 2014 All Rights Reserved
 */
package com.acooly.openapi.framework.core.auth;

import com.acooly.openapi.framework.common.context.ApiContext;

/**
 * Api Service 认证接口
 *
 * @author zhangpu
 * @date 2014年5月17日
 */
public interface ApiIpAuthentication {

    void authenticate(ApiContext apiContext);

}
