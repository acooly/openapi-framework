/*
 * acooly.cn Inc.
 * Copyright (c) 2016 All Rights Reserved.
 * create by zhangpu
 * date:2016年3月20日
 *
 */
package com.acooly.openapi.framework.nms.handle.impl;

import com.acooly.openapi.framework.common.enums.MessageType;
import com.acooly.openapi.framework.domain.NotifyMessage;
import com.acooly.openapi.framework.nms.handle.NotifyMessageSendDispatcher;
import com.acooly.openapi.framework.nms.handle.NotifyMessageSendHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.util.EnumMap;
import java.util.Map;

/** @author zhangpu */
@Component
public class NotifyMessageSendDispatcherImpl
    implements NotifyMessageSendDispatcher, InitializingBean {

  private static final Logger logger =
      LoggerFactory.getLogger(NotifyMessageSendDispatcherImpl.class);

  private static Map<MessageType, NotifyMessageSendHandler> handlerMap =
      new EnumMap<>(MessageType.class);

  @Autowired private ApplicationContext applicationContext;

  @Override
  public void dispatch(NotifyMessage notifyMessage) {
    NotifyMessageSendHandler handler = handlerMap.get(notifyMessage.getMessageType());
    if (handler == null) {
      throw new RuntimeException(notifyMessage.getMessageType() + "类型的通知发送组件不存在");
    }
    handler.send(notifyMessage);
  }

  @Override
  public void afterPropertiesSet() throws Exception {
    Map<String, NotifyMessageSendHandler> map =
        applicationContext.getBeansOfType(NotifyMessageSendHandler.class);
    if (map.isEmpty()) {
      return;
    }
    for (NotifyMessageSendHandler handler : map.values()) {
      if (handler.getNotifyMessageType() != null) {
        handlerMap.put(handler.getNotifyMessageType(), handler);
        logger.info("加载{}通知发送组件:{}", handler.getNotifyMessageType().getCode(), handler);
      }
    }
  }
}
