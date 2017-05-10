/*
 * acooly.cn Inc.
 * Copyright (c) 2016 All Rights Reserved.
 * create by zhangpu 
 * date:2016年3月20日
 *
 */
package com.yiji.framework.openapi.core.notify;

import org.yiji.framework.openapi.facade.order.ApiNotifyOrder;

/**
 * 异步通知处理
 * <p>
 * 负责组装异步通知报文,调用异步通知发送服务发送异步通知
 *
 * @author zhangpu
 */
public interface ApiNotifyHandler {

    void notify(ApiNotifyOrder apiNotifyOrder);

    void send(ApiNotifyOrder apiNotifyOrder);

}
