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
 * 参数格式错误异常
 *
 * @author qiubo@qq.com
 * @author zhangpu
 */
public class ApiParamFormatException extends ApiException {

    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = -3257528330627711814L;

    private String requiredTypeOrFormat;

    /**
     * @param paramName            参数名
     * @param paramValue           参数值
     * @param requiredTypeOrFormat 期望的类型.
     */
    public ApiParamFormatException(
            String paramName, Object paramValue, String requiredTypeOrFormat) {
        super(ApiServiceResultCode.PARAM_FORMAT_ERROR, "参数" + paramName + "=" + paramValue + ",格式错误,需要的:" + requiredTypeOrFormat);
        this.requiredTypeOrFormat = requiredTypeOrFormat;
    }

    public String getRequiredTypeOrFormat() {
        return requiredTypeOrFormat;
    }

    public void setRequiredTypeOrFormat(String requiredTypeOrFormat) {
        this.requiredTypeOrFormat = requiredTypeOrFormat;
    }
}
