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
  Object getAuthenticationInfo(String accessKey);

  Object getAuthorizationInfo(String accessKey);
}
