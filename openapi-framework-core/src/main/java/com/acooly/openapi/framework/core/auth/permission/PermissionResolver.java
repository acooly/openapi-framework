/*
 * acooly.com Inc.
 * Copyright (c) 2014 All Rights Reserved
 */

/*
 * 修订记录:
 * qiubo@qq.com 2017-07-27 18:11 创建
 *
 */
package com.acooly.openapi.framework.core.auth.permission;

/** @author qiubo@qq.com */
public interface PermissionResolver {
  Permission resolvePermission(String permissionString);
}
