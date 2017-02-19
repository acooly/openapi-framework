/*
 * www.yiji.com Inc.
 * Copyright (c) 2014 All Rights Reserved
 */
package com.yiji.framework.openapi.demo.server.service;

import com.yiji.framework.openapi.core.meta.OpenApiService;
import com.yiji.framework.openapi.core.service.base.BaseApiService;
import com.yiji.framework.openapi.core.service.enums.ResponseType;
import com.yiji.framework.openapi.demo.server.message.request.WithdrawRequest;
import com.yiji.framework.openapi.demo.server.message.response.WithdrawResponse;
import org.apache.commons.codec.digest.DigestUtils;

/**
 * @author zhangpu
 * @date 2014年7月29日
 */
@OpenApiService(name = "withdraw", desc = "提现", responseType = ResponseType.SYN)
public class WithdrawApiService extends
		BaseApiService<WithdrawRequest, WithdrawResponse> {

	@Override
	protected void doService(WithdrawRequest request, WithdrawResponse response) {
		response.setTradeNo(DigestUtils.md5Hex(request.getRequestNo()));
	}
}
