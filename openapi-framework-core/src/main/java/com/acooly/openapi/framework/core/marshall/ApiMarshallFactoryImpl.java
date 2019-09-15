/**
 * openapi-framework
 * <p>
 * Copyright 2014 Acooly.cn, Inc. All rights reserved.
 *
 * @author zhangpu
 * @date 2019-09-08 11:36
 */
package com.acooly.openapi.framework.core.marshall;

import com.acooly.openapi.framework.common.enums.ApiMessageType;
import com.acooly.openapi.framework.common.enums.ApiProtocol;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * @author zhangpu
 * @date 2019-09-08 11:36
 */
@Slf4j
@Component
public class ApiMarshallFactoryImpl implements ApiMarshallFactory, ApplicationContextAware, InitializingBean {

    private ApplicationContext applicationContext;

    private Multimap<ApiProtocol, ApiMarshall> marshalls = HashMultimap.create();

    @Override
    public ApiMarshall getApiMarshall(ApiProtocol apiProtocol, ApiMessageType apiMessageType) {
        return marshalls.get(apiProtocol)
                .stream().filter(e -> e.getApiMessageType() == apiMessageType).findFirst().orElse(null);
    }


    @Override
    public void afterPropertiesSet() throws Exception {
        for (ApiMarshall apiMarshall : applicationContext.getBeansOfType(ApiMarshall.class).values()) {
            marshalls.put(apiMarshall.getProtocol(), apiMarshall);
            log.info("加载{}:{} Marshall处理器:{}", apiMarshall.getProtocol().code(),
                    apiMarshall.getApiMessageType().code(), apiMarshall.getClass().getName());
        }
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
