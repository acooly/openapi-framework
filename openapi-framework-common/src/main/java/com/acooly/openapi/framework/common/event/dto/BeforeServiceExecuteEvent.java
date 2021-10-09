/*
 * acooly.com Inc.
 * Copyright (c) 2014 All Rights Reserved
 */

/*
 * 修订记录:
 * qiubo@qq.com 2017-07-31 17:25 创建
 *
 */
package com.acooly.openapi.framework.common.event.dto;

import com.acooly.openapi.framework.common.context.ApiContext;
import com.acooly.openapi.framework.common.message.ApiRequest;
import com.acooly.openapi.framework.common.message.ApiResponse;

/**
 * 服务执行之前的事件.
 *
 * @author qiubo@qq.com
 * @author zhangpu 增加ApiContext的事件参数和构造
 */
public class BeforeServiceExecuteEvent extends ServiceExecuteEvent {
    public BeforeServiceExecuteEvent(ApiRequest apiRequest, ApiResponse apiResponse) {
        super(apiRequest, apiResponse);
    }

    public BeforeServiceExecuteEvent(ApiContext apiContext) {
        super(apiContext);
    }
}
