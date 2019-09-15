/*
 * acooly.cn Inc.
 * Copyright (c) 2016 All Rights Reserved.
 * create by zhangpu
 * date:2016年3月17日
 *
 */
package com.acooly.openapi.framework.core.executer;

import com.acooly.openapi.framework.common.context.ApiContext;
import com.acooly.openapi.framework.common.context.ApiContextHolder;
import com.acooly.openapi.framework.core.filterchain.OpenApiFilterChain;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 服务执行HTTP实现
 *
 * @author zhangpu
 */
@Component
public class HttpApiServiceExecuter
        implements ApiServiceExecuter<HttpServletRequest, HttpServletResponse> {

    protected static final Logger logger = LoggerFactory.getLogger(HttpApiServiceExecuter.class);

    @Autowired
    protected OpenApiFilterChain openApiFilterChain;

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) {
        ApiContext context = ApiContextHolder.getApiContext();
        context.setHttpRequest(request);
        context.setHttpResponse(response);
        openApiFilterChain.doFilter(context);
    }
}
