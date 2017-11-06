/*
 * acooly.com Inc.
 * Copyright (c) 2014 All Rights Reserved
 */
package com.acooly.openapi.framework.service.test;

import com.acooly.core.utils.Ids;
import com.acooly.core.utils.Money;
import com.acooly.core.utils.net.HttpResult;
import com.acooly.openapi.framework.common.utils.json.JsonMarshallor;
import com.acooly.openapi.framework.core.test.AbstractApiServieTests;
import com.acooly.openapi.framework.service.test.request.WithdrawRequest;
import com.acooly.openapi.framework.service.test.response.WithdrawResponse;
import org.junit.Test;

import java.util.Map;

/**
 * @author zhangpu
 * @date 2014年7月30日
 */
public class WithdrawApiServieTest extends AbstractApiServieTests {
  {
    gatewayUrl = "http://localhost:8089/gateway.html";
    key = "06f7aab08aa2431e6dae6a156fc9e0b4";
    partnerId = "test";
    version = null;
  }

  @Test
  public void testReuqest() {
    service = "withdraw";
    String userId = "12345678901234567890";
    Money amount = Money.amout("1000.00");
    WithdrawRequest request = new WithdrawRequest(userId, amount, "ABC", "1234123412341234", "0");
    request.setRequestNo(Ids.oid());
    Map<String, String> map = marshall(request);
    map.put("busiType", "T0");
    map.remove("version");

    HttpResult result = post(map);
    WithdrawResponse response =
        JsonMarshallor.INSTANCE.parse(result.getBody(), WithdrawResponse.class);
    System.out.println(response);
  }
}
