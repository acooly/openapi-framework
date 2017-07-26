/*
 * acooly.com Inc.
 * Copyright (c) 2014 All Rights Reserved
 */

/*
 * 修订记录:
 * qiubo@qq.com 2017-07-25 16:29 创建
 * zhangpu 2016-03-15 增加suid
 *
 */
package com.acooly.openapi.framework.core.exception.impl;

import com.acooly.openapi.framework.common.enums.ApiServiceResultCode;
import com.acooly.openapi.framework.common.exception.ApiServiceException;

/**
 * 服务路由异常
 *
 * @author qiubo@qq.com
 * @author zhangpu
 */
public class ApiServiceRouteException extends ApiServiceException {

    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = -3385225632927612343L;

    public ApiServiceRouteException(String serviceName, Object version) {
        super(ApiServiceResultCode.SERVICE_NOT_FOUND_ERROR.getCode(),
                ApiServiceResultCode.SERVICE_NOT_FOUND_ERROR.getMessage(),
                "请求服务[" + serviceName + ":" + version + "]不存在");
    }

}
