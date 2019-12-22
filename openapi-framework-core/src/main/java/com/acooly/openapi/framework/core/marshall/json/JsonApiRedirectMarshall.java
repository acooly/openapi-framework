/*
 * acooly.cn Inc.
 * Copyright (c) 2016 All Rights Reserved.
 * create by zhangpu
 * date:2016年3月17日
 *
 */
package com.acooly.openapi.framework.core.marshall.json;

import com.acooly.core.utils.Encodes;
import com.acooly.core.utils.Strings;
import com.acooly.openapi.framework.common.ApiConstants;
import com.acooly.openapi.framework.common.context.ApiContext;
import com.acooly.openapi.framework.common.context.ApiContextHolder;
import com.acooly.openapi.framework.common.dto.ApiMessageContext;
import com.acooly.openapi.framework.common.message.ApiResponse;
import com.acooly.openapi.framework.common.utils.json.JsonMarshallor;
import com.acooly.openapi.framework.core.marshall.ApiRedirectMarshall;
import org.springframework.stereotype.Component;

/**
 * @author zhangpu
 */
@Component("jsonApiRedirectMarshall")
public class JsonApiRedirectMarshall extends AbstractJsonResponseMarshall<ApiMessageContext, ApiResponse>
        implements ApiRedirectMarshall<ApiMessageContext, ApiResponse> {


    @Override
    protected ApiMessageContext doMarshall(ApiResponse apiResponse) {
        ApiContext context = ApiContextHolder.getContext();
        ApiMessageContext messageContext = new ApiMessageContext();
        String message = JsonMarshallor.INSTANCE.marshall(apiResponse);
        messageContext.parameter(ApiConstants.BODY, Encodes.urlEncode(message));
        messageContext.parameter(ApiConstants.SIGN_TYPE, context.getSignType().code());
        messageContext.parameter(ApiConstants.ACCESS_KEY, context.getAccessKey());
        if (context.getApiProtocol() != null) {
            messageContext.parameter(ApiConstants.PROTOCOL, context.getApiProtocol().code());
        }
        messageContext.parameter(ApiConstants.GID, context.getGid());
        messageContext.setBody(message);
        return messageContext;
    }

    @Override
    protected ApiMessageContext doSign(ApiResponse apiResponse, ApiMessageContext result) {
        ApiContext context = ApiContextHolder.getContext();
        if (result.getClass().isAssignableFrom(ApiMessageContext.class)) {
            if (Strings.isNotBlank(context.getAccessKey())) {
                String sign = apiAuthentication.signature(result.getBody(), context.getAccessKey(), context.getSignType().name());
                result.parameter(ApiConstants.SIGN, sign);
            }
        }
        return result;
    }
}
