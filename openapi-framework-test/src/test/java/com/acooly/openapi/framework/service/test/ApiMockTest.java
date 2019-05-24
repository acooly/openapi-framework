package com.acooly.openapi.framework.service.test;

import com.acooly.core.utils.Money;
import com.acooly.openapi.framework.core.test.AbstractApiServieTests;
import com.acooly.openapi.framework.common.message.builtin.LoginRequest;
import com.acooly.openapi.framework.service.test.dto.GoodInfo;
import com.acooly.openapi.framework.service.test.enums.GoodType;
import com.acooly.openapi.framework.service.test.request.OrderCreateRequest;
import com.acooly.openapi.framework.service.test.response.OrderCreateResponse;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author qiuboboy@qq.com
 * @date 2018-08-14 16:44
 */
@Slf4j
public class ApiMockTest extends AbstractApiServieTests {
    static String content = UUID.randomUUID().toString();

    {
        gatewayUrl = "http://127.0.0.1:8089/gateway.mock";
    }

    @Test
    public void testSync() throws Exception {
        OrderCreateRequest request = new OrderCreateRequest();
        request.setRequestNo("2");
        request.setService("createOrder");
        request.setTitle("同步请求创建订单");
        request.setPayeeUserId("12345678900987654321");
        request.setPayerUserId("09876543211234567890");
        request.setBuyerUserId("09876543211234567890");
        request.setBuyeryEmail("qiuboboy1@qq.com");
        request.setBuyeryMobileNo("13898765453");
        request.setBuyerCertNo("330702194706165014");
        request.setPassword("12312312");
        request.setContext(content);
        List<GoodInfo> goodInfos = Lists.newArrayList();
        for (int i = 1; i <= 2; i++) {
            GoodInfo goodInfo = new GoodInfo();
            goodInfo.setGoodType(GoodType.actual);
            goodInfo.setName("天子精品" + i);
            goodInfo.setPrice(Money.amout("400.00"));
            goodInfo.setReferUrl("http://acooly.cn/tianzi");
            goodInfos.add(goodInfo);
        }
        request.setGoodsInfos(goodInfos);
        request.ext("xx", "oo");
        OrderCreateResponse response = request(request, OrderCreateResponse.class);
        log.info("{}", response);
        assertThat(response).isNotNull();
        assertThat(response.isSuccess()).isTrue();
    }

    @Test
    public void name() {

        LoginRequest loginRequest = new LoginRequest();
        log.info("{}", JSON.toJSONString(loginRequest, SerializerFeature.WriteNullStringAsEmpty, SerializerFeature.WriteNullNumberAsZero, SerializerFeature.WriteNullBooleanAsFalse));
    }

    @Test
    public void test1() {
        OrderCreateResponse response=new OrderCreateResponse();
    }
}
