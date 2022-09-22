/**
 * openapi-framework
 * <p>
 * Copyright 2014 Acooly.cn, Inc. All rights reserved.
 *
 * @author zhangpu
 * @date 2019-09-06 11:13
 */
package com.acooly.openapi.framework.core.filterchain.filter;

import com.acooly.core.utils.mapper.BeanCopier;
import com.acooly.module.filterchain.FilterChain;
import com.acooly.openapi.framework.common.context.ApiContext;
import com.acooly.openapi.framework.common.message.ApiResponse;
import com.acooly.openapi.framework.common.utils.ApiAnnotations;
import com.acooly.openapi.framework.core.OpenAPIProperties;
import com.acooly.openapi.framework.core.filterchain.OpenApiFilterEnum;
import com.acooly.openapi.framework.core.listener.multicaster.EventPublisher;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 服务MOCK调用和实现
 *
 * @author zhangpu
 * @date 2019-09-06 11:13
 */
@Slf4j
@Component
public class ServiceMockExecuteOpenApiFilter extends AbstractOpenApiFilter {

    @Resource
    protected OpenAPIProperties openAPIProperties;
    @Resource
    private EventPublisher eventPublisher;

    @Override
    public void doInternalFilter(ApiContext context, FilterChain<ApiContext> filterChain) {
        // 如果没有打开MOCK全局开关，则跳过
        if (!isMock(context)) {
            return;
        }
        doMock(context);
    }


    /**
     * Mock执行
     *
     * @param context
     */
    protected void doMock(ApiContext context) {
        ApiResponse demoResponse = ApiAnnotations.demoApiMessage(context.getResponse().getClass());
        BeanCopier.copy(context.getResponse(), demoResponse, BeanCopier.CopyStrategy.IGNORE_NULL, BeanCopier.NoMatchingRule.IGNORE);
        context.setResponse(demoResponse);
    }

    @Override
    protected OpenApiFilterEnum openApiFilter() {
        return OpenApiFilterEnum.ServiceMock;
    }
}
