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
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * @author zhangpu 2018-01-22 17:06
 */
@Getter
@Setter
@ApiModel(value = "ApiDocMessageContext", description = "api服务报文消息结构体")
public class ApiDocMessageContext {

    @ApiModelProperty(value = "服务编号")
    private String serviceNo;

    @ApiModelProperty(value = "消息类型")
    private MessageTypeEnum messageType;

    @ApiModelProperty(value = "浏览器header内容")
    private Object header;

    @ApiModelProperty(value = "浏览器body体内容")
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
