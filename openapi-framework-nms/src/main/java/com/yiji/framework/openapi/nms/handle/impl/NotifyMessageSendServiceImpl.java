/*
 * acooly.cn Inc.
 * Copyright (c) 2016 All Rights Reserved.
 * create by zhangpu 
 * date:2016年3月20日
 *
 */
package com.yiji.framework.openapi.nms.handle.impl;

import com.yiji.framework.openapi.domain.NotifyMessage;
import com.yiji.framework.openapi.nms.handle.NotifyMessageSendDispatcher;
import com.yiji.framework.openapi.nms.handle.NotifyMessageSendService;
import com.yiji.framework.openapi.service.NotifyMessageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author zhangpu
 */
@Service("notifyMessageSendService")
public class NotifyMessageSendServiceImpl implements NotifyMessageSendService {
    private static final Logger logger = LoggerFactory.getLogger(NotifyMessageSendDispatcherImpl.class);

    @Resource
    protected NotifyMessageSendDispatcher notifyMessageSendDispatcher;

    @Resource
    protected NotifyMessageService notifyMessageService;

    @Override
    public void sendNotifyMessage(final NotifyMessage notifyMessage) {
        try {
            notifyMessageService.insert(notifyMessage);
            notifyMessageSendDispatcher.dispatch(notifyMessage);
        } catch (Exception e) {
            throw new RuntimeException("发送异步通知失败:" + e.getMessage());
        }

    }

    @Override
    public void autoNotifyMessage() {
        try {
            List<NotifyMessage> notifyMessages = notifyMessageService.listUnProcessed(5);
            if (notifyMessages == null || notifyMessages.isEmpty()) {
                logger.debug("异步通知 无可发消息...");
                return;
            }
            for (NotifyMessage notifyMessage : notifyMessages) {
                notifyMessageSendDispatcher.dispatch(notifyMessage);
            }
            logger.info("异步通知任务 分发：{}条", notifyMessages.size());
        } catch (Exception e) {
            logger.warn("异步通知任务 失败：{}", e.getMessage());
        }

    }

}
