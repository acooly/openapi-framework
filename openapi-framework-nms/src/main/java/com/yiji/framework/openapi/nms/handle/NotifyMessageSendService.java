/*
 * acooly.cn Inc.
 * Copyright (c) 2016 All Rights Reserved.
 * create by zhangpu 
 * date:2016年3月20日
 *
 */
package com.yiji.framework.openapi.nms.handle;

import com.yiji.framework.openapi.domain.NotifyMessage;

/**
 * @author zhangpu
 */
public interface NotifyMessageSendService {

	/**
	 * 发送通知
	 * 
	 * @param notifyMessage
	 */
	void sendNotifyMessage(NotifyMessage notifyMessage);

	/***
	 * 定时任务自动处理
	 */
	void autoNotifyMessage();

}
