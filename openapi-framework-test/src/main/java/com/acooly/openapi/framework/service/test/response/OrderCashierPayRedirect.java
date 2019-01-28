package com.acooly.openapi.framework.service.test.response;

import com.acooly.core.utils.Money;
import com.acooly.openapi.framework.common.annotation.OpenApiField;
import com.acooly.openapi.framework.common.message.ApiResponse;
import com.acooly.openapi.framework.service.test.enums.OrderPayStatus;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * 收银台支付 跳转到下层服务的报文
 *
 * @author zhangpu
 * @date 2019-01-28 15:03
 */
@Getter
@Setter
public class OrderCashierPayRedirect extends ApiResponse {

    @NotEmpty
    @Size(max = 64)
    @OpenApiField(desc = "订单号", constraint = "商户订单号，唯一标志一笔交易", demo = "20912213123sdf")
    private String merchOrderNo;

    @NotNull
    @OpenApiField(desc = "支付金额")
    private Money amount;

    @NotEmpty
    @OpenApiField(desc = "买家用户ID")
    private String payerUserId;

}
