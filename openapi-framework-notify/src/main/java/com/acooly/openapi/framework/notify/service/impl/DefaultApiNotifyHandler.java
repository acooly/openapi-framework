/*
 * acooly.cn Inc.
 * Copyright (c) 2016 All Rights Reserved.
 * create by zhangpu
 * date:2016年3月20日
 *
 */
package com.acooly.openapi.framework.notify.service.impl;

import com.acooly.core.utils.Asserts;
import com.acooly.core.utils.Strings;
import com.acooly.openapi.framework.common.ApiConstants;
import com.acooly.openapi.framework.common.context.ApiContext;
import com.acooly.openapi.framework.common.context.ApiContextHolder;
import com.acooly.openapi.framework.common.dto.ApiMessageContext;
import com.acooly.openapi.framework.common.dto.OrderDto;
import com.acooly.openapi.framework.common.enums.ApiProtocol;
import com.acooly.openapi.framework.common.enums.ApiServiceResultCode;
import com.acooly.openapi.framework.common.enums.SignTypeEnum;
import com.acooly.openapi.framework.common.exception.ApiServiceException;
import com.acooly.openapi.framework.common.executor.ApiService;
import com.acooly.openapi.framework.common.message.ApiNotify;
import com.acooly.openapi.framework.core.marshall.ApiMarshallFactory;
import com.acooly.openapi.framework.core.service.factory.ApiServiceFactory;
import com.acooly.openapi.framework.facade.order.ApiNotifyOrder;
import com.acooly.openapi.framework.notify.handle.NotifyMessageSendService;
import com.acooly.openapi.framework.notify.service.ApiNotifyHandler;
import com.acooly.openapi.framework.service.domain.NotifyMessage;
import com.acooly.openapi.framework.service.service.OrderInfoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 框架异步通知处理 默认实现
 *
 * @author zhangpu
 */
@Component
public class DefaultApiNotifyHandler implements ApiNotifyHandler {

    private static final Logger logger = LoggerFactory.getLogger(DefaultApiNotifyHandler.class);

    @Autowired
    protected OrderInfoService orderInfoService;
    @Autowired
    protected ApiServiceFactory apiServiceFactory;
    @Resource
    protected ApiMarshallFactory apiMarshallFactory;
    @Resource
    private NotifyMessageSendService notifyMessageSendService;

    @Override
    public void asyncNotify(ApiNotifyOrder apiNotifyOrder) {
        MDC.put(ApiConstants.GID, apiNotifyOrder.getGid());
        try {
            // 获取订单信息
            apiNotifyOrder.check();
            OrderDto orderInfo = getOrderInfo(apiNotifyOrder.getGid(), apiNotifyOrder.getPartnerId());
            // 查找对应服务并调用异步业务处理
            ApiNotify apiNotify = doExcecuteService(apiNotifyOrder, orderInfo);
            ApiContextHolder.getContext().setAsyncNotify(true);
            ApiMessageContext messageContext = doMarshall(apiNotify);
            String notifyUrl = getNotifyUrl(apiNotifyOrder, orderInfo);
            NotifyMessage notifyMessage = new NotifyMessage();
            notifyMessage.setGid(apiNotifyOrder.getGid());
            notifyMessage.setRequestNo(orderInfo.getRequestNo());
            notifyMessage.setPartnerId(orderInfo.getPartnerId());
            notifyMessage.setService(orderInfo.getService());
            notifyMessage.setVersion(orderInfo.getVersion());
            notifyMessage.setProtocol(orderInfo.getProtocol());
            notifyMessage.setSignType(messageContext.getSignType());
            notifyMessage.setSign(messageContext.getSign());
            notifyMessage.setUrl(notifyUrl);
            notifyMessage.setContent(messageContext.getBody());
            notifyMessageSendService.sendNotifyMessage(notifyMessage);
        } catch (ApiServiceException ase) {
            logger.warn("异步通知 失败:", ase);
            throw ase;
        } catch (Exception e) {
            logger.warn("异步通知 失败:", e);
            throw new ApiServiceException(ApiServiceResultCode.INTERNAL_ERROR, "处理失败,请查openApi日志堆栈");
        } finally {
            MDC.clear();
        }
    }


