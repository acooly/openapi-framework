/**
 * openapi-framework
 * <p>
 * Copyright 2014 Acooly.cn, Inc. All rights reserved.
 *
 * @author zhangpu
 * @date 2019-04-14 14:14
 */
package com.acooly.openapi.framework.service.test.api;

import com.acooly.openapi.framework.common.annotation.ApiDocNote;
import com.acooly.openapi.framework.common.annotation.ApiDocType;
import com.acooly.openapi.framework.common.annotation.OpenApiService;
import com.acooly.openapi.framework.common.enums.ApiBusiType;
import com.acooly.openapi.framework.common.enums.ResponseType;
import com.acooly.openapi.framework.common.message.ApiRequest;
import com.acooly.openapi.framework.common.message.ApiResponse;
import com.acooly.openapi.framework.core.service.base.BaseApiService;
import lombok.extern.slf4j.Slf4j;

/**
 * @author zhangpu
 * @date 2019-04-14 14:14
 */
@Slf4j
@ApiDocType(code = "test", name = "测试")
@ApiDocNote("特殊字符专用简单测试")
@OpenApiService(name = "simpleInfo", desc = "", responseType = ResponseType.SYN, owner = "zhangpu", busiType = ApiBusiType.Trade)
public class SimpleInfoApiService extends BaseApiService<ApiRequest, ApiResponse> {
    @Override
    protected void doService(ApiRequest request, ApiResponse response) {

    }
}