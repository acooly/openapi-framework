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
 * OpenApi异常父类,所有异常必须继承于此异常.
 *
 * @author zhangpu
 * @date 2020-02-23
 */
public class ApiException extends RuntimeException implements Messageable {

    /**
     * UID
     */
    private static final long serialVersionUID = 4741339021195297955L;

    private String resultCode;
    private String resultMessage;
    private String detail;

    public ApiException() {
        super();
    }

    /**
     * @param resultCode
     * @param resultMessage
     * @param detail
     */
    public ApiException(String resultCode, String resultMessage, String detail, Throwable cause) {
        super(resultCode + ":" + resultMessage + ":" + detail, cause);
        this.resultCode = resultCode;
        this.resultMessage = resultMessage;
        this.detail = detail;
    }

    public ApiException(String resultCode, String resultMessage, String detail) {
        this(resultCode, resultMessage, detail, null);
    }

    public ApiException(String resultCode, String resultMessage) {
        this(resultCode, resultMessage, null);
    }

    public ApiException(Messageable messageable, String detail, Throwable cause) {
        this(messageable.code(), messageable.message(), detail, cause);
    }

    public ApiException(Messageable messageable, String detail) {
        this(messageable, detail, null);
    }

    public ApiException(Messageable messageable) {
        this(messageable, null);
    }


    public String detail() {
        return detail;
    }

    @Override
    public String code() {
        return this.resultCode;
    }

    @Override
    public String message() {
        return this.resultMessage;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("{resultCode:").append(resultCode)
                .append(", resultMessage:").append(resultMessage)
                .append(", detail:").append(detail)
                .append("}");
        return builder.toString();
    }

    @Override
    public synchronized Throwable fillInStackTrace() {
        return this;
    }

    public String getResultCode() {
        return resultCode;
    }

    public void setResultCode(String resultCode) {
        this.resultCode = resultCode;
    }

    public String getResultMessage() {
        return resultMessage;
    }

    public void setResultMessage(String resultMessage) {
        this.resultMessage = resultMessage;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }
}
