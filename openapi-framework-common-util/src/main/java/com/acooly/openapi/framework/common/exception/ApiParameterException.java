/*
 * acooly.com Inc.
 * Copyright (c) 2014 All Rights Reserved
 */

/*
 * 修订记录:
 * qiubo@qq.com 2017-07-25 02:04 创建
 *
 */
package com.acooly.openapi.framework.common.exception;

import com.acooly.openapi.framework.common.enums.ApiServiceResultCode;

/**
 * 参数错误异常
 *
 * @author zhangpu
 */
public class ApiParameterException extends ApiException {

    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = -3257528330627711814L;

    /**
     * @param detail 详情.
     */
    public ApiParameterException(String detail) {
        super(ApiServiceResultCode.PARAMETER_ERROR, detail);
    }

}
