/*
 * acooly.cn Inc.
 * Copyright (c) 2016 All Rights Reserved.
 * create by zhangpu
 * date:2016年3月17日
 *
 */
package com.acooly.openapi.framework.core.exception;

import com.acooly.openapi.framework.common.message.ApiRequest;
import com.acooly.openapi.framework.common.message.ApiResponse;

/**
 * 服务框架错误统一处理接口
 *
 * @author zhangpu
 */
public interface ApiServiceExceptionHander {

  void handleApiServiceException(ApiRequest apiRequest, ApiResponse apiResponse, Throwable ase);
}
