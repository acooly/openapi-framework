/*
 * acooly.cn Inc.
 * Copyright (c) 2016 All Rights Reserved.
 * create by zhangpu
 * date:2016年3月17日
 *
 */
package com.acooly.openapi.framework.core.marshall;

import com.acooly.openapi.framework.common.enums.ApiMessageType;
import com.acooly.openapi.framework.common.message.ApiRequest;
import com.acooly.openapi.framework.common.context.ApiContext;

/**
 * 请求报文组装接口
 *
 * @param <T>
 * @author zhangpu
 */
public interface ApiRequestMarshall<T extends ApiRequest, S extends ApiContext> extends ApiMarshall<T, S> {

    @Override
    default ApiMessageType getApiMessageType() {
        return ApiMessageType.Request;
    }
}
