/*
 * www.yiji.com Inc.
 * Copyright (c) 2016 All Rights Reserved
 */

/*
 * 修订记录:
 * kuli@yiji.com 2016-06-15 03:03 创建
 */
package com.yiji.framework.openapi.demo.client;

import com.acooly.core.utils.Money;
import com.acooly.core.utils.Profiles;
import com.google.common.collect.Lists;
import com.yiji.framework.openapi.sdk.OpenApiClient;
import com.yiji.framework.openapi.demo.server.message.dto.GoodInfo;
import com.yiji.framework.openapi.demo.server.message.enums.GoodType;
import com.yiji.framework.openapi.demo.server.message.request.CreateOrderRequest;
import com.yiji.framework.openapi.demo.server.message.response.CreateOrderResponse;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.List;

/**
 * @author acooly
 */
public class OpenApiSdkMainTest {

    static{
        Profiles.setProfile(Profiles.Profile.dev);
    }


    public static void main(String[] args) throws Exception{

        ApplicationContext context = new ClassPathXmlApplicationContext("classpath:spring/openapi/openapi-framework-sdk.xml");
        OpenApiClient openApiClient = context.getBean("openApiClient",OpenApiClient.class);

        Money amount = Money.amout("1000.00");
        CreateOrderRequest request = new CreateOrderRequest();
        request.setMerchOrderNo("12345678901234567890");
        request.setTitle("同步请求创建订单");
        request.setAmount(amount);
        request.setPayeeUserId("12345678900987654321");
        request.setPayerUserId("09876543211234567890");
        request.setBuyerUserId("09876543211234567890");
        request.setBuyeryEmail("zhangpu@163.com");
        request.setBuyeryMobileNo("13898765453");
        request.setBuyerCertNo("330702194706165014");
        request.setSignType("MD5");
        request.setPassword("123456");
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
        CreateOrderResponse response = openApiClient.request(request);

        System.out.println(response);

    }

}
