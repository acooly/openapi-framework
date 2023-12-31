/*
 * acooly.com Inc.
 * Copyright (c) 2014 All Rights Reserved
 */

/*
 * 修订记录:
 * qiubo@qq.com 2017-07-31 15:30 创建
 *
 */
package com.acooly.openapi.framework.core.listener.multicaster;

import com.acooly.openapi.framework.common.annotation.OpenApiListener;
import com.acooly.openapi.framework.core.listener.ApiListener;
import com.acooly.openapi.framework.common.event.dto.OpenApiEvent;
import com.acooly.openapi.framework.common.executor.ApiService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Map;

/** @author qiubo@qq.com */
@Component
public class EventPublisher implements InitializingBean {
  private static final Logger logger = LoggerFactory.getLogger(EventPublisher.class);

  @Resource private ApplicationContext applicationContext;

  private SystemEventMulticaster systemEventMulticaster = new SystemEventMulticaster();

  @Override
  public void afterPropertiesSet() throws Exception {
    Map<String, ApiListener> openApiListenerBeansMap =
        applicationContext.getBeansOfType(ApiListener.class);
    for (ApiListener apiListener : openApiListenerBeansMap.values()) {
      OpenApiListener listener = apiListener.getClass().getAnnotation(OpenApiListener.class);
      if (listener != null && listener.global()) {
        systemEventMulticaster.addListener(apiListener);
        logger.info("加载openapi全局监听器:{}", apiListener.getClass().getName());
      }
    }
  }

  /**
   * 发布事件到全局事件监听器和服务事件监听器
   *
   * @param event
   * @param service
   */
  public void publishEvent(OpenApiEvent event, ApiService service) {
    if (systemEventMulticaster != null) {
      systemEventMulticaster.publishEvent(event);
    }
    if (service != null) {
      service.publishEvent(event);
    }
  }

  /**
   * 发布事件到全局事件监听器
   *
   * @param event
   */
  public void publishEvent(OpenApiEvent event) {
    systemEventMulticaster.publishEvent(event);
  }

  public boolean canPublishGlobalEvent() {
    return systemEventMulticaster != null && systemEventMulticaster.canPublish();
  }

  public boolean canPublishEvent(ApiService service) {
    return systemEventMulticaster.canPublish() || (service != null && service.canPublish());
  }
}
