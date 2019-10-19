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
public class ApiSignOrder extends OrderBase {

    /**
     * 商户访问码
     * 默认可为parentId
     */
    @NotBlank
    private String accessKey;
    /**
     * 签名算法
     */
    @NotNull
    private SignTypeEnum signType = SignTypeEnum.MD5;


    /**
     * 报文（被验证的签名字符串）
     */
    @NotBlank
    private String body;

    public ApiSignOrder() {
    }

    public ApiSignOrder(@NotBlank String accessKey, @NotBlank String body) {
        this.accessKey = accessKey;
        this.body = body;
    }

    public ApiSignOrder(@NotNull ApiMessageContext apiRequestContext) {
        this(apiRequestContext.getAccessKey(),
                apiRequestContext.getBody());
        this.gid(apiRequestContext.getGid());
        this.partnerId(apiRequestContext.getPartnerId());
    }
}
