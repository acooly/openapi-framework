/*
 * acooly.cn Inc.
 * Copyright (c) 2016 All Rights Reserved.
 * create by zhangpu 
 * date:2016年3月17日
 */
package com.yiji.framework.openapi.core.auth.realm.impl;

import java.util.List;

import com.yiji.framework.openapi.core.OpenApiConstants;
import org.springframework.stereotype.Component;

import com.google.common.collect.Lists;
import com.yiji.framework.openapi.core.auth.realm.SimpleAuthInfoRealm;

/**
 *
 * 框架AuthInfoRealm默认实现
 *
 * 常量实现.
 *
 *
 * @author qzhanbo@yiji.com
 * @author acooly
 */
public class DefaultSimpleAuthInfoRealm implements SimpleAuthInfoRealm {

	/**
	 * 获取商户安全校验码
	 *
	 * @param accessKey
	 * @return
	 */
	@Override
	public String getSecretKey(String accessKey) {
		return OpenApiConstants.DEF_SECRETKEY;
	}

	/**
	 * 获取商户授权的权限列表.
	 *
	 * @param accessKey
	 * @return
	 */
	public List<String> getAuthorizedServices(String accessKey) {
		return Lists.newArrayList(OpenApiConstants.WILDCARD_TOKEN);

	}

}
