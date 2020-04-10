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
 * OpenApi客户端异常
 * <p>
 * 专用于客户端错误和异常处理
 *
 * @author zhangpu
 * @date 2020-02-23
 */
public class ApiClientException extends ApiException {

    public ApiClientException() {
        super();
    }

    public ApiClientException(String resultCode, String resultMessage, String detail, Throwable cause) {
        super(resultCode, resultMessage, detail, cause);
    }

    public ApiClientException(String resultCode, String resultMessage, String detail) {
        super(resultCode, resultMessage, detail);
    }

    public ApiClientException(String resultCode, String resultMessage) {
        super(resultCode, resultMessage);
    }

    public ApiClientException(Messageable messageable, String detail, Throwable cause) {
        super(messageable, detail, cause);
    }

    public ApiClientException(Messageable messageable, String detail) {
        super(messageable, detail);
    }

    public ApiClientException(Messageable messageable) {
        super(messageable);
    }
}
