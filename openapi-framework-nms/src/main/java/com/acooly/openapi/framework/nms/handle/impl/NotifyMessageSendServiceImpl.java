/*
 * acooly.cn Inc.
 * Copyright (c) 2016 All Rights Reserved.
 * create by zhangpu
 * date:2016年3月20日
 *
 */
package com.acooly.openapi.framework.nms.handle.impl;

import com.acooly.core.utils.ShutdownHooks;
import com.acooly.openapi.framework.domain.NotifyMessage;
import com.acooly.openapi.framework.nms.handle.NotifyMessageSendDispatcher;
import com.acooly.openapi.framework.nms.handle.NotifyMessageSendService;
import com.acooly.openapi.framework.service.NotifyMessageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/** @author zhangpu */
@Service("notifyMessageSendService")
public class NotifyMessageSendServiceImpl implements NotifyMessageSendService, InitializingBean {
  private static final Logger logger =
      LoggerFactory.getLogger(NotifyMessageSendDispatcherImpl.class);

  @Resource protected NotifyMessageSendDispatcher notifyMessageSendDispatcher;

  @Resource protected NotifyMessageService notifyMessageService;

  private ScheduledExecutorService retryService;

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

  private void autoNotifyMessageAtFixRate() {
    if (retryService == null) {
      retryService =
          Executors.newScheduledThreadPool(
              1,
              r -> {
                Thread thread = new Thread(r);
                thread.setName("NOTIFY-MESSAGE-RETRY-TASK");
                thread.setDaemon(true);
                return thread;
              });

      ShutdownHooks.addShutdownHook(() -> retryService.shutdown(), "notifyMessageShutdownHook");

      // 启动后1分钟开始执行,间隔2分钟执行一次
      retryService.scheduleAtFixedRate(
          () -> this.autoNotifyMessage(), 60, 2 * 60, TimeUnit.SECONDS);
    }
  }

  @Override
  public void afterPropertiesSet() throws Exception {
    try {
      autoNotifyMessageAtFixRate();
    } catch (Exception e) {
      logger.error("The retry notify  task init failed", e);
    }
  }
}
