/*
 * acooly.com Inc.
 * Copyright (c) 2014 All Rights Reserved
 */
package com.acooly.openapi.framework.core.service.base;

import java.util.Map;

import javax.annotation.Resource;

import com.acooly.openapi.framework.common.enums.ApiBusiType;
import com.acooly.openapi.framework.core.executer.ApiContextHolder;
import com.acooly.openapi.framework.core.marshall.ObjectAccessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.acooly.openapi.framework.facade.order.ApiNotifyOrder;

import com.acooly.core.utils.mapper.BeanCopier;
import com.acooly.openapi.framework.common.enums.ApiProtocol;
import com.acooly.openapi.framework.common.message.ApiNotify;
import com.acooly.openapi.framework.common.message.ApiRequest;
import com.acooly.openapi.framework.common.message.ApiResponse;
import com.acooly.openapi.framework.core.executer.ApiContext;
import com.acooly.openapi.framework.domain.OrderInfo;
import com.acooly.openapi.framework.service.OrderInfoService;

/**
 * Api Service 抽象模板实现
 * <p/>
 * service处理模板，统一错误处理
 *
 * @author zhangpu
 * @author Bohr.Qiu <qiubo@qq.com>
 */
public abstract class AbstractApiService<O extends ApiRequest, R extends ApiResponse> extends GeneralApiService<O, R> {

    private static final Logger logger = LoggerFactory.getLogger(AbstractApiService.class);

    /**
     * 默认重定向地址
     */
    private String defaultRedirectUrl;
    /**
     * 订单持久化服务
     */
    @Resource
    private OrderInfoService orderInfoService;

    /**
     * 默认异步处理方法.用于构造异步响应对象.此方法会把外部请求对象中所有的参数填充到ApiNotify
     *
     * @param orderInfo 请求订单信息
     * @param data      外部调用时传入的对象
     * @return
     */
    @Override
    public final ApiNotify handleNotify(OrderInfo orderInfo, Object data) {
        ApiNotifyOrder apiNotifyOrder = (ApiNotifyOrder) data;
        ApiNotify apiNotify = getApiNotifyBean();
        BeanCopier.copy(orderInfo, apiNotify, "notifyUrl", "returnUrl");
        ObjectAccessor<ApiNotify> objectAccessor = ObjectAccessor.of(apiNotify);
        for (Map.Entry<String, String> entry : apiNotifyOrder.getParameters().entrySet()) {
            objectAccessor.setPropertyValue(entry.getKey(), entry.getValue());
        }
        // 对数据库的orderNo特别处理
        apiNotify.setMerchOrderNo(orderInfo.getOrderNo());
        customizeApiNotify(orderInfo, apiNotifyOrder, apiNotify);
        return apiNotify;
    }

    @Override
    public final void service(O request, R response) {
        ApiContext apiContext = ApiContextHolder.getApiContext();
        try {
            if (apiContext.getOpenApiService().busiType() != ApiBusiType.Query) {
                saveOrder(request, apiContext.getGid());
            }
            doService(request, response);
        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * 服务处理方法
     * <p/>
     * 如果抛出非ApiServiceException异常,对外响应内部错误
     * <p/>
     * 如果抛出ApiServiceException异常,会根据异常的信息返回给用户
     * <p/>
     *
     * @param request
     * @param response
     */
    protected abstract void doService(O request, R response);

    /**
     * 异步服务返回对象
     *
     * @param orderInfo      原始订单信息
     * @param apiNotifyOrder 外部调用过来准备推送的数据
     * @param apiNotify      准备发给cs的推送内容对象
     */
    protected void customizeApiNotify(OrderInfo orderInfo, ApiNotifyOrder apiNotifyOrder, ApiNotify apiNotify) {

    }

    /**
     * 异步通知entity默认使用基类，API业务服务可以根据需求定义ApiNotify子类，然后在ApiService中覆写该方法返回子类的类型，
     * handleNotify抽象实现会自动填充完成通知对象组装
     *
     * @return
     */
    @Override
    public ApiNotify getApiNotifyBean() {
        return new ApiNotify();
    }

    protected String getGid() {
        return ApiContextHolder.getApiContext().getGid();
    }

    public final void saveOrder(O request, String gid) {
        try {
            OrderInfo orderInfo = new OrderInfo();
            orderInfo.setRequestNo(request.getRequestNo());
            orderInfo.setGid(gid);
            orderInfo.setNotifyUrl(request.getNotifyUrl());
            orderInfo.setReturnUrl(request.getReturnUrl());
            orderInfo.setPartnerId(request.getPartnerId());
            orderInfo.setOrderNo(request.getMerchOrderNo());
            orderInfo.setService(request.getService());
            orderInfo.setVersion(request.getVersion());
            orderInfo.setSignType(request.getSignType());
            orderInfo.setProtocol(ApiProtocol.valueOf(request.getProtocol()));
            orderInfo.setContext(request.getContext());
            orderInfoService.insert(orderInfo);
        } catch (Exception e) {
            logger.warn("订单写入失败，忽略错误，继续执行服务:" + e.getMessage());
        }
    }

    /**
     * 设置重定向url地址
     * <p/>
     * 如果在服务初始化时设置的是默认重定向地址.在服务执行过程中设置的是本次请求的重定向地址.
     * <p/>
     * 本次请求的重定向地址优先被使用
     *
     * @param redirectUrl
     */
    public final void setRedirectUrl(String redirectUrl) {
        if (!ApiContextHolder.isInited()) {
            this.defaultRedirectUrl = redirectUrl;
        } else {
            ApiContextHolder.getApiContext().setRedirectUrl(redirectUrl);
        }
    }

    public final String getDefaultRedirectUrl() {
        return defaultRedirectUrl;
    }
}
