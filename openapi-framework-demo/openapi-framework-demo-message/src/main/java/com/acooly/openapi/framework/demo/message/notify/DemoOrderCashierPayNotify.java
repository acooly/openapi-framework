package com.acooly.openapi.framework.demo.message.notify;

import com.acooly.core.utils.Money;
import com.acooly.openapi.framework.common.annotation.OpenApiField;
import com.acooly.openapi.framework.common.message.ApiNotify;
import com.acooly.openapi.framework.demo.message.enums.OrderPayStatus;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * @author zhangpu
 * @date 2016/2/12
 */
@Getter
@Setter
public class DemoOrderCashierPayNotify extends ApiNotify {

    @NotBlank
    @Size(max = 64)
    @OpenApiField(desc = "订单号", constraint = "商户订单号，唯一标志一笔交易", demo = "20912213123sdf", ordinal = 1)
    private String merchOrderNo;

    @NotNull
    @OpenApiField(desc = "支付金额", ordinal = 2)
    private Money amount;

    @NotBlank
    @OpenApiField(desc = "买家用户ID", demo = "1212121212", ordinal = 3)
    private String payerUserId;

    @NotNull
    @OpenApiField(desc = "状态", ordinal = 4)
    private OrderPayStatus payStatus;


}
