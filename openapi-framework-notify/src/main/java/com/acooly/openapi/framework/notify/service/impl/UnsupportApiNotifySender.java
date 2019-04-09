package com.acooly.openapi.framework.notify.service.impl;

import com.acooly.openapi.framework.notify.dto.NotifySendMessage;
import com.acooly.openapi.framework.notify.service.ApiNotifySender;

/**
 * @author qiuboboy@qq.com
 * @date 2017-12-08 17:45
 */
public class UnsupportApiNotifySender implements ApiNotifySender {
    @Override
    public void send(NotifySendMessage notifySendMessage) {
        throw new UnsupportedOperationException(notifySendMessage.toString());
    }
}
