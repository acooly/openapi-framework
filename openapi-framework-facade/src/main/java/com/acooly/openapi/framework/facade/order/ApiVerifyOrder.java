/**
 * openapi-framework
 * <p>
 * Copyright 2014 Acooly.cn, Inc. All rights reserved.
 *
 * @author zhangpu
 * @date 2019-09-16 15:18
 */
package com.acooly.openapi.framework.facade.order;

import com.acooly.core.common.facade.OrderBase;
import com.acooly.openapi.framework.common.dto.ApiMessageContext;
import com.acooly.openapi.framework.common.enums.SignTypeEnum;
import lombok.Getter;
import lombok.Setter;

import org.hibernate.validator.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 验签请求报文
 *
 * @author zhangpu
 * @date 2019-09-16 15:18
 */
@Getter
@Setter
public class ApiVerifyOrder extends OrderBase {

    /**
     * 商户访问码
     */
    @NotBlank
    private String accessKey;
    /**
     * 签名算法
     */
    @NotNull
    private SignTypeEnum signType = SignTypeEnum.MD5;
    /**
     * 请求验证的签名
     */
    @NotBlank
    private String sign;

    /**
     * 报文（被验证的签名字符串）
     */
    @NotBlank
    private String body;

    public ApiVerifyOrder() {
    }

    public ApiVerifyOrder(@NotBlank String accessKey, @NotBlank String sign, @NotBlank String body) {
        this.accessKey = accessKey;
        this.sign = sign;
        this.body = body;
    }

    public ApiVerifyOrder(@NotNull ApiMessageContext apiRequestContext) {
        this(apiRequestContext.getAccessKey(),
                apiRequestContext.getSign(),
                apiRequestContext.getBody());
        this.gid(apiRequestContext.getGid());
        this.partnerId(apiRequestContext.getPartnerId());
    }
}
