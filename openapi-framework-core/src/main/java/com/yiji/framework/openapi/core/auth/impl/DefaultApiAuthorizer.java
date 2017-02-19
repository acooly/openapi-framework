/*
 * acooly.cn Inc.
 * Copyright (c) 2016 All Rights Reserved.
 * create by zhangpu
 * date:2016年3月17日
 */
package com.yiji.framework.openapi.core.auth.impl;

import java.util.List;

import org.springframework.stereotype.Component;

import com.yiji.framework.openapi.core.auth.ApiAuthorizer;
import com.yiji.framework.openapi.core.auth.permission.Permission;
import com.yiji.framework.openapi.core.exception.impl.ApiServiceAuthorizationException;

/**
 * 默认认证器实现
 * 
 * @author qzhanbo@yiji.com
 * @author zhangpu
 */
@Component
public class DefaultApiAuthorizer implements ApiAuthorizer {

	@Override
	public void authorize(String serviceName, List<Permission> permissionList) {
		if (permissionList != null) {
			for (Permission perm : permissionList) {
				if (perm.implies(serviceName)) {
					return;
				}
			}
		}

		throw new ApiServiceAuthorizationException("服务[" + serviceName + "]未授权");
	}

}
