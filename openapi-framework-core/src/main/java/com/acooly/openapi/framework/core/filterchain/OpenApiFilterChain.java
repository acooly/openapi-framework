/**
 * openapi-framework
 * <p>
 * Copyright 2014 Acooly.cn, Inc. All rights reserved.
 *
 * @author zhangpu
 * @date 2019-08-31 21:24
 */
package com.acooly.openapi.framework.core.filterchain;

import com.acooly.module.filterchain.Filter;
import com.acooly.module.filterchain.AbstractFilterChainBase;
import com.acooly.openapi.framework.common.context.ApiContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.AnnotationAwareOrderComparator;
import org.springframework.stereotype.Component;

import java.util.Iterator;

/**
 * @author zhangpu
 * @date 2019-08-31 21:24
 */
@Slf4j
@Component
public class OpenApiFilterChain extends AbstractFilterChainBase<ApiContext> {

    /**
     * 同步接口处理：
     * 1. 初始化（填入入参）和验证
     * 3. 认证和权限
     * 4. request marshall
     * 5. 解密
     * <p>
     * 6. 调用服务处理
     * <p>
     * 7. 加密
     * 8. response unmarshall
     * 9. 签名
     * 10.返回
     */

    @Override
    public void doFilter(ApiContext context) {
        if (context == null) {
            return;
        }
        Iterator<Filter<ApiContext>> iterator = filters.iterator();
        while (iterator.hasNext()) {
            Filter nextFilter = iterator.next();
            nextFilter.doFilter(context, this);
        }
    }

    @Override
    protected void adjustFilters() {
        AnnotationAwareOrderComparator.sort(filters);
    }
}
