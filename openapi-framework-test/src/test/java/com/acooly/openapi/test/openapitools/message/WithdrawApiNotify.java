package com.acooly.openapi.test.openapitools.message;

import com.acooly.core.utils.Money;
import com.acooly.core.utils.enums.ResultStatus;
import com.acooly.openapi.framework.common.message.ApiNotify;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * 提现通知报文
 *
 * @author zhangpu
 * @date 2019-01-30
 */
@Getter
@Setter
public class WithdrawApiNotify extends ApiNotify {

    @NotBlank
    @Size(max = 64)
    private String merchOrderNo;

    @NotBlank
    @Size(max = 20, min = 20, message = "会员编码，固定长度为20字节")
    private String userId;

    @Size(max = 20, message = "账号最大长度为20字节")
    private String accountNo;

    @NotNull
    private Money amountIn;

    @NotNull
    private Money fee;

    private String delay = "T1";

    @NotNull
    @Size(max = 16)
    private ResultStatus status;

}
