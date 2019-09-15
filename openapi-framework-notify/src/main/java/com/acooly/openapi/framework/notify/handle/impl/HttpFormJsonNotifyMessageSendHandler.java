/*
 * acooly.cn Inc.
 * Copyright (c) 2016 All Rights Reserved.
 * create by zhangpu
 * date:2016年3月20日
 *
 */
package com.acooly.openapi.framework.notify.handle.impl;

import com.acooly.openapi.framework.common.enums.ApiProtocol;
import com.acooly.openapi.framework.notify.handle.NotifyMessageSendHandler;
import com.acooly.openapi.framework.service.domain.NotifyMessage;
import com.github.kevinsawicki.http.HttpRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * JSON协议的HTTP通知实现
 *
 * @author zhangpu
 */
@Slf4j
@Component
public class HttpFormJsonNotifyMessageSendHandler extends AbstractNotifyMessageSendHandler implements NotifyMessageSendHandler {

    @Override
    protected HttpRequest doRequest(NotifyMessage notifyMessage) {
        log.info("通知地址:{}", notifyMessage.getUrl());
        log.info("通知数据:{}", notifyMessage.getParameters());
        HttpRequest httpRequest = HttpRequest.post(notifyMessage.getUrl())
                .connectTimeout(openApiNotifyProperties.getConnTimeout())
                .readTimeout(openApiNotifyProperties.getReadTimeout())
                .trustAllCerts()
                .trustAllHosts()
                .followRedirects(false)
                .contentType(HttpRequest.CONTENT_TYPE_FORM)
                .form(notifyMessage.getParameters());
        return httpRequest;
    }

    @Override
    public ApiProtocol getApiProtocol() {
        return ApiProtocol.HTTP_FORM_JSON;
    }
}
