/*
 * acooly.com Inc.
 * Copyright (c) 2014 All Rights Reserved
 */
package com.acooly.openapi.framework.core.auth;

import com.acooly.openapi.framework.core.executer.ApiContext;

import java.util.Map;

/**
 * Api Service 认证接口
 *
 * @author zhangpu
 * @date 2014年5月17日
 */
public interface ApiAuthentication {

  void authenticate(ApiContext apiContext);

  String signature(Map<String, String> response);

  String signature(Map<String, String> responseData, String partnerId, String signType);
}
