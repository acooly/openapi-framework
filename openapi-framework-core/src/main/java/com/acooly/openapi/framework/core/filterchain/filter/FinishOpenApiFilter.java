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
import com.acooly.openapi.framework.common.context.ApiContextHolder;
import com.acooly.openapi.framework.core.filterchain.OpenApiFilterEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * 清理相关filter
 *
 * @author zhangpu
 * @date 2019-09-06 11:13
 */
@Slf4j
@Component
public class FinishOpenApiFilter extends AbstractOpenApiFilter {


    @Override
    public void doFilter(ApiContext context, FilterChain<ApiContext> filterChain) {
        destoryApiContext(context);
    }

    /**
     * 销毁释放线程绑定ApiContext
     *
     * @param apiContext
     */
    protected void destoryApiContext(ApiContext apiContext) {
        apiContext.destory();
        ApiContextHolder.clear();
    }

    @Override
    protected OpenApiFilterEnum openApiFilter() {
        return OpenApiFilterEnum.FinishHandle;
    }
}
