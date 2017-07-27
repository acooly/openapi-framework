/*
 * acooly.cn Inc.
 * Copyright (c) 2016 All Rights Reserved.
 * create by zhangpu 
 * date:2016年3月20日
 *
 */
package com.acooly.openapi.framework.nms.handle;

import com.acooly.openapi.framework.domain.NotifyMessage;

/**
 * @author zhangpu
 */
public interface NotifyMessageSendDispatcher {

	void dispatch(NotifyMessage notifyMessage);

}