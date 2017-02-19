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
public interface NotifyMessageSendDispatcher {

	void dispatch(NotifyMessage notifyMessage);

}
