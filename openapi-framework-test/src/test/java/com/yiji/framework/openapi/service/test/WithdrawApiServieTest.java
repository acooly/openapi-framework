/*
 * www.yiji.com Inc.
 * Copyright (c) 2014 All Rights Reserved
 */
package com.yiji.framework.openapi.service.test;

import java.util.Map;

import org.junit.Test;

import com.acooly.core.utils.Money;
import com.acooly.core.utils.net.HttpResult;
import com.yiji.framework.openapi.common.utils.json.JsonMarshallor;
import com.yiji.framework.openapi.core.test.AbstractApiServieTests;
import com.yiji.framework.openapi.service.test.request.WithdrawRequest;
import com.yiji.framework.openapi.service.test.response.WithdrawResponse;

/**
 * @author zhangpu
 * @date 2014年7月30日
 */
public class WithdrawApiServieTest extends AbstractApiServieTests {
	{
		gatewayUrl = "http://localhost:8080/gateway.html";
		key = "1234567890987654321";
		partnerId = "20140411020055684571";
		version = null;
	}

	@Test
	public void testReuqest() {
		service = "withdraw";
		String userId = "12345678901234567890";
		Money amount = Money.amout("1000.00");
		WithdrawRequest request = new WithdrawRequest(userId, amount, "ABC",
				"1234123412341234", "0");
		Map<String, String> map = marshall(request);
		map.put("busiType", "T0");
		map.remove("version");

		HttpResult result = post(map);
		WithdrawResponse response = JsonMarshallor.INSTANCE.parse(
				result.getBody(), WithdrawResponse.class);
		System.out.println(response);
	}

}
