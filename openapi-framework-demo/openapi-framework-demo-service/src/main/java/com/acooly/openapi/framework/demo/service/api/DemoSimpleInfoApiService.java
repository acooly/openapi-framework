/**
 * openapi-framework
 * <p>
 * Copyright 2014 Acooly.cn, Inc. All rights reserved.
 *
 * @author zhangpu
 * @date 2019-04-14 14:14
 */
package com.acooly.openapi.framework.demo.service.api;

import com.acooly.openapi.framework.common.annotation.ApiDocNote;
import com.acooly.openapi.framework.common.annotation.ApiDocType;
import com.acooly.openapi.framework.common.annotation.OpenApiService;
import com.acooly.openapi.framework.common.enums.ApiBusiType;
import com.acooly.openapi.framework.common.enums.ResponseType;
import com.acooly.openapi.framework.common.message.ApiRequest;
import com.acooly.openapi.framework.common.message.ApiResponse;
import com.acooly.openapi.framework.core.service.base.BaseApiService;
import com.acooly.openapi.framework.demo.service.DemoApiUtils;
import lombok.extern.slf4j.Slf4j;

/**
 * @author zhangpu
 * @date 2019-04-14 14:14
 */
@Slf4j
@ApiDocType(code = DemoApiUtils.API_DEMO_DOC_TYPE_CODE, name = DemoApiUtils.API_DEMO_DOC_TYPE_NAME)
@ApiDocNote("特殊字符专用简单测试")
@OpenApiService(name = "demoSimpleInfo", desc = "测试：特殊字符报文", responseType = ResponseType.SYN, owner = "zhangpu", busiType = ApiBusiType.Trade)
public class DemoSimpleInfoApiService extends BaseApiService<ApiRequest, ApiResponse> {
    @Override
    protected void doService(ApiRequest request, ApiResponse response) {

//        ResultBase resultBase = new ResultBase();
//        resultBase.setStatus(ApiServiceResultCode.OBJECT_NOT_EXIST);
//        resultBase.setCode("TEST_ERROR_CODE");
//        resultBase.setDetail("测试错误消息");
//        resultBase.throwIfNotSuccess();
    }
}