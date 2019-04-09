/*
 * acooly.cn Inc.
 * Copyright (c) 2016 All Rights Reserved.
 * create by zhangpu
 * date:2016年3月20日
 *
 */
package com.acooly.openapi.framework.notify.handle;

import com.acooly.openapi.framework.service.domain.NotifyMessage;

/** @author zhangpu */
public interface NotifyMessageSendService {

  /**
   * 发送通知
   *
   * @param notifyMessage
   */
  void sendNotifyMessage(NotifyMessage notifyMessage);

  /** * 定时任务自动处理 */
  void autoNotifyMessage();
}
