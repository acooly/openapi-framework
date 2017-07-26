package com.acooly.openapi.framework.service.test.request;

import com.acooly.core.utils.Money;
import com.acooly.openapi.framework.common.annotation.OpenApiField;
import com.acooly.openapi.framework.common.annotation.OpenApiMessage;
import com.acooly.openapi.framework.common.enums.ApiMessageType;
import com.acooly.openapi.framework.common.exception.ApiServiceException;
import com.acooly.openapi.framework.common.exception.OrderCheckException;
import com.acooly.openapi.framework.common.message.ApiRequest;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;

/**
 * 跳转提现页面
 *
 * @author cuifuqiang
 *
 */
@OpenApiMessage(service = "redirectWithdraw", type = ApiMessageType.Request)
public class WithdrawRedirectRequest extends ApiRequest {

	@NotEmpty
	@Length(max = 32)
	@OpenApiField(desc = "用户UserId", constraint = "用户UserId")
	private String userId;

	@OpenApiField(desc = "用户账户", constraint = "未指定,默认使用结算账户")
	private String accountNo;

	@NotNull
	@OpenApiField(desc = "交易金额", constraint = "交易金额，单位：元")
	private Money amount;

	/**
	 * 参数校验,校验失败请抛出OrderCheckException
	 */
	public void check() throws OrderCheckException {
		String orderNo = getMerchOrderNo();
		if (StringUtils.isBlank(orderNo)) {
			throw new ApiServiceException("TRADE_ORDER_CREATE_ERROR", "orderNo:交易订单号不能为空");
		}
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
