/*
 * acooly.cn Inc.
 * Copyright (c) 2016 All Rights Reserved.
 * create by zhangpu 
 * date:2016年3月17日
 *
 */
package com.acooly.openapi.framework.core.marshall.formjson;

import com.acooly.openapi.framework.core.marshall.ApiRedirectMarshall;
import com.google.common.collect.Maps;
import com.acooly.openapi.framework.common.message.ApiResponse;
import com.acooly.openapi.framework.common.utils.Servlets;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class HttpFormJsonRedirectMarshall extends AbstractHttpFormJsonResponseMarshall<String, ApiResponse>
        implements ApiRedirectMarshall<String, ApiResponse> {

    protected String getLogLabel(ApiResponse apiResponse) {
        String labelPostfix = (StringUtils.isNotBlank(apiResponse.getService()) ? "[" + apiResponse.getService() + "]:"
                : ":");
        return "服务跳转" + labelPostfix;
    }

    @Override
    protected String doMarshall(Map<String, Object> responseData) {
        Map<String, String> data = Maps.transformEntries(responseData, new Maps.EntryTransformer<String, Object, String>() {
            @Override
            public String transformEntry(String key, Object value) {
                return String.valueOf(value);
            }
        });
        return Servlets.buildQueryString(data);
    }

}
