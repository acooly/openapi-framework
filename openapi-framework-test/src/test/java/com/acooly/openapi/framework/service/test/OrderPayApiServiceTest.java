/*
 * acooly.com Inc.
 * Copyright (c) 2014 All Rights Reserved
 */
package com.acooly.openapi.framework.service.test;

import com.acooly.core.utils.Money;
import com.acooly.core.utils.net.HttpResult;
import com.acooly.core.utils.net.Https;
import com.acooly.openapi.framework.core.test.AbstractApiServieTests;
import com.acooly.openapi.framework.service.test.request.PayOrderRequest;
import com.acooly.openapi.framework.service.test.response.PayOrderResponse;
import com.google.common.collect.Maps;
import org.junit.Test;

import java.util.Map;
import java.util.UUID;

/** @author zhangpu */
public class OrderPayApiServiceTest extends AbstractApiServieTests {
  {
    gatewayUrl = "http://localhost:8089/gateway.do";
    key = "c9cef22553af973d4b04a012f9cb8ea8";
    partnerId = "test";
    notifyUrl = "http://127.0.0.1:8090/notify/receiver";
    version = null;
    signType = null;
  }


  @Test
  public void testPayOrder() throws Exception {
    service = "payOrder";
    Money amount = Money.amout("1000.00");
    PayOrderRequest request = new PayOrderRequest();
    request.setRequestNo(UUID.randomUUID().toString());
    request.setAmount(amount);
    request.setPayerUserId("09876543211234567890");
    request.setContext("这是客户端参数:{userName:1,\"password\":\"12121\"}");
    PayOrderResponse orderResponse = request(request, PayOrderResponse.class);
    logger.info("{}", orderResponse);
  }

  @Test
  public void testPayApiNotify() throws Exception {
    Map<String, String> postData = Maps.newHashMap();
    postData.put("gid", "G0010000000016071213141202300000");
    postData.put("partnerId", partnerId);
    postData.put("orderNo", "411111111111111111131");

    postData.put("outOrderNo", "41111111111111111113");
    postData.put("tradeNo", "123456789");
    postData.put("amount", "111111");
    HttpResult result =
        Https.getInstance().post("http://localhost:8090/notify/sender", postData, "utf-8");
  }
}
