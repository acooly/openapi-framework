/*
 * acooly.com Inc.
 * Copyright (c) 2014 All Rights Reserved
 */
package com.acooly.openapi.framework.service.test;

import com.acooly.core.utils.Money;
import com.acooly.core.utils.net.HttpResult;
import com.acooly.core.utils.net.Https;
import com.acooly.openapi.framework.service.test.dto.GoodInfo;
import com.acooly.openapi.framework.service.test.enums.GoodType;
import com.acooly.openapi.framework.service.test.request.CreateOrderRequest;
import com.acooly.openapi.framework.service.test.request.PayOrderRequest;
import com.acooly.openapi.framework.service.test.response.CreateOrderResponse;
import com.acooly.openapi.framework.service.test.response.PayOrderResponse;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.acooly.openapi.framework.common.ApiConstants;
import com.acooly.openapi.framework.core.security.sign.SignTypeEnum;
import com.acooly.openapi.framework.core.test.AbstractApiServieTests;
import org.junit.Test;

import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * @author zhangpu
 */
public class OrderPayApiServiceTest extends AbstractApiServieTests {
    {
        gatewayUrl = "http://localhost:8089/gateway.html";
        key = "06f7aab08aa2431e6dae6a156fc9e0b4";
        partnerId = "test";
        notifyUrl = "http://127.0.0.1:8090/notify/receiver";
        version = null;
        signType = null;
    }

    @Test
    public void testCreateOrder() throws Exception {
        service = "createOrder";
        Money amount = Money.amout("1000.00");
        CreateOrderRequest request = new CreateOrderRequest();
        request.setRequestNo(UUID.randomUUID().toString());
        request.setMerchOrderNo("1234567890=-09876543");
        request.setTitle("同步请求创建订单");
        request.setAmount(amount);
        request.setPayeeUserId("12345678900987654321");
        request.setPayerUserId("09876543211234567890");
        request.setBuyerUserId("09876543211234567890");
        request.setBuyeryEmail("zhangpu@163.com");
        request.setBuyeryMobileNo("13898765453");
        request.setBuyerCertNo("330702194706165014");
        request.setSignType(SignTypeEnum.MD5.toString());
        request.setPassword(encrypt("12312312"));
        request.setContext("这是客户端参数:{userName:1,\"password\":\"12121\"}");
        List<GoodInfo> goodInfos = Lists.newArrayList();
        GoodInfo g = null;
        for (int i = 1; i <= 2; i++) {
            g = new GoodInfo();
            g.setGoodType(GoodType.actual);
            g.setName("天子精品" + i);
            g.setPrice(Money.amout("400.00"));
            g.setQuantity(1);
            g.setReferUrl("http://acooly.cn/tianzi");
            goodInfos.add(g);
        }
        request.setGoodsInfos(goodInfos);
        request(request, CreateOrderResponse.class, new ApiTestHandler() {
            @Override
            public Map<String, String> afterMarshall(Map<String, String> requestData) {
                requestData.remove(ApiConstants.SIGN_TYPE);
                requestData.remove(ApiConstants.VERSION);
                requestData.remove(ApiConstants.PROTOCOL);
                logger.info("ApiTestHandler:{}",requestData);
                return requestData;
            }
        });
    }

    @Test
    public void testPayOrder() throws Exception {
        service = "payOrder";
        Money amount = Money.amout("1000.00");
        PayOrderRequest request = new PayOrderRequest();
        request.setRequestNo(UUID.randomUUID().toString());
        request.setMerchOrderNo("1234567890=-09876543");
        request.setAmount(amount);
        request.setPayerUserId("09876543211234567890");
        request.setSignType(SignTypeEnum.MD5.toString());
        request.setContext("这是客户端参数:{userName:1,\"password\":\"12121\"}");
        request.setNotifyUrl(notifyUrl);
        request(request, PayOrderResponse.class);
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
        HttpResult result = Https.getInstance().post("http://localhost:8090/notify/sender", postData, "utf-8");
    }

}
