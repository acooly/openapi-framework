/*
 * acooly.cn Inc.
 * Copyright (c) 2016 All Rights Reserved.
 * create by zhangpu 
 * date:2016年3月17日
 *
 */
package com.yiji.framework.openapi.core.exception.handler;

import com.yiji.framework.openapi.common.enums.ApiServiceResultCode;
import com.yiji.framework.openapi.common.exception.ApiServiceException;
import com.yiji.framework.openapi.common.message.ApiRequest;
import com.yiji.framework.openapi.common.message.ApiResponse;
import com.yiji.framework.openapi.core.exception.ApiServiceExceptionHander;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 框架统一异常处理 默认实现
 *
 * @author acooly
 */
@Service
@Slf4j
public class DefaultApiServiceExceptionHander implements ApiServiceExceptionHander {

    @Override
    public void handleApiServiceException(ApiRequest apiRequest, ApiResponse apiResponse, Throwable ase) {
        if (ApiServiceException.class.isAssignableFrom(ase.getClass())) {
            handleApiServiceException(apiResponse, (ApiServiceException) ase);
        } else {
            String serviceName="";
            if(apiRequest != null){
                serviceName=apiRequest.getService();
            }
            log.error("处理服务[{}]异常",serviceName,ase);
            handleInternalException(apiResponse);
        }
    }

    /**
     * 服务异常处理
     *
     * @param apiResponse
     * @param ase
     */
    protected void handleApiServiceException(ApiResponse apiResponse, ApiServiceException ase) {
        apiResponse.setResultCode(ase.getResultCode());
        apiResponse.setResultMessage(ase.getResultMessage());
        apiResponse.setResultDetail(ase.getDetail());
    }

    /**
     * 系统异常处理
     *
     * @param apiResponse
     */
    protected void handleInternalException(ApiResponse apiResponse) {
        apiResponse.setResultCode(ApiServiceResultCode.INTERNAL_ERROR.code());
        apiResponse.setResultMessage(ApiServiceResultCode.INTERNAL_ERROR.message());
    }

}
