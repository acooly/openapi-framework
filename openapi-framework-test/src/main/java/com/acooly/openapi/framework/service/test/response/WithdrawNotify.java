package com.acooly.openapi.framework.service.test.response;

import com.acooly.core.utils.Money;
import com.acooly.openapi.framework.common.annotation.OpenApiField;
import com.acooly.openapi.framework.common.message.ApiNotify;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;

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
    @Length(max = 32)
    @OpenApiField(desc = "用户UserId", constraint = "用户UserId")
    private String userId;

    @NotNull
    @OpenApiField(desc = "交易金额", constraint = "交易金额，单位：元")
    private Money amount;

    @Length(max = 32)
    @OpenApiField(desc = "提现账户", constraint = "提现账户")
    private String accountNo;

    @NotNull
    @Length(min = 20, max = 64)
    @OpenApiField(desc = "全站统一流水号")
    private String gid;

}
