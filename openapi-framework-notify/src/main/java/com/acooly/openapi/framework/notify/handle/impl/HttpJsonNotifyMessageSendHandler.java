/*
 * acooly.cn Inc.
 * Copyright (c) 2016 All Rights Reserved.
 * create by zhangpu
 * date:2016年3月20日
 *
 */
package com.acooly.openapi.framework.notify.handle.impl;

import com.acooly.openapi.framework.common.ApiConstants;
import com.acooly.openapi.framework.notify.handle.NotifyMessageSendHandler;
import com.acooly.openapi.framework.service.domain.NotifyMessage;
import com.github.kevinsawicki.http.HttpRequest;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.entity.ContentType;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * JSON协议的HTTP通知实现
 *
 * @author zhangpu
 */
@Slf4j
@Component
public class HttpJsonNotifyMessageSendHandler extends AbstractNotifyMessageSendHandler implements NotifyMessageSendHandler {

    @Override
    protected HttpRequest doRequest(NotifyMessage notifyMessage) {
        Map<String, String> requestHeaders = Maps.newHashMap();
        requestHeaders.put(ApiConstants.X_API_SIGN_TYPE, notifyMessage.getParameter(ApiConstants.SIGN_TYPE));
        requestHeaders.put(ApiConstants.X_API_SIGN, notifyMessage.getParameter(ApiConstants.SIGN));
        requestHeaders.put(ApiConstants.X_API_PROTCOL, notifyMessage.getProtocol().code());
        requestHeaders.put(ApiConstants.X_API_ACCESS_KEY, notifyMessage.getPartnerId());
        String body = notifyMessage.getParameter(ApiConstants.BODY);
        log.info("通知地址:{}", notifyMessage.getUrl());
        log.info("通知报文头:{}", requestHeaders);
        log.info("通知报文体:{}", body);
        HttpRequest httpRequest = HttpRequest.post(notifyMessage.getUrl())
                .connectTimeout(openApiNotifyProperties.getConnTimeout())
                .readTimeout(openApiNotifyProperties.getReadTimeout())
                .trustAllCerts()
                .trustAllHosts()
                .headers(requestHeaders)
                .followRedirects(false)
                .contentType(ContentType.APPLICATION_JSON.toString())
                .send(body);
        return httpRequest;
    }
}
