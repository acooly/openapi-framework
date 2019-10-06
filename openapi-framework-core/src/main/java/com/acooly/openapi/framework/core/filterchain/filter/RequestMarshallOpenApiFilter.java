/**
 * openapi-framework
 * <p>
 * Copyright 2014 Acooly.cn, Inc. All rights reserved.
 *
 * @author zhangpu
 * @date 2019-09-06 11:13
 */
package com.acooly.openapi.framework.core.filterchain.filter;

import com.acooly.module.filterchain.FilterChain;
import com.acooly.openapi.framework.common.context.ApiContext;
import com.acooly.openapi.framework.common.message.ApiRequest;
import com.acooly.openapi.framework.core.marshall.ApiMarshallFactory;
import com.acooly.openapi.framework.core.marshall.ApiRequestMarshall;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 组织请求报文为报文对象
 *
 * @author zhangpu
 * @date 2019-09-06 11:13
 */
@Slf4j
@Component
public class RequestMarshallOpenApiFilter extends AbstractOpenApiFilter {

    @Resource
    protected ApiMarshallFactory apiMarshallFactory;

    @Override
    public void doInternalFilter(ApiContext context, FilterChain<ApiContext> filterChain) {
        ApiRequestMarshall apiRequestMarshall = apiMarshallFactory.getRequestMarshall(context.getApiProtocol());
        ApiRequest apiRequest = (ApiRequest) apiRequestMarshall.marshall(context);
        context.setRequest(apiRequest);
    }

    @Override
    public int getOrder() {
        return 3;
    }
}
