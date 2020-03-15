/*
 * acooly.com Inc.
 * Copyright (c) 2014 All Rights Reserved
 */
package com.acooly.openapi.test.openapitools.message;

import com.acooly.core.utils.Money;
import com.acooly.openapi.framework.common.message.ApiAsyncRequest;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * @author zhangpu
 * @date 2014年7月29日
 */
@Getter
@Setter
public class WithdrawApiRequest extends ApiAsyncRequest {

    @NotBlank
    @Size(max = 64)
    private String merchOrderNo;

    @NotBlank
    @Size(max = 20, min = 20, message = "会员编码，固定长度为20字节")
    private String userId;

    @Size(max = 20, message = "账号最大长度为20字节")
    private String accountNo;

    @NotNull(message = "提现金额是必选项，格式为保留两位小数的元，如: 2.00,200.05")
    private Money amount;

    @NotNull
    private String delay = "T1";
    
}
