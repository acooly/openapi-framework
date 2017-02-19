/*
 * www.yiji.com Inc.
 * Copyright (c) 2014 All Rights Reserved
 */

/*
 * 修订记录:
 * qzhanbo@yiji.com 2014-07-27 18:11 创建
 *
 */
package com.yiji.framework.openapi.core.auth.permission;

/**
 * @author qzhanbo@yiji.com
 */
public interface PermissionResolver {
	Permission resolvePermission(String permissionString);
}
