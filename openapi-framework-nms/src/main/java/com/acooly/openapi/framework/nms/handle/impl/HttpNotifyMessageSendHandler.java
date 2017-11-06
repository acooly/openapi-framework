/*
 * acooly.cn Inc.
 * Copyright (c) 2016 All Rights Reserved.
 * create by zhangpu
 * date:2016年3月20日
 *
 */
package com.acooly.openapi.framework.nms.handle.impl;

import com.acooly.core.utils.Strings;
import com.acooly.core.utils.net.HttpResult;
import com.acooly.core.utils.net.Https;
import com.acooly.openapi.framework.common.enums.MessageType;
import com.acooly.openapi.framework.common.enums.TaskExecuteStatus;
import com.acooly.openapi.framework.common.enums.TaskStatus;
import com.acooly.openapi.framework.core.notify.impl.DefaultApiNotifySender;
import com.acooly.openapi.framework.domain.NotifyMessage;
import com.acooly.openapi.framework.nms.handle.NotifyMessageSendHandler;
import com.acooly.openapi.framework.service.NotifyMessageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/** @author zhangpu */
@Component
public class HttpNotifyMessageSendHandler
    implements NotifyMessageSendHandler, ApplicationContextAware, InitializingBean {

  private static Logger logger = LoggerFactory.getLogger(HttpNotifyMessageSendHandler.class);
  /** 通知间隔时间,第一次隔两分钟，然后隔10分钟... 一共通知11次,失败后通知10次,55小时42分钟 */
  private static int[] notifyTime = {
    2, 10, 30, 60, 2 * 60, 6 * 60, 6 * 60, 10 * 60, 15 * 60, 15 * 60
  };
  /** 失败后通知总次数 */
  private static int notifyCount = notifyTime.length;
  @Resource private NotifyMessageService notifyMessageService;
  @Resource private TaskExecutor notifyTaskExecutor;
  private ApplicationContext applicationContext;
  private String connectionTimeout;
  // private static int[] notifyTime = { 1, 1, 1, 1, 1, 1, 1, 1, 1, 1 };
  private String socketTimeout;

  private static Date getNextNotifyTime(int num) {
    GregorianCalendar calendar = new GregorianCalendar();
    calendar.setTime(new Date());
    calendar.add(Calendar.MINUTE, notifyTime[num]);
    return calendar.getTime();
  }

  @Override
  public void send(final NotifyMessage notifyMessage) {
    notifyTaskExecutor.execute(
        new Runnable() {
          @Override
          public void run() {
            doSend(notifyMessage);
          }
        });
  }

  protected void doSend(final NotifyMessage notifyMessage) {
    MDC.put("gid", notifyMessage.getGid());
    Date now = new Date();
    // 如果非第一次通知，下次通知时间还没有到，则忽略
    if (notifyMessage.getSendCount() != 0
        && notifyMessage.getNextSendTime() != null
        && notifyMessage.getNextSendTime().after(now)) {
      return;
    }
    // 处理前锁定
    int rows = notifyMessageService.updateProccessingStatus(notifyMessage);
    if (rows <= 0) {
      // 被其他节点或线程处理了,放弃发送
      return;
    }
    notifyMessage.setSendCount(notifyMessage.getSendCount() + 1);
    logger.info("第{}次通知", notifyMessage.getSendCount());

    String result = null;
    String respInfo = null;
    try {
      HttpResult httpResult =
          Https.getCustomInstance(connectionTimeout, socketTimeout)
              .post(notifyMessage.getUrl(), notifyMessage.getParameters());
      result = httpResult.getBody();
      logger.info("通知地址:{}", notifyMessage.getUrl());
      logger.info("通知内容:{}", notifyMessage.getParameters());
      logger.info("通知结果:{}", httpResult);
      respInfo = httpResult.getStatus() + ":" + httpResult.getBody();
    } catch (Exception e) {
      respInfo = e.getMessage();
      logger.error("第{}次通知 失败，原因:{}", notifyMessage.getSendCount(), e.getMessage());
    }

    TaskStatus taskStatus = null;
    if (isSuccess(result)) {
      logger.info("第{}次通知成功", notifyMessage.getSendCount());
      taskStatus = TaskStatus.Success;
    } else {
      logger.info("第{}次通知失败", notifyMessage.getSendCount());
      int sendCount = notifyMessage.getSendCount();
      if (sendCount >= notifyCount) {
        taskStatus = TaskStatus.Finish;
        logger.info("通知失败次数达到阀值，结束通知任务");
      } else {
        taskStatus = TaskStatus.Waitting;
        notifyMessage.setNextSendTime(getNextNotifyTime(sendCount - 1));
        logger.info("通知失败次数未达到阀值，等待下次通知");
      }
    }
    notifyMessage.setStatus(taskStatus);
    notifyMessage.setExecuteStatus(TaskExecuteStatus.Unprocessed);
    if (Strings.length(respInfo) > 128) {
      respInfo = Strings.substring(respInfo, 0, 128);
    }
    notifyMessage.setRespInfo(respInfo);
    logger.info("notify save: notifyMessage:{}", notifyMessage);
    notifyMessageService.updateStatus(notifyMessage);
    MDC.clear();
  }

  protected boolean isSuccess(String result) {
    if (result == null) {
      return false;
    }
    result = result.toLowerCase();
    if ("success".equals(result)) {
      return true;
    }
    result = result.replaceAll("\\s*", "");
    if ("success".equals(result)) {
      return true;
    }
    return result.length() < 50 && result.contains("success");
  }

  @Override
  public MessageType getNotifyMessageType() {
    return MessageType.HTTP;
  }

  @Override
  public void afterPropertiesSet() throws Exception {
    DefaultApiNotifySender defaultApiNotifySender =
        applicationContext.getBean(DefaultApiNotifySender.class);
    this.connectionTimeout = defaultApiNotifySender.getConnectionTimeout();
    this.socketTimeout = defaultApiNotifySender.getSocketTimeout();
  }

  @Override
  public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
    this.applicationContext = applicationContext;
  }
}
