/*
 * acooly.com Inc.
 * Copyright (c) 2014 All Rights Reserved
 */

/*
 * 修订记录:
 * qiubo@qq.com 2017-07-27 18:13 创建
 *
 */
package com.acooly.openapi.framework.core.auth.permission;

import com.acooly.openapi.framework.common.ApiConstants;
import org.springframework.stereotype.Component;

/**
 * 权限解析器
 *
 * @author qiubo@qq.com
 */
@Component("permissionResolver")
public class DefaultPermissionResolver implements PermissionResolver {

  @Override
  public Permission resolvePermission(String permissionString) {
    if (ApiConstants.WILDCARD_TOKEN.equals(permissionString)) {
      return new AllPermission();
    }
    return new DefaultPermission(permissionString);
  }
}
