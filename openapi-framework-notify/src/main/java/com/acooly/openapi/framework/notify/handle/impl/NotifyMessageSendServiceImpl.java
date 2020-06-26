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
import com.acooly.openapi.framework.notify.OpenApiNotifyProperties;
import com.acooly.openapi.framework.notify.handle.NotifyMessageSendDispatcher;
import com.acooly.openapi.framework.notify.handle.NotifyMessageSendService;
import com.acooly.openapi.framework.service.domain.NotifyMessage;
import com.acooly.openapi.framework.service.service.NotifyMessageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author zhangpu
 */
@Slf4j
@Service("notifyMessageSendService")
public class NotifyMessageSendServiceImpl implements NotifyMessageSendService {

    @Resource
    protected NotifyMessageSendDispatcher notifyMessageSendDispatcher;

    @Resource
    protected NotifyMessageService notifyMessageService;

    @Autowired
    private OpenApiNotifyProperties openApiNotifyProperties;

    @Override
    public void sendNotifyMessage(final NotifyMessage notifyMessage) {
        try {
            if (notifyMessage.getId() == null) {
                notifyMessageService.insert(notifyMessage);
            }
            notifyMessageSendDispatcher.dispatch(notifyMessage);
        } catch (BusinessException be) {
            throw be;
        } catch (Exception e) {
            log.warn("异步通知发送 失败:", e);
            throw new BusinessException(ApiServiceResultCode.INTERNAL_ERROR, e.getMessage());
        }
    }

    @Override
    public void autoNotifyMessage() {
        try {
            List<NotifyMessage> notifyMessages = notifyMessageService.listUnProcessed(openApiNotifyProperties.getRetryFetchSize());
            if (notifyMessages == null || notifyMessages.isEmpty()) {
                log.debug("异步通知 无可发消息...");
                return;
            }
            for (NotifyMessage notifyMessage : notifyMessages) {
                notifyMessageSendDispatcher.dispatch(notifyMessage);
            }
            log.info("异步通知任务 分发：{}条", notifyMessages.size());
        } catch (Exception e) {
            log.warn("异步通知任务 失败：{}", e.getMessage());
        }
    }
}
