/*
 * acooly.cn Inc.
 * Copyright (c) 2016 All Rights Reserved.
 * create by zhangpu
 * date:2016年3月20日
 *
 */
package com.acooly.openapi.framework.notify.handle.impl;

import com.acooly.openapi.framework.common.ApiConstants;
import com.acooly.openapi.framework.common.dto.ApiMessageContext;
import com.acooly.openapi.framework.notify.handle.NotifyMessageSendHandler;
import com.acooly.openapi.framework.service.domain.NotifyMessage;
import com.github.kevinsawicki.http.HttpRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.entity.ContentType;
import org.springframework.stereotype.Component;

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
        ApiMessageContext messageContext = doMessage(notifyMessage);
        log.info("通知地址:{}", messageContext.getUrl());
        log.info("通知报文头:{}", messageContext.getHeaders());
        log.info("通知报文体:{}", messageContext.getBody());
        HttpRequest httpRequest = HttpRequest.post(messageContext.getUrl())
                .connectTimeout(openApiNotifyProperties.getConnTimeout())
                .readTimeout(openApiNotifyProperties.getReadTimeout())
                .trustAllCerts()
                .trustAllHosts()
                .headers(messageContext.getHeaders())
                .followRedirects(false)
                .contentType(ContentType.APPLICATION_JSON.toString())
                .send(messageContext.getBody());
        return httpRequest;
    }


    protected ApiMessageContext doMessage(NotifyMessage notifyMessage) {
        ApiMessageContext messageContext = new ApiMessageContext();
        messageContext.header(ApiConstants.X_API_SIGN_TYPE, notifyMessage.getSignType());
        messageContext.header(ApiConstants.X_API_SIGN, notifyMessage.getSign());
        messageContext.header(ApiConstants.X_API_PROTOCOL, notifyMessage.getProtocol().code());
        messageContext.header(ApiConstants.X_API_ACCESS_KEY, notifyMessage.getPartnerId());
        messageContext.setBody(notifyMessage.getContent());
        messageContext.setUrl(notifyMessage.getUrl());
        return messageContext;
    }


}
