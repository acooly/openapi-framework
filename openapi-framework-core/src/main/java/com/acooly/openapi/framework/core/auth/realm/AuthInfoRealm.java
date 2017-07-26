/*
 * acooly.com Inc.
 * Copyright (c) 2014 All Rights Reserved
 */
package com.acooly.openapi.framework.core.auth.realm;

/**
 * @author zhangpu
 * @date 2014年6月27日
 */
public interface AuthInfoRealm {
	String APP_CLIENT_REALM="appClientAuthInfoRealm";
	Object getAuthenticationInfo(String partnerId);

	Object getAuthorizationInfo(String partnerId);
}
