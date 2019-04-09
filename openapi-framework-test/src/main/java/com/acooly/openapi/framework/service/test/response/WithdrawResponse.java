/*
 * acooly.com Inc.
 * Copyright (c) 2014 All Rights Reserved
 */
package com.acooly.openapi.framework.service.test.response;

import com.acooly.openapi.framework.common.annotation.OpenApiField;
import com.acooly.openapi.framework.common.message.ApiResponse;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.Size;

/**
 * @author zhangpu
 * @date 2014年7月29日
 */
public class WithdrawResponse extends ApiResponse {

    @NotEmpty
    @Size(max = 64)
    @OpenApiField(desc = "订单号", constraint = "商户订单号，唯一标志一笔交易", demo = "20912213123sdf")
    private String merchOrderNo;

    /**
     * 提现交易号
     */
    @Length(max = 20, min = 20, message = "提现交易号长度为20字符")
    @OpenApiField(desc = "提现交易号", constraint = "提现的服务器端唯一编码", demo = "11111111112222222222")
    private String tradeNo;

    public String getTradeNo() {
        return tradeNo;
    }

    public void setTradeNo(String tradeNo) {
        this.tradeNo = tradeNo;
    }
}
