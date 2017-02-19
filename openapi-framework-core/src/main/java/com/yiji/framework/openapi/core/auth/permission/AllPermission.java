/*
 * www.yiji.com Inc.
 * Copyright (c) 2014 All Rights Reserved
 */

/*
 * 修订记录:
 * qzhanbo@yiji.com 2014-07-27 18:15 创建
 *
 */
package com.yiji.framework.openapi.core.auth.permission;

/**
 *
 * @author qzhanbo@yiji.com
 */
public class AllPermission implements Permission {
	@Override
	public boolean implies(String resource) {
		return true;
	}
}
