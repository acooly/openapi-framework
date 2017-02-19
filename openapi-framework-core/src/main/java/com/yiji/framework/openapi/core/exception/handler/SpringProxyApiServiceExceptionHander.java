/*
 * acooly.cn Inc.
 * Copyright (c) 2016 All Rights Reserved.
 * create by zhangpu 
 * date:2016年3月17日
 *
 */
package com.yiji.framework.openapi.core.exception.handler;

import com.acooly.integration.bean.AbstractSpringProxyBean;
import com.yiji.framework.openapi.common.message.ApiRequest;
import com.yiji.framework.openapi.common.message.ApiResponse;
import com.yiji.framework.openapi.core.exception.ApiServiceExceptionHander;
import org.springframework.stereotype.Service;

/**
 * @author acooly
 */
@Service("apiServiceExceptionHander")
public class SpringProxyApiServiceExceptionHander
        extends AbstractSpringProxyBean<ApiServiceExceptionHander, DefaultApiServiceExceptionHander>
        implements ApiServiceExceptionHander {

    @Override
    public void handleApiServiceException(ApiRequest apiRequest, ApiResponse apiResponse, Throwable ase) {
        getTarget().handleApiServiceException(apiRequest, apiResponse, ase);
    }
}
