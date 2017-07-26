/*
 * acooly.cn Inc.
 * Copyright (c) 2016 All Rights Reserved.
 * create by zhangpu
 * date:2016年3月17日
 */
package com.acooly.openapi.framework.core.auth.impl;

import java.util.List;

import com.acooly.openapi.framework.core.auth.ApiAuthorizer;
import com.acooly.openapi.framework.core.auth.permission.Permission;
import com.acooly.openapi.framework.core.exception.impl.ApiServiceAuthorizationException;
import org.springframework.stereotype.Component;

/**
 * 默认认证器实现
 * 
 * @author qiubo@qq.com
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
