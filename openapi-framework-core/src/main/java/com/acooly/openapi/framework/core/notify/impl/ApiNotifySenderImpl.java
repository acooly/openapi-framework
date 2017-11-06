/*
 * acooly.com Inc.
 * Copyright (c) 2016 All Rights Reserved
 */

/*
 * 修订记录:
 * kuli@yiji.com 2016-07-30 23:58 创建
 */
package com.acooly.openapi.framework.core.notify.impl;

import com.acooly.integration.bean.AbstractSpringProxyBean;
import com.acooly.openapi.framework.core.notify.ApiNotifySender;
import com.acooly.openapi.framework.core.notify.domain.NotifySendMessage;
import org.springframework.stereotype.Service;

/**
 * 异步通知发送服务 代理实现
 *
 * @author acooly
 */
@Service("apiNotifySender")
public class ApiNotifySenderImpl
    extends AbstractSpringProxyBean<ApiNotifySender, DefaultApiNotifySender>
    implements ApiNotifySender {

  @Override
  public void send(NotifySendMessage notifySendMessage) {
    getTarget().send(notifySendMessage);
  }
}
