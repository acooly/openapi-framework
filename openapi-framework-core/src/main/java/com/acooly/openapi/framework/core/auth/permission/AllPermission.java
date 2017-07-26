/*
 * acooly.com Inc.
 * Copyright (c) 2014 All Rights Reserved
 */

/*
 * 修订记录:
 * qiubo@qq.com 2017-07-27 18:15 创建
 *
 */
package com.acooly.openapi.framework.core.auth.permission;

/**
 *
 * @author qiubo@qq.com
 */
public class AllPermission implements Permission {
	@Override
	public boolean implies(String resource) {
		return true;
	}
}
