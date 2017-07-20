/*
 * www.yiji.com Inc.
 * Copyright (c) 2016 All Rights Reserved
 */

/*
 * 修订记录:
 * kuli@yiji.com 2016-07-30 23:25 创建
 */
package com.yiji.framework.openapi.core.notify.impl;

import com.acooly.core.utils.net.HttpResult;
import com.acooly.core.utils.net.Https;
import com.yiji.framework.openapi.core.notify.ApiNotifySender;
import com.yiji.framework.openapi.core.notify.domain.NotifySendMessage;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * 默认异步通知发送实现
 *
 * @author acooly
 */
@Getter
@Setter
@Component
public class DefaultApiNotifySender implements ApiNotifySender {

    private static final Logger logger = LoggerFactory.getLogger(DefaultApiNotifySender.class);

    private String connectionTimeout;
    private String socketTimeout;

    @Override
    public void send(NotifySendMessage notifySendMessage) {
        try {
            HttpResult httpResult = Https.getCustomInstance(connectionTimeout, socketTimeout).post(notifySendMessage.getUrl(), notifySendMessage.getParameters());
            String result = httpResult.getBody();
            logger.info("异步通知 url:{}", notifySendMessage.getUrl());
            logger.info("异步通知 content:{}", notifySendMessage.getParameters());
            if (!isSuccess(result)) {
                throw new RuntimeException(httpResult.getStatus() + ": " + result);
            }
            logger.info("异步通知 通知成功: {}" + httpResult);
        } catch (Exception e) {
            logger.error("异步通知 通知失败: {}", e.getMessage());
        }
    }

    protected boolean isSuccess(String result) {
        if (result == null) {
            return false;
        }
        result = result.toLowerCase();
        if ("success".equals(result)) {
            return true;
        }
        result = result.replaceAll("\\s*", "");
        if ("success".equals(result)) {
            return true;
        }
        return result.length() < 50 && result.contains("success");
    }

}
