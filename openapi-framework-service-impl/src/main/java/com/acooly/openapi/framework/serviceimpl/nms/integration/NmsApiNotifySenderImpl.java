/*
 * acooly.com Inc.
 * Copyright (c) 2016 All Rights Reserved
 */

/*
 * 修订记录:
 * kuli@yiji.com 2016-07-31 00:23 创建
 */
package com.acooly.openapi.framework.serviceimpl.nms.integration;

import com.acooly.core.utils.mapper.BeanCopier;
import com.acooly.openapi.framework.core.notify.ApiNotifySender;
import com.acooly.openapi.framework.core.notify.domain.NotifySendMessage;
import com.acooly.openapi.framework.domain.NotifyMessage;
import com.acooly.openapi.framework.serviceimpl.nms.handle.NotifyMessageSendService;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/** @author acooly */
@Component
public class NmsApiNotifySenderImpl implements ApiNotifySender {

  @Resource private NotifyMessageSendService notifyMessageSendService;

  @Override
  public void send(NotifySendMessage notifySendMessage) {
    NotifyMessage notifyMessage = new NotifyMessage();
    BeanCopier.copy(
        notifySendMessage,
        notifyMessage,
        BeanCopier.CopyStrategy.IGNORE_NULL,
        BeanCopier.NoMatchingRule.IGNORE);
    notifyMessageSendService.sendNotifyMessage(notifyMessage);
  }
}
