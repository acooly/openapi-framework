/*
 * acooly.cn Inc.
 * Copyright (c) 2016 All Rights Reserved.
 * create by zhangpu 
 * date:2016年3月20日
 *
 */
package com.yiji.framework.openapi.nms.handle;

import com.yiji.framework.openapi.domain.NotifyMessage;
import com.yiji.framework.openapi.common.enums.MessageType;

/**
 * @author zhangpu
 */
public interface NotifyMessageSendHandler {

	/**
	 * 发送
	 *
	 * @param notifyMessage
	 */
	void send(NotifyMessage notifyMessage);

	MessageType getNotifyMessageType();

}
