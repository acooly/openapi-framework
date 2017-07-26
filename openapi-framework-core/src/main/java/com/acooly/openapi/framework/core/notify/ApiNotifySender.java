/*
 * acooly.com Inc.
 * Copyright (c) 2016 All Rights Reserved
 */

/*
 * 修订记录:
 * kuli@yiji.com 2016-07-30 23:07 创建
 */
package com.acooly.openapi.framework.core.notify;

import com.acooly.openapi.framework.core.notify.domain.NotifySendMessage;

/**
 * 异步通知发送服务接口
 * <p>
 * 默认为直接发送,如果需要采用外部实现,请直接实现该接口并放入spring容器中。
 *
 * @author acooly
 */
public interface ApiNotifySender {

    void send(NotifySendMessage notifySendMessage);

}
