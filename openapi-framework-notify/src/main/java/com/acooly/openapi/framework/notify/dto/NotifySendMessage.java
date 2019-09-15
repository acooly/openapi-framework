/*
 * acooly.cn Inc.
 * Copyright (c) 2016 All Rights Reserved.
 * create by zhangpu
 * date:2016年3月20日
 *
 */
package com.acooly.openapi.framework.notify.dto;

import com.acooly.core.common.facade.LinkedHashMapParameterize;
import com.acooly.core.utils.validate.jsr303.HttpUrl;
import com.acooly.openapi.framework.common.ApiConstants;
import com.acooly.openapi.framework.common.enums.ApiProtocol;
import com.acooly.openapi.framework.common.message.ApiNotify;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Id;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * @author zhangpu
 */
@Getter
@Setter
public class NotifySendMessage extends LinkedHashMapParameterize<String, String>
        implements Serializable {

    @Id
    private Long id;
    @NotBlank
    private String gid;
    @NotBlank
    private String requestNo;
    @NotBlank
    private String merchOrderNo;
    @NotBlank
    private String partnerId;
    @NotBlank
    @HttpUrl
    private String url;
    @NotBlank
    private String service;
    @NotBlank
    private String version = ApiConstants.VERSION_DEFAULT;
    private ApiProtocol protocol = ApiProtocol.JSON;

}
