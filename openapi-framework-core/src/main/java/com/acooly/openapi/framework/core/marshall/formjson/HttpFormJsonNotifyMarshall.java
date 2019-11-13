/*
 * acooly.cn Inc.
 * Copyright (c) 2016 All Rights Reserved.
 * create by zhangpu
 * date:2016年3月17日
 *
 */
package com.acooly.openapi.framework.core.marshall.formjson;

import com.acooly.core.utils.Encodes;
import com.acooly.openapi.framework.common.dto.ApiMessageContext;
import com.acooly.openapi.framework.common.message.ApiNotify;
import com.acooly.openapi.framework.core.marshall.ApiNotifyMarshall;
import com.google.common.collect.Maps;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @author zhangpu
 */
@Component("httpFormJsonNotifyMarshall")
public class HttpFormJsonNotifyMarshall extends AbstractHttpFormJsonResponseMarshall<ApiMessageContext, ApiNotify>
        implements ApiNotifyMarshall<ApiMessageContext, ApiNotify> {


    @Override
    protected ApiMessageContext doMarshall(Map<String, Object> responseData) {
        Map<String, String> map = Maps.transformValues(responseData, v -> String.valueOf(v));
        ApiMessageContext messageContext = new ApiMessageContext();
        messageContext.setParameters(map);
        messageContext.setBody(buildPostBody(map));
        return messageContext;
    }


    protected String buildPostBody(Map<String, String> map) {
        if (map == null || map.size() == 0) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        map.forEach((k, v) -> {
            sb.append(k).append("=").append(Encodes.urlEncode(v)).append("&");
        });
        return sb.substring(0, sb.length() - 1);
    }
}
