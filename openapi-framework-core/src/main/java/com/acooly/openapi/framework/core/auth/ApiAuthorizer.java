/*
 * acooly.com Inc.
 * Copyright (c) 2014 All Rights Reserved
 */

/*
 * 修订记录:
 * qiubo@qq.com 2017-07-26 00:08 创建
 *
 */
package com.acooly.openapi.framework.core.auth;

import java.util.List;

import com.acooly.openapi.framework.core.auth.permission.Permission;

/**
 * 授权处理器
 * @author qiubo@qq.com
 */
public interface ApiAuthorizer {
	void authorize(String serviceName, List<Permission> permissionList);
}
