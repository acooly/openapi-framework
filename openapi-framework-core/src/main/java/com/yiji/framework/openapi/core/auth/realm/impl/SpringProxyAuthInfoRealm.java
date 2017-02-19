/*
 * acooly.cn Inc.
 * Copyright (c) 2016 All Rights Reserved.
 * create by zhangpu 
 * date:2016年3月17日
 */
package com.yiji.framework.openapi.core.auth.realm.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import com.yiji.framework.openapi.core.auth.realm.SimpleAuthInfoRealm;

/**
 * 认证授权 realm代理实现（默认实现）
 * <p/>
 * 自动获取spring容器内AuthInfoRealm的集成工程实现,如无定制实现则使用框架提供默认实现. 实现realm实现与集成工程环境以来解偶.
 *
 * @author acooly
 */
@Component
public class SpringProxyAuthInfoRealm extends CacheableAuthInfoRealm {

	@Autowired
	private ApplicationContext applicationContext;

	@Resource(name = "databaseSimpleAuthInfoRealm", description = "框架默认实现")
	private SimpleAuthInfoRealm defaultSimpleAuthInfoRealm;

	/**
	 * SimpleAuthInfoRealm 实现
	 */
	private SimpleAuthInfoRealm simpleAuthInfoRealm;

	@Override
	public String getSecretKey(String accessKey) {
		return simpleAuthInfoRealm.getSecretKey(accessKey);
	}

	@Override
	public List<String> getAuthorizedServices(String accessKey) {
		return simpleAuthInfoRealm.getAuthorizedServices(accessKey);
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		Map<String, SimpleAuthInfoRealm> simpleAuthInfoRealms = applicationContext
				.getBeansOfType(SimpleAuthInfoRealm.class);
		if (simpleAuthInfoRealms == null || simpleAuthInfoRealms.size() == 0) {
			this.simpleAuthInfoRealm = defaultSimpleAuthInfoRealm;
		}

		for (SimpleAuthInfoRealm air : simpleAuthInfoRealms.values()) {
			if (air != defaultSimpleAuthInfoRealm && air != this) {
				this.simpleAuthInfoRealm = air;
				break;
			}
		}
		if (this.simpleAuthInfoRealm == null) {
			this.simpleAuthInfoRealm = defaultSimpleAuthInfoRealm;
		}
		logger.info("代理接口:{},实现:{}", SimpleAuthInfoRealm.class.getName(),
				this.simpleAuthInfoRealm.getClass().getName());
		super.afterPropertiesSet();
	}
}
