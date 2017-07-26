/*
 * acooly.cn Inc.
 * Copyright (c) 2016 All Rights Reserved.
 * create by zhangpu 
 * date:2016年3月17日
 *
 */
package com.acooly.openapi.framework.core.marshall.formjson;

import com.acooly.openapi.framework.common.message.ApiResponse;
import com.acooly.openapi.framework.core.marshall.ApiResponseMarshall;
import com.acooly.openapi.framework.common.utils.json.JsonMarshallor;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * 响应报文组装
 *
 * @author zhangpu
 */
@Component
public class HttpFormJsonApiResponseMarshall extends AbstractHttpFormJsonResponseMarshall<String, ApiResponse>
        implements ApiResponseMarshall<String, ApiResponse> {

    @Override
    protected String doMarshall(Map<String, Object> responseData) {
        return JsonMarshallor.INSTANCE.marshall(responseData);
    }

}
