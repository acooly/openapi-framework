/*
 * www.yiji.com Inc.
 * Copyright (c) 2014 All Rights Reserved
 */

/*
 * 修订记录:
 * qzhanbo@yiji.com 2014-07-27 18:13 创建
 *
 */
package com.yiji.framework.openapi.core.auth.permission;

import com.yiji.framework.openapi.core.OpenApiConstants;
import org.springframework.stereotype.Component;


/**
 * 权限解析器
 * @author qzhanbo@yiji.com
 */
@Component("permissionResolver")
public class DefaultPermissionResolver implements PermissionResolver {
	
	@Override
	public Permission resolvePermission(String permissionString) {
		if (OpenApiConstants.WILDCARD_TOKEN.equals(permissionString)) {
			return new AllPermission();
		}
		return new DefaultPermission(permissionString);
	}
}
