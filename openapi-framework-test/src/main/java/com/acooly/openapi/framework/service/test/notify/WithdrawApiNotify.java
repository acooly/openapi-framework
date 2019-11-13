package com.acooly.openapi.framework.service.test.notify;

import com.acooly.core.utils.Money;
import com.acooly.core.utils.enums.ResultStatus;
import com.acooly.openapi.framework.common.annotation.OpenApiField;
import com.acooly.openapi.framework.common.message.ApiNotify;
import com.acooly.openapi.framework.service.test.request.WithdrawApiRequest;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
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
public class WithdrawApiNotify extends ApiNotify {

    @NotBlank
    @Size(max = 64)
    @OpenApiField(desc = "订单号", constraint = "商户订单号，交易唯一标志", demo = "20912213123sdf", ordinal = 1)
    private String merchOrderNo;

    @NotBlank
    @Length(max = 20, min = 20, message = "会员编码，固定长度为20字节")
    @OpenApiField(desc = "会员编码", constraint = "提现用户ID", demo = "20198982938272827232", ordinal = 2)
    private String userId;

    @Length(max = 20, message = "账号最大长度为20字节")
    @OpenApiField(desc = "会员账号", constraint = "会员账号，如果为空则为主账号，与会员编码相同", demo = "20198982938272827232", ordinal = 3)
    private String accountNo;

    @NotNull
    @OpenApiField(desc = "到账金额", constraint = "到账金额为扣除手续费后的用户实际到银行卡的金额", demo = "199.40", ordinal = 4)
    private Money amountIn;

    @NotNull
    @OpenApiField(desc = "手续费", constraint = "本笔交易被扣除的手续费", demo = "200.15", ordinal = 5)
    private Money fee;

    @OpenApiField(desc = "到账方式", constraint = "到账方式,默认:T1", demo = "T1", ordinal = 6)
    @Length(max = 1, min = 1, message = "长度为1字符")
    private WithdrawApiRequest.DelayEnum delay = WithdrawApiRequest.DelayEnum.T1;

    @NotNull
    @Length(max = 16)
    @OpenApiField(desc = "提现处理结果", constraint = "同步通知时，可能为success或processing; 异步通知时为确定的最终状态值：success或failure",
            demo = "success", ordinal = 7)
    private ResultStatus status;

}
