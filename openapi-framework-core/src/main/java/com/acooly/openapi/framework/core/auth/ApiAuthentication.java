/*
 * acooly.com Inc.
 * Copyright (c) 2014 All Rights Reserved
 */
package com.acooly.openapi.framework.core.auth;

import java.util.Map;

/**
 * Api Service 认证接口
 * 
 * @author zhangpu
 * @date 2014年5月17日
 */
public interface ApiAuthentication {

	void authenticate(Map<String, String> response);

	void signature(Map<String, String> response);

	void signature(Map<String, String> responseData, String partnerId, String signType);
}
