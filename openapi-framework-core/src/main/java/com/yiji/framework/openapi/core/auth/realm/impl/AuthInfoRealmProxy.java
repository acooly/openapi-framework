/*
 * acooly.cn Inc.
 * Copyright (c) 2016 All Rights Reserved.
 * create by zhangpu
 * date:2015年3月17日
 */
package com.yiji.framework.openapi.core.auth.realm.impl;

import com.yiji.framework.openapi.common.exception.ApiServiceException;
import com.yiji.framework.openapi.core.auth.realm.AuthInfoRealm;
import com.yiji.framework.openapi.core.executer.ApiContextHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Iterator;
import java.util.Map;

import static com.yiji.framework.openapi.common.enums.ApiServiceResultCode.APP_CLIENT_NOT_SUPPORT;
import static com.yiji.framework.openapi.core.OpenApiConstants.APP_CLIENT_ENABLE;

/**
 * AuthInfoRealm 代理实现
 *
 * @author zhangpu
 */
@Component("authInfoRealm")
public class AuthInfoRealmProxy implements AuthInfoRealm, InitializingBean {

    private static final Logger logger = LoggerFactory.getLogger(AuthInfoRealmProxy.class);

    @Resource
    private ApplicationContext applicationContext;

    private AuthInfoRealm targetBean;
    private AuthInfoRealm appAuthInfoRealm;

    @Override
    public Object getAuthenticationInfo(String partnerId) {
        return getTargetBean().getAuthenticationInfo(partnerId);
    }

    @Override
    public Object getAuthorizationInfo(String partnerId) {
        return getTargetBean().getAuthorizationInfo(partnerId);
    }

    protected AuthInfoRealm getTargetBean() {
        if (ApiContextHolder.getApiContext() != null && ApiContextHolder.getApiContext().isAppClient()) {
            if (appAuthInfoRealm == null) {
                throw new ApiServiceException(APP_CLIENT_NOT_SUPPORT);
            }
            return appAuthInfoRealm;
        } else {
            return this.targetBean;
        }
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
            if (entry.getKey().equals(APP_CLIENT_REALM)) {
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
        Boolean appClient = Boolean.parseBoolean(System.getProperty(APP_CLIENT_ENABLE));
        if (appClient) {
            try {
                appAuthInfoRealm = applicationContext.getBean(APP_CLIENT_REALM, AuthInfoRealm.class);
            } catch (BeansException e) {
                logger.error("openapi启动了移动端特性支持，需要定义bean name=appClientAuthInfoRealm的AuthInfoRealm");
            }
        }
    }

}
