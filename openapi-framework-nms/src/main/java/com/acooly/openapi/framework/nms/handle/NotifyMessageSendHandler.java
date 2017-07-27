/*
 * acooly.cn Inc.
 * Copyright (c) 2016 All Rights Reserved.
 * create by zhangpu 
 * date:2016年3月20日
 *
 */
package com.acooly.openapi.framework.nms.handle;

import com.acooly.openapi.framework.domain.NotifyMessage;
import com.acooly.openapi.framework.common.enums.MessageType;

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