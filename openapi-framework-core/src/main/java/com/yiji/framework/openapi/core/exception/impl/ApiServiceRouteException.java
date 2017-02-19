/*
 * www.yiji.com Inc.
 * Copyright (c) 2014 All Rights Reserved
 */

/*
 * 修订记录:
 * qzhanbo@yiji.com 2014-07-25 16:29 创建
 * zhangpu 2016-03-15 增加suid
 *
 */
package com.yiji.framework.openapi.core.exception.impl;

import com.yiji.framework.openapi.common.enums.ApiServiceResultCode;
import com.yiji.framework.openapi.common.exception.ApiServiceException;

/**
 * 服务路由异常
 *
 * @author qzhanbo@yiji.com
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
