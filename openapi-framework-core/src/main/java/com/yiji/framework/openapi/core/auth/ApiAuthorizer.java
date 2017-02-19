/*
 * www.yiji.com Inc.
 * Copyright (c) 2014 All Rights Reserved
 */

/*
 * 修订记录:
 * qzhanbo@yiji.com 2014-07-26 00:08 创建
 *
 */
package com.yiji.framework.openapi.core.auth;

import java.util.List;

import com.yiji.framework.openapi.core.auth.permission.Permission;

/**
 * 授权处理器
 * @author qzhanbo@yiji.com
 */
public interface ApiAuthorizer {
	void authorize(String serviceName, List<Permission> permissionList);
}
