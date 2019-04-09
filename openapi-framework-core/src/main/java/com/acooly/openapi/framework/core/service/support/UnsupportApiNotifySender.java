package com.acooly.openapi.framework.core.service.support;

import com.acooly.openapi.framework.core.notify.ApiNotifySender;
import com.acooly.openapi.framework.core.notify.domain.NotifySendMessage;

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
