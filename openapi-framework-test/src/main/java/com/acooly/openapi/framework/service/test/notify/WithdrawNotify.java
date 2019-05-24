package com.acooly.openapi.framework.service.test.notify;

import com.acooly.core.utils.Money;
import com.acooly.core.utils.enums.ResultStatus;
import com.acooly.openapi.framework.common.annotation.OpenApiField;
import com.acooly.openapi.framework.common.message.ApiNotify;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * 跳转提现页面
 *
 * @author zhangpu
 * @date 2019-01-30
 */
@Getter
@Setter
public class WithdrawNotify extends ApiNotify {

    @NotEmpty
    @Size(max = 64)
    @OpenApiField(desc = "订单号", constraint = "商户订单号，唯一标志一笔交易", demo = "20912213123sdf", ordinal = 1)
    private String merchOrderNo;

    @NotEmpty
    @Length(max = 20, min = 20, message = "提现用户ID为必选项，长度为20字符")
    @OpenApiField(desc = "提现用户ID", constraint = "提现用户ID", demo = "20198982938272827232", ordinal = 2)
    private String userId;

    @NotEmpty
    @Length(max = 32)
    @OpenApiField(desc = "提现账户", constraint = "提现账户", demo = "12e1eqweqwe", ordinal = 3)
    private String accountNo;

    @NotNull(message = "提现金额是必选项，格式为保留两位小数的元，如: 2.00,200.05")
    @OpenApiField(desc = "提现金额", constraint = "提现金额，格式为保留两位小数的元，如: 2.00,200.05", demo = "200.15", ordinal = 4)
    private Money amount;

    @NotNull
    @Length(max = 16)
    @OpenApiField(desc = "提现处理结果", constraint = "同步通知时，可能为success或processing; 异步通知时为确定的最终状态值：success或failure",
            demo = "success", ordinal = 5)
    private ResultStatus status;

}
