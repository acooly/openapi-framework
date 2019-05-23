/*
 * acooly.cn Inc.
 * Copyright (c) 2016 All Rights Reserved.
 * create by zhangpu
 * date:2016年3月17日
 *
 */
package com.acooly.openapi.framework.core.log;

import com.acooly.openapi.framework.common.enums.ApiProtocol;
import com.acooly.openapi.framework.common.message.ApiMessage;

import java.util.Map;

/**
 * 框架统一日志处理
 *
 * @author zhangpu
 */
public interface OpenApiLoggerHandler {

    void log(String label, Map<String, ?> data);

    void log(String label, String msg);

    void log(String label, ApiMessage apiMessage, String msg);

    default ApiProtocol getProtocol() {
        return ApiProtocol.JSON;
    }
}
