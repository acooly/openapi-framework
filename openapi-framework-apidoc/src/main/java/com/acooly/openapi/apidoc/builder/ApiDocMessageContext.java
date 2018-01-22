/*
 * www.acooly.cn Inc.
 * Copyright (c) 2018 All Rights Reserved
 */

/*
 * 修订记录:
 * zhangpu@acooly.cn 2018-01-22 17:06 创建
 */
package com.acooly.openapi.apidoc.builder;

import com.acooly.openapi.apidoc.enums.MessageTypeEnum;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

/**
 * @author zhangpu 2018-01-22 17:06
 */
@Getter
@Setter
public class ApiDocMessageContext {

    private String serviceNo;

    private MessageTypeEnum messageType;

    private Object header;

    private Object body;


    public ApiDocMessageContext() {
    }

    public ApiDocMessageContext(String serviceNo, MessageTypeEnum messageType, Object header, Object body) {
        this.serviceNo = serviceNo;
        this.messageType = messageType;
        this.header = header;
        this.body = body;
    }
}
