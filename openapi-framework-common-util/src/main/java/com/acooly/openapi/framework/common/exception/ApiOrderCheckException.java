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

import com.acooly.core.utils.enums.Messageable;
import com.acooly.openapi.framework.common.enums.ApiServiceResultCode;

import java.util.HashMap;
import java.util.Map;

/**
 * JSR303参数错误异常
 *
 * @author zhangpu
 */
public class ApiOrderCheckException extends IllegalArgumentException implements Messageable {
    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = -3257528330627711814L;

    private Map<String, String> errorMap = new HashMap<>();

    private String msg;

    /**
     * @param detail 详情.
     */
    public ApiOrderCheckException() {
        super();
    }

    public ApiOrderCheckException(String parameter, String msg) {
        super();
        this.addError(parameter, msg);
    }

    public ApiOrderCheckException(Throwable cause) {
        super(cause);
    }


    public Map<String, String> getErrorMap() {
        return errorMap;
    }

    public void addError(String parameter, String msg) {
        this.errorMap.put(parameter, msg);
        this.msg = null;
    }


    @Override
    public String getMessage() {
        if (msg == null) {
            if (errorMap.isEmpty()) {
                msg = "";
            } else {
                StringBuilder sb = new StringBuilder(errorMap.size() * 15);
                for (Map.Entry entry : errorMap.entrySet()) {
                    sb.append(entry.getValue()).append(",");
                }
                sb.deleteCharAt(sb.length() - 1);
                msg = sb.toString();
            }
        }
        return msg;
    }

    @Override
    public synchronized Throwable fillInStackTrace() {
        return this;
    }

    @Override
    public String code() {
        return ApiServiceResultCode.PARAMETER_ERROR.getCode();
    }

    @Override
    public String message() {
        return this.getMessage();
    }

}
