/*
 * acooly.com Inc.
 * Copyright (c) 2014 All Rights Reserved
 */
package com.acooly.openapi.framework.core.auth;


import com.acooly.openapi.framework.common.message.ApiRequest;

/**
 * API Service 授权接口
 *
 * @author zhangpu
 * @date 2014年5月17日
 */
public interface ApiAuthorization {

    void authorize(ApiRequest apiRequest);
}
