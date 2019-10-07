/*
 * acooly.cn Inc.
 * Copyright (c) 2016 All Rights Reserved.
 * create by zhangpu
 * date:2016年3月17日
 *
 */
package com.acooly.openapi.framework.core.marshall.formjson;

import com.acooly.openapi.framework.common.dto.ApiMessageContext;
import com.acooly.openapi.framework.common.message.ApiResponse;
import com.acooly.openapi.framework.common.utils.json.JsonMarshallor;
import com.acooly.openapi.framework.core.marshall.ApiResponseMarshall;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * 响应报文组装
 *
 * @author zhangpu
 */
@Component("httpFormJsonApiResponseMarshall")
public class HttpFormJsonApiResponseMarshall extends AbstractHttpFormJsonResponseMarshall<ApiMessageContext, ApiResponse>
        implements ApiResponseMarshall<ApiMessageContext, ApiResponse> {


    @Override
    protected ApiMessageContext doMarshall(Map<String, Object> responseData) {
        ApiMessageContext messageContext = new ApiMessageContext();
        messageContext.setBody(JsonMarshallor.INSTANCE.marshall(responseData));
        return messageContext;
    }


}