    @Override
    public ApiMessageContext syncNotify(ApiNotifyOrder apiNotifyOrder) {
        MDC.put(ApiConstants.GID, apiNotifyOrder.getGid());
        try {
            // 获取订单信息
            apiNotifyOrder.check();
            OrderDto orderInfo = getOrderInfo(apiNotifyOrder.getGid(), apiNotifyOrder.getPartnerId());
            // 查找对应服务并调用异步业务处理
            ApiNotify apiNotify = doExcecuteService(apiNotifyOrder, orderInfo);
            ApiContextHolder.getContext().setAsyncNotify(false);
            ApiMessageContext messageContext = doMarshall(apiNotify);
            String returnUrl = getReturnUrl(apiNotifyOrder, orderInfo);
            messageContext.setUrl(returnUrl);
            return messageContext;
        } catch (ApiServiceException ase) {
            throw ase;
        } catch (Exception e) {
            logger.warn("同步通知 失败:", e);
            throw new ApiServiceException(ApiServiceResultCode.INTERNAL_ERROR, "处理失败,请查openApi日志堆栈");
        } finally {
            MDC.clear();
        }
    }

    protected ApiNotify doExcecuteService(ApiNotifyOrder apiNotifyOrder, OrderDto orderInfo) {
        ApiService apiService = apiServiceFactory.getApiService(orderInfo.getService(), orderInfo.getVersion());
        initApiContext(orderInfo, apiService);
        return apiService.handleNotify(orderInfo, apiNotifyOrder);
    }

    protected ApiMessageContext doMarshall(ApiNotify apiNotify) {
        return (ApiMessageContext) apiMarshallFactory.getNotifyMarshall(
                ApiContextHolder.getContext().getApiProtocol()).marshall(apiNotify);
    }

    protected void initApiContext(OrderDto orderInfo, ApiService apiService) {
        ApiContext context = ApiContextHolder.getContext();
        context.setGid(orderInfo.getGid());
        context.setAccessKey(orderInfo.getAccessKey());
        context.setPartnerId(orderInfo.getPartnerId());
        context.setSignType(SignTypeEnum.valueOf(orderInfo.getSignType()));
        context.setApiProtocol(orderInfo.getProtocol() == null ? ApiProtocol.JSON : orderInfo.getProtocol());
        context.setApiService(apiService);
    }


    protected String getNotifyUrl(ApiNotifyOrder apiNotifyOrder, OrderDto orderInfo) {
        String callerUrl = apiNotifyOrder.getParameter(ApiConstants.NOTIFY_URL);
        String url = Strings.defaultString(callerUrl, orderInfo.getNotifyUrl());
        Asserts.notEmpty(url, "异步通知地址不能为空");
        return url;
    }

    protected String getReturnUrl(ApiNotifyOrder apiNotifyOrder, OrderDto orderInfo) {
        String callerUrl = apiNotifyOrder.getParameter(ApiConstants.RETURN_URL);
        String url = Strings.defaultString(callerUrl, orderInfo.getReturnUrl());
        Asserts.notEmpty(url, "同步通知地址不能为空");
        return url;
    }


    /**
     * 通过GID获取请求订单信息
     *
     * @param gid
     * @return
     */
    private OrderDto getOrderInfo(String gid, String partnerId) {
        OrderDto orderInfo = orderInfoService.findByGid(gid, partnerId);
        if (orderInfo == null) {
            throw new ApiServiceException(ApiServiceResultCode.OBJECT_NOT_EXIST, "请求的原始订单不存在");
        }
        return orderInfo;
    }

}
