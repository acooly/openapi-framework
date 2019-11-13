/*
 * acooly.cn Inc.
 * Copyright (c) 2016 All Rights Reserved.
 * create by zhangpu
 * date:2016年3月20日
 *
 */
package com.acooly.openapi.framework.notify.handle;

import com.acooly.openapi.framework.common.enums.ApiProtocol;
import com.acooly.openapi.framework.common.enums.MessageType;
import com.acooly.openapi.framework.service.domain.NotifyMessage;

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

    /**
     * 通知类型
     *
     * @return
     */
    default MessageType getNotifyMessageType() {
        return MessageType.HTTP;
    }


    /**
     * 协议
     *
     * @return
     */
    default ApiProtocol getApiProtocol() {
        return ApiProtocol.JSON;
    }
}
