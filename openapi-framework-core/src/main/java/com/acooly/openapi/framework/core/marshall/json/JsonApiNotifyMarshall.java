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
import com.acooly.openapi.framework.common.message.ApiNotify;
import com.acooly.openapi.framework.common.utils.json.JsonMarshallor;
import com.acooly.openapi.framework.core.marshall.ApiNotifyMarshall;
import org.springframework.stereotype.Component;

/**
 * @author zhangpu
 * @date 2019-10-05
 */
@Component
public class JsonApiNotifyMarshall extends AbstractJsonResponseMarshall<ApiMessageContext, ApiNotify>
        implements ApiNotifyMarshall<ApiMessageContext, ApiNotify> {

    @Override
    protected ApiMessageContext doMarshall(ApiNotify response) {
        ApiContext apiContext = ApiContextHolder.getContext();
        ApiMessageContext messageContext = apiContext.getApiResponseContext();
        if (messageContext == null) {
            messageContext = new ApiMessageContext();
        }
        String message = JsonMarshallor.INSTANCE.marshall(response);
        messageContext.setBody(message);
        return messageContext;
    }
}
