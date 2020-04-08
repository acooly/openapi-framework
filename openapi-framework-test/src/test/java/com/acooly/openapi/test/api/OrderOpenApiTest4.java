/*
 * www.yiji.com Inc.
 * Copyright (c) 2014 All Rights Reserved
 */
package com.acooly.openapi.test.api;

import com.acooly.core.utils.Ids;
import com.acooly.core.utils.Money;
import com.acooly.openapi.framework.core.test.AbstractApiServieTests4;
import com.acooly.openapi.framework.demo.message.dto.GoodsInfo;
import com.acooly.openapi.framework.demo.message.enums.GoodType;
import com.acooly.openapi.framework.demo.message.request.DemoOrderCreateApiRequest;
import com.acooly.openapi.framework.demo.message.response.DemoOrderCreateApiResponse;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author zhangpu
 */
@Slf4j
public class OrderOpenApiTest4 extends AbstractApiServieTests4 {

    String content = UUID.randomUUID().toString();
    String payerUserId = "09876543211234567890";
    Money amount = Money.amout("200.01");

    @Test
    public void testCreateOrder() throws Exception {
        service = "createOrder";
        DemoOrderCreateApiRequest request = new DemoOrderCreateApiRequest();
        request.setRequestNo(Ids.getDid());
        request.setMerchOrderNo(Ids.getDid());
        request.setService("orderCreate");
        request.setTitle("同步请求创建订单\uD83D\uDC3E一休哥\uD83D\uDC3E ");
        request.setAmount(amount);
        request.setPayeeUserId("12345678900987654321");
        request.setPayerUserId(payerUserId);
        request.setBuyerUserId("09876543211234567890");
        request.setBuyeryEmail("qiuboboy@qq.com");
        request.setBuyeryMobileNo("13898765453");
        request.setBuyerCertNo("330702194706165014");
        request.setPassword(encrypt("12312312"));
        request.setContext(content);
        List<GoodsInfo> goodsInfos = Lists.newArrayList();
        GoodsInfo goodsInfo = new GoodsInfo();
        goodsInfo.setGoodType(GoodType.actual);
        goodsInfo.setName("天子精品");
        goodsInfo.setPrice(Money.amout("400.00"));
        goodsInfo.setReferUrl("http://acooly.cn/tianzi");
        goodsInfos.add(goodsInfo);

        request.setGoodsInfos(goodsInfos);
//        request.ext("xx", "oo");
        DemoOrderCreateApiResponse response = request(request, DemoOrderCreateApiResponse.class);
        log.info("{}", response);
        log.info("订单号: {}", request.getMerchOrderNo());
        assertThat(response).isNotNull();
        assertThat(response.isSuccess()).isTrue();
        assertThat(response.getContext()).isEqualTo(content);
    }


}
