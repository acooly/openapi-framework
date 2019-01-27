/*
 * acooly.cn Inc.
 * Copyright (c) 2016 All Rights Reserved.
 * create by zhangpu
 * date:2016年3月20日
 *
 */
package com.acooly.openapi.framework.notify.handle.impl;

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
import java.util.concurrent.ScheduledExecutorService;

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

    private ScheduledExecutorService retryService;

    @Autowired
    private OpenApiNotifyProperties openApiNotifyProperties;

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
//
//    private void autoNotifyMessageAtFixRate() {
//        if (retryService == null) {
//            retryService =
//                    Executors.newScheduledThreadPool(
//                            1,
//                            r -> {
//                                Thread thread = new Thread(r);
//                                thread.setName("NOTIFY-MESSAGE-RETRY-TASK");
//                                thread.setDaemon(true);
//                                return thread;
//                            });
//
//            ShutdownHooks.addShutdownHook(() -> retryService.shutdown(), "notifyMessageShutdownHook");
//
//            // 启动后1分钟开始执行,间隔2分钟执行一次
//            retryService.scheduleAtFixedRate(
//                    () -> this.autoNotifyMessage(), 60, 2 * 60, TimeUnit.SECONDS);
//        }
//    }

//    @Override
//    public void afterPropertiesSet() throws Exception {
//        try {
//            autoNotifyMessageAtFixRate();
//        } catch (Exception e) {
//            log.error("The retry notify  scheduling init failed", e);
//        }
//    }
}
