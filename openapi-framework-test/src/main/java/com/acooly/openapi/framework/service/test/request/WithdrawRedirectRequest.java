package com.acooly.openapi.framework.service.test.request;

import com.acooly.core.common.exception.OrderCheckException;
import com.acooly.core.utils.Money;
import com.acooly.openapi.framework.common.annotation.OpenApiField;
import com.acooly.openapi.framework.common.exception.ApiServiceException;
import com.acooly.openapi.framework.common.message.ApiRequest;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;

/**
 * 跳转提现页面
 *
 * @author cuifuqiang
 */
public class WithdrawRedirectRequest extends ApiRequest {

    @NotEmpty
    @Length(max = 32)
    @OpenApiField(desc = "用户UserId", constraint = "用户UserId", demo = "1212121212", ordinal = 1)
    private String userId;

    @OpenApiField(desc = "用户账户", constraint = "未指定,默认使用结算账户", demo = "1212121221", ordinal = 2)
    private String accountNo;

    @NotNull
    @OpenApiField(desc = "交易金额", constraint = "交易金额，单位：元", ordinal = 3)
    private Money amount;

    /**
     * 参数校验,校验失败请抛出OrderCheckException
     */
    public void check() throws OrderCheckException {
        long amongCent = getAmount().getCent();
        if (amongCent <= 0) {
            throw new ApiServiceException("PARAMETER_ERROR", "交易金额必须大于0");
        }
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Money getAmount() {
        return amount;
    }

    public void setAmount(Money amount) {
        this.amount = amount;
    }

    public String getAccountNo() {
        return accountNo;
    }

    public void setAccountNo(String accountNo) {
        this.accountNo = accountNo;
    }
}
