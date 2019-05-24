package com.acooly.openapi.framework.service.test.request;

import com.acooly.core.utils.Money;
import com.acooly.openapi.framework.common.annotation.OpenApiField;
import com.acooly.openapi.framework.common.message.ApiAsyncRequest;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * 收银台跳转支付 请求报文
 *
 * @author zhangpu
 * @date 2019-1-28
 */
@Getter
@Setter
public class OrderCashierPayRequest extends ApiAsyncRequest {

    @NotEmpty
    @Size(max = 64)
    @OpenApiField(desc = "订单号", constraint = "商户订单号，唯一标志一笔交易", demo = "20912213123sdf", ordinal = 1)
    private String merchOrderNo;

    @NotNull
    @OpenApiField(desc = "支付金额", demo = "200.01", ordinal = 2)
    private Money amount;

    @NotEmpty
    @OpenApiField(desc = "买家用户ID", demo = "23412341234", ordinal = 3)
    private String payerUserId;

}
