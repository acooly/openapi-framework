/*
 * acooly.com Inc.
 * Copyright (c) 2014 All Rights Reserved
 */

/*
 * 修订记录:
 * qiubo@qq.com 2017-08-02 02:57 创建
 *
 */
package com.acooly.openapi.framework.common.context;

import com.acooly.openapi.framework.common.annotation.ArchOnly;

/**
 * 仅限框架内调用
 *
 * @author qiubo@qq.com
 * @author zhangpu
 */
@ArchOnly
public class ApiContextHolder {

    static final ThreadLocal<ApiContext> openApiContextThreadLocal = new ThreadLocal<ApiContext>();

    @ArchOnly
    public static ApiContext getContext() {
        if (!isInited()) {
            init();
        }
        return openApiContextThreadLocal.get();
    }

    @ArchOnly
    public static ApiContext getApiContext() {
        return getContext();
    }


    /**
     * 仅限框架内调用
     */
    @ArchOnly
    public static void setApiContext(ApiContext context) {
        if (context != null) {
            openApiContextThreadLocal.set(context);
        }
    }

    /**
     * 仅限框架内调用
     */
    @ArchOnly
    public static void init() {
        ApiContext context = openApiContextThreadLocal.get();
        if (context == null) {
            context = new ApiContext();
            openApiContextThreadLocal.set(context);
        }
    }

    /**
     * ApiContextHolder是否已经初始化
     *
     * <p>仅限框架内调用
     *
     * @return
     */
    @ArchOnly
    public static boolean isInited() {
        return openApiContextThreadLocal.get() != null;
    }

    /**
     * 仅限框架内调用
     */
    @ArchOnly
    public static void clear() {
        openApiContextThreadLocal.remove();
    }
}
