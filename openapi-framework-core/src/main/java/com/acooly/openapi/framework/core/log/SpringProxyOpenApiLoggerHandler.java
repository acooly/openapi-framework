/*
 * acooly.cn Inc.
 * Copyright (c) 2016 All Rights Reserved.
 * create by zhangpu
 * date:2016年3月17日
 *
 */
package com.acooly.openapi.framework.core.log;

import com.acooly.integration.bean.AbstractSpringProxyBean;
import com.acooly.openapi.framework.common.message.ApiMessage;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * Spring 代理实现
 *
 * @author zhangpu
 */
@Component("openApiLoggerHandler")
public class SpringProxyOpenApiLoggerHandler
        extends AbstractSpringProxyBean<OpenApiLoggerHandler, DefaultOpenApiLoggerHandler>
        implements OpenApiLoggerHandler {

    @Override
    public void log(String label, Map<String, ?> data) {
        getTarget().log(label, data);
    }

    @Override
    public void log(String label, String msg) {
        getTarget().log(label, msg);
    }

    @Override
    public void log(String label, ApiMessage apiMessage, String msg) {
        getTarget().log(label, apiMessage, msg);
    }

    @Override
    public void log(String label, ApiMessage apiMessage, String msg, Map<String, String> headers, String ext) {
        getTarget().log(label, apiMessage, msg, headers, ext);
    }
}
