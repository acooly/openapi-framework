/*
 * acooly.cn Inc.
 * Copyright (c) 2016 All Rights Reserved.
 * create by zhangpu
 * date:2016年3月17日
 *
 */
package com.acooly.openapi.framework.core.marshall.json;

import com.acooly.openapi.framework.common.context.ApiContext;
import com.acooly.openapi.framework.common.context.ApiContextHolder;
import com.acooly.openapi.framework.common.dto.ApiMessageContext;
import com.acooly.openapi.framework.common.message.ApiResponse;
import com.acooly.openapi.framework.common.utils.json.JsonMarshallor;
import com.acooly.openapi.framework.core.marshall.ApiRedirectMarshall;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

@Component
public class JsonRedirectMarshallJson extends AbstractJsonResponseMarshall<ApiMessageContext, ApiResponse>
        implements ApiRedirectMarshall<ApiMessageContext, ApiResponse> {


    @Override
    protected ApiMessageContext doMarshall(ApiResponse apiResponse) {
        ApiContext apiContext = ApiContextHolder.getContext();
        ApiMessageContext messageContext = apiContext.getApiResponseContext();
        String message = JsonMarshallor.INSTANCE.marshall(apiResponse);
        messageContext.setBody(message);
        return messageContext;
    }

}
