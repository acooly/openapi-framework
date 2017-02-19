/*
 * acooly.cn Inc.
 * Copyright (c) 2016 All Rights Reserved.
 * create by zhangpu 
 * date:2016年3月17日
 *
 */
package com.yiji.framework.openapi.core.exception;


import com.yiji.framework.openapi.common.message.ApiRequest;
import com.yiji.framework.openapi.common.message.ApiResponse;

/**
 * 服务框架错误统一处理接口
 *
 * @author zhangpu
 */
public interface ApiServiceExceptionHander {

    void handleApiServiceException(ApiRequest apiRequest, ApiResponse apiResponse, Throwable ase);

}
