/*
 * acooly.cn Inc.
 * Copyright (c) 2016 All Rights Reserved.
 * create by zhangpu
 * date:2016年3月20日
 *
 */
package com.acooly.openapi.framework.notify.handle.impl;

import com.acooly.core.common.exception.BusinessException;
import com.acooly.openapi.framework.common.enums.ApiServiceResultCode;
import com.acooly.openapi.framework.notify.handle.NotifyMessageSendDispatcher;
import com.acooly.openapi.framework.notify.handle.NotifyMessageSendHandler;
import com.acooly.openapi.framework.service.domain.NotifyMessage;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @author zhangpu
 */
@Slf4j
@Component
public class NotifyMessageSendDispatcherImpl implements NotifyMessageSendDispatcher, InitializingBean {
    private static Map<String, NotifyMessageSendHandler> handlerMap = Maps.newHashMap();

    @Autowired
    private ApplicationContext applicationContext;

    @Override
    public void dispatch(NotifyMessage notifyMessage) {
        NotifyMessageSendHandler handler = handlerMap.get(getHandlerKey(notifyMessage.getMessageType().code(),
                notifyMessage.getProtocol().code()));
        if (handler == null) {
            log.warn("通知 没有对应的发送处理器。protocol:{},messageType:{}", notifyMessage.getProtocol(),
                    notifyMessage.getMessageType());
            throw new BusinessException(ApiServiceResultCode.OBJECT_NOT_EXIST, notifyMessage.getMessageType() + "类型的通知发送组件不存在");
        }
        handler.send(notifyMessage);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        Map<String, NotifyMessageSendHandler> map = applicationContext.getBeansOfType(NotifyMessageSendHandler.class);
        if (map.isEmpty()) {
            return;
        }
        for (NotifyMessageSendHandler handler : map.values()) {
            handlerMap.put(getHandlerKey(handler), handler);
            log.info("OpenApi 加载异步通知实现：{}/{} :{}", handler.getNotifyMessageType().getCode(),
                    handler.getApiProtocol().getCode(), handler);
        }
    }

    protected String getHandlerKey(NotifyMessageSendHandler handler) {
        return getHandlerKey(handler.getNotifyMessageType().code(), handler.getApiProtocol().code());
    }

    protected String getHandlerKey(String messageType, String protocol) {
        return messageType + "_" + protocol;
    }
}
