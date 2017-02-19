/*
 * acooly.cn Inc.
 * Copyright (c) 2016 All Rights Reserved.
 * create by zhangpu
 * date:2015年3月17日
 */
package com.yiji.framework.openapi.core.auth.realm.impl;

import java.util.Iterator;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import com.yiji.framework.openapi.core.auth.realm.AuthInfoRealm;

/**
 * AuthInfoRealm 代理实现
 * 
 * @author zhangpu
 *
 */
@Component("authInfoRealm")
public class AuthInfoRealmProxy implements AuthInfoRealm, InitializingBean {

	private static final Logger logger = LoggerFactory.getLogger(AuthInfoRealmProxy.class);

	@Resource
	private ApplicationContext applicationContext;

	private AuthInfoRealm targetBean;

	@Override
	public Object getAuthenticationInfo(String partnerId) {
		return getTargetBean().getAuthenticationInfo(partnerId);
	}

	@Override
	public Object getAuthorizationInfo(String partnerId) {
		return getTargetBean().getAuthorizationInfo(partnerId);
	}

	protected AuthInfoRealm getTargetBean() {
		return this.targetBean;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		Map<String, AuthInfoRealm> beans = applicationContext.getBeansOfType(AuthInfoRealm.class);
		AuthInfoRealm defaultBean = null;
		Iterator<Map.Entry<String, AuthInfoRealm>> it = beans.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry<String, AuthInfoRealm> entry = it.next();
			if (entry.getValue().getClass().equals(this.getClass())) {
				it.remove();
			}
			if (entry.getValue().getClass().equals(SpringProxyAuthInfoRealm.class)) {
				defaultBean = entry.getValue();
				it.remove();
			} else if (entry.getValue().getClass().equals(DefaultAuthInfoRealm.class)) {
				defaultBean = entry.getValue();
				it.remove();
			}
		}
		if (beans.size() == 0) {
			this.targetBean = defaultBean;
		} else {
			this.targetBean = beans.entrySet().iterator().next().getValue();
		}
		logger.info("Proxy target bean: {}", targetBean.getClass());
	}

}
