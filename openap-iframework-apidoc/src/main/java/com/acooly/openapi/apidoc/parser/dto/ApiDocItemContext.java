/*
 * www.acooly.cn Inc.
 * Copyright (c) 2017 All Rights Reserved
 */

/*
 * 修订记录:
 * zhangpu@acooly.cn 2017-12-12 10:13 创建
 */
package com.acooly.openapi.apidoc.parser.dto;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Maps;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

/**
 * ApiDoc 字段备注对象
 *
 * @author zhangpu 2017-12-12 10:13
 */
@Getter
@Setter
public class ApiDocItemContext {


    /**
     * 备注内容
     */
    private String content;

    /**
     * 备注扩展扩展
     */
    private Map<String, Object> context;


    public void put(String key, Object value) {
        // 无需线程安全，但需有序
        if (context == null) {
            context = Maps.newLinkedHashMap();
        }
        context.put(key, value);
    }


    public String toJson() {
        return JSON.toJSONString(this);
    }

}
