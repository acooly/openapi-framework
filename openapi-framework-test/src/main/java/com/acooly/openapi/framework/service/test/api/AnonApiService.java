/**
 * openapi-framework
 * <p>
 * Copyright 2014 Acooly.cn, Inc. All rights reserved.
 *
 * @author Administrator
 * @date 2023-05-23 16:45
 */
package com.acooly.openapi.framework.service.test.api;

import com.acooly.openapi.framework.common.annotation.ApiDocNote;
import com.acooly.openapi.framework.common.annotation.OpenApiService;
import com.acooly.openapi.framework.common.message.ApiRequest;
import com.acooly.openapi.framework.common.message.ApiResponse;
import com.acooly.openapi.framework.core.service.base.BaseApiService;
import lombok.extern.slf4j.Slf4j;

/**
 *
 * @author Administrator
 * @date 2023-05-23 16:45
 */
@Slf4j
@ApiDocNote(value = "anno测试")
@OpenApiService(name = "anno", desc = "anno测试")
public class AnonApiService extends BaseApiService<ApiRequest, ApiResponse> {


    @Override
    protected void doService(ApiRequest request, ApiResponse response) {
        log.info("anno测试 request:{}", request);
    }
}
