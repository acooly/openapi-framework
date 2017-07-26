/*
 * acooly.cn Inc.
 * Copyright (c) 2016 All Rights Reserved.
 * create by zhangpu 
 * date:2016年3月17日
 *
 */
package com.acooly.openapi.framework.core.marshall.formjson;

import com.google.common.collect.Maps;
import com.acooly.openapi.framework.common.message.ApiNotify;
import com.acooly.openapi.framework.core.marshall.ApiNotifyMarshall;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class HttpFormJsonNotifyMarshall extends AbstractHttpFormJsonResponseMarshall<Map<String, String>, ApiNotify>
        implements ApiNotifyMarshall<Map<String, String>, ApiNotify> {

    @Override
    protected Map<String, String> doMarshall(Map<String, Object> responseData) {
        return Maps.transformEntries(responseData, new Maps.EntryTransformer<String, Object, String>() {
            @Override
            public String transformEntry(String key, Object value) {
                return String.valueOf(value);
            }
        });

    }

}
