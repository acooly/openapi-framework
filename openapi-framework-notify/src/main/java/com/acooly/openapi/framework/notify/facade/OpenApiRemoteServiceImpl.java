/*
 * acooly.cn Inc.
 * Copyright (c) 2016 All Rights Reserved.
 * create by zhangpu
 * date:2016年3月21日
 *
 */
package com.acooly.openapi.framework.notify.facade;

import com.acooly.core.common.facade.ResultBase;
import com.acooly.core.utils.enums.ResultStatus;
import com.acooly.module.appservice.AppService;
import com.acooly.openapi.framework.common.dto.ApiMessageContext;
import com.acooly.openapi.framework.core.auth.ApiAuthentication;
import com.acooly.openapi.framework.facade.api.OpenApiRemoteService;
import com.acooly.openapi.framework.facade.order.ApiNotifyOrder;
import com.acooly.openapi.framework.facade.order.ApiSignOrder;
import com.acooly.openapi.framework.facade.order.ApiVerifyOrder;
import com.acooly.openapi.framework.facade.result.ApiNotifyResult;
import com.acooly.openapi.framework.facade.result.ApiSignResult;
import com.acooly.openapi.framework.notify.service.ApiNotifyHandler;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * OpenApi 通知远程服务实现 （facade接口实现）
 *
 * @author zhangpu
 */
@Service("openApiRemoteService")
public class OpenApiRemoteServiceImpl implements OpenApiRemoteService {


    @Resource
    private ApiNotifyHandler apiNotifyHandler;
    @Resource
    private ApiAuthentication apiAuthentication;

    /**
     * 异步通知处理
     */
    @Override
    @AppService
    public ResultBase asyncNotify(ApiNotifyOrder apiNotifyOrder) {
        apiNotifyHandler.asyncNotify(apiNotifyOrder);
        return new ResultBase();
    }

    @Override
    @AppService
    public ResultBase sendNotify(ApiNotifyOrder apiNotifyOrder) {
        apiNotifyHandler.sendNotify(apiNotifyOrder);
        return new ResultBase();
    }

    @Override
    @AppService
    public ApiNotifyResult syncReturn(ApiNotifyOrder apiNotifyOrder) {
        ApiMessageContext context = apiNotifyHandler.syncNotify(apiNotifyOrder);
        ApiNotifyResult result = new ApiNotifyResult();
        result.setProtocol(context.getProtocol());
        result.setSign(context.getSign());
        result.setSignType(context.getSignType());
        result.setAccessKey(context.getAccessKey());
        result.setBody(context.getBody());
        result.setReturnUrl(context.getUrl());
        result.setStatus(ResultStatus.success);
        return result;
    }


    @Override
    @AppService
    public ResultBase verify(ApiVerifyOrder apiVerifyOrder) {
        apiAuthentication.verify(apiVerifyOrder.getBody(), apiVerifyOrder.getAccessKey(),
                apiVerifyOrder.getSignType(), apiVerifyOrder.getSign());
        return new ResultBase();
    }

    @Override
    @AppService
    public ApiSignResult sign(ApiSignOrder apiSignOrder) {
        String sign = apiAuthentication.signature(apiSignOrder.getBody(), apiSignOrder.getAccessKey(), apiSignOrder.getSignType().code());
        ApiSignResult result = new ApiSignResult();
        result.setSign(sign);
        return result;
    }
}
