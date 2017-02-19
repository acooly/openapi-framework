/*
 * www.yiji.com Inc.
 * Copyright (c) 2014 All Rights Reserved
 */
package com.yiji.framework.openapi.service.test.response;

import com.yiji.framework.openapi.common.annotation.OpenApiField;
import com.yiji.framework.openapi.common.message.ApiResponse;
import org.hibernate.validator.constraints.Length;


/**
 * @author zhangpu
 * @date 2014年7月29日
 */
public class WithdrawResponse extends ApiResponse {

	/** 提现交易号 */
	@OpenApiField(desc = "提现交易号")
	@Length(max = 20, min = 20, message = "提现交易号长度为20字符")
	private String tradeNo;

	public String getTradeNo() {
		return tradeNo;
	}

	public void setTradeNo(String tradeNo) {
		this.tradeNo = tradeNo;
	}

}
