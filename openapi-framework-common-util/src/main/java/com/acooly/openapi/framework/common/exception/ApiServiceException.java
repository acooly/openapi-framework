/*
 * acooly.cn Inc.
 * Copyright (c) 2016 All Rights Reserved.
 * create by zhangpu
 * date:2016年3月17日
 *
 */
package com.acooly.openapi.framework.common.exception;

import com.acooly.core.utils.enums.Messageable;

/**
 * OpenApi服务端异常
 * <p>
 * 专用于服务器端错误和异常处理
 *
 * @author zhangpu
 * @date 2020-02-23
 */
public class ApiServiceException extends ApiException {

    public ApiServiceException() {
        super();
    }

    public ApiServiceException(String resultCode, String resultMessage, String detail, Throwable cause) {
        super(resultCode, resultMessage, detail, cause);
    }

    public ApiServiceException(String resultCode, String resultMessage, String detail) {
        super(resultCode, resultMessage, detail);
    }

    public ApiServiceException(String resultCode, String resultMessage) {
        super(resultCode, resultMessage);
    }

    public ApiServiceException(Messageable messageable, String detail, Throwable cause) {
        super(messageable, detail, cause);
    }

    public ApiServiceException(Messageable messageable, String detail) {
        super(messageable, detail);
    }

    public ApiServiceException(Messageable messageable) {
        super(messageable);
    }
}
