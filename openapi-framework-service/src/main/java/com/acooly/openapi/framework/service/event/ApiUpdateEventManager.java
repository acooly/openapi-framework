/**
 * openapi-framework
 * <p>
 * Copyright 2014 Acooly.cn, Inc. All rights reserved.
 *
 * @author zhangpu
 * @date 2023-07-10 19:54
 */
package com.acooly.openapi.framework.service.event;

import com.acooly.module.event.EventBus;
import com.acooly.openapi.framework.service.domain.ApiAuth;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 发布清除缓存的事件。
 * <p>
 * 处理器：openapi-framework-core: `com.acooly.openapi.framework.core.auth.event.ApiUpdateEventHandler
 *
 * @author zhangpu
 * @date 2023-07-10 19:54
 */
@Slf4j
@Component
public class ApiUpdateEventManager {

    @Autowired(required = false)
    private EventBus eventBus;

    public void publish(ApiUpdateEvent event) {
        if (eventBus != null) {
            eventBus.publish(event);
        }
    }

    public void publish(ApiAuth oldApiAuth) {
        publish(new ApiUpdateEvent(oldApiAuth));
    }
}
