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
import com.acooly.openapi.framework.common.dto.ApiMessageContext;
import com.acooly.openapi.framework.common.exception.ApiServiceException;
import com.acooly.openapi.framework.core.auth.ApiAuthentication;
import com.acooly.openapi.framework.facade.api.OpenApiRemoteService;
import com.acooly.openapi.framework.facade.order.ApiNotifyOrder;
import com.acooly.openapi.framework.facade.order.ApiVerifyOrder;
import com.acooly.openapi.framework.facade.result.ApiNotifyResult;
import com.acooly.openapi.framework.notify.service.ApiNotifyHandler;
import com.acooly.openapi.framework.service.service.AuthInfoRealmService;
import com.acooly.openapi.framework.service.service.OrderInfoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * OpenApi 通知远程服务实现 （facade接口实现）
 *
 * @author zhangpu
 */
@Service("openApiRemoteService")
public class OpenApiRemoteServiceImpl implements OpenApiRemoteService {

    private static Logger logger = LoggerFactory.getLogger(OpenApiRemoteServiceImpl.class);

    @Resource
    private ApiNotifyHandler apiNotifyHandler;
    @Resource
    private OrderInfoService orderInfoService;
    @Resource
    private ApiAuthentication apiAuthentication;

    @Resource
    private AuthInfoRealmService authInfoRealmService;

    /**
     * 异步通知处理
     */
    @Override
    public ResultBase asyncNotify(ApiNotifyOrder apiNotifyOrder) {
        ResultBase result = new ResultBase();
        try {
            apiNotifyOrder.check();
            apiNotifyHandler.asyncNotify(apiNotifyOrder);
        } catch (ApiServiceException e) {
            result.setStatus(ResultStatus.failure);
            result.setDetail(e.getDetail());
        } catch (Exception e) {
            result.setStatus(ResultStatus.failure);
            result.setDetail(e.getMessage());
        }
        return result;
    }

    @Override
    public ApiNotifyResult syncReturn(ApiNotifyOrder apiNotifyOrder) {
        logger.info("服务跳回 入参:{}", apiNotifyOrder);
        ApiNotifyResult result = new ApiNotifyResult();
        try {
            apiNotifyOrder.check();
            ApiMessageContext context = apiNotifyHandler.syncNotify(apiNotifyOrder);
            result.setSign(context.getSign());
            result.setSignType(context.getSignType());
            result.setAccessKey(context.getAccessKey());
            result.setBody(context.getBody());
            result.setReturnUrl(context.getUrl());
            result.setStatus(ResultStatus.success);
        } catch (ApiServiceException e) {
            result.setStatus(ResultStatus.failure);
            result.setDetail(e.getDetail());
        } catch (Exception e) {
            result.setStatus(ResultStatus.failure);
            result.setDetail(e.getMessage());
        }
        logger.info("服务跳回 出参:{}", result);
        return result;
    }


    @Override
    public ResultBase verify(ApiVerifyOrder apiVerifyOrder) {
        ResultBase result = new ResultBase();
        try {
            apiVerifyOrder.check();
            apiAuthentication.verify(apiVerifyOrder.getBody(), apiVerifyOrder.getAccessKey(), apiVerifyOrder.getSignType(), apiVerifyOrder.getSign());
        } catch (ApiServiceException e) {
            result.setStatus(e);
            result.setDetail(e.getDetail());
        } catch (Exception e) {
            result.setStatus(ResultStatus.failure);
            result.setDetail(e.getMessage());
        }
        return result;
    }


}
