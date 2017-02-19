///*
// * www.yiji.com Inc.
// * Copyright (c) 2016 All Rights Reserved
// */
//
///*
// * 修订记录:
// * kuli@yiji.com 2016-06-12 18:23 创建
// */
//package com.yiji.framework.openapi.sdk.test;
//
//import com.acooly.core.utils.Money;
//import com.acooly.core.utils.net.HttpResult;
//import com.acooly.core.utils.net.Https;
//import com.alibaba.fastjson.JSON;
//import com.google.common.collect.Maps;
//import com.yiji.framework.openapi.core.security.sign.SignTypeEnum;
//import com.yiji.framework.openapi.sdk.ApiSdkConstants;
//import com.yiji.framework.openapi.sdk.YijiPayClient;
//import com.yiji.framework.openapi.sdk.common.utils.IdGenerator;
//import com.yiji.framework.openapi.service.test.request.CreateOrderRequest;
//import com.yiji.framework.openapi.service.test.request.PayOrderRequest;
//import com.yiji.framework.openapi.service.test.request.TransactorQueryRequest;
//import com.yiji.framework.openapi.service.test.request.WithdrawRedirectRequest;
//import com.yiji.framework.openapi.service.test.response.CreateOrderResponse;
//import com.yiji.framework.openapi.service.test.response.PayOrderResponse;
//import com.yiji.framework.openapi.service.test.response.TransactorQueryResponse;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.test.context.ContextConfiguration;
//import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;
//import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
//
//import java.util.Map;
//
///**
// * @author acooly
// */
//@RunWith(SpringJUnit4ClassRunner.class)
//@ContextConfiguration("classpath*:/spring/yiji-openapi-sdk.xml")
//public class OpenApiSdkTest extends AbstractJUnit4SpringContextTests {
//
////    static {
////        System.setProperty("spring.profiles.active","dev");
////    }
//
//    @Autowired
//    private YijiPayClient yijiPayClient;
//
//
//    @Test
//    public void testCreateOrder() {
//        Money amount = Money.amout("1000.00");
//        CreateOrderRequest request = new CreateOrderRequest();
//        request.setMerchOrderNo("12345678901234567890");
//        request.setTitle("同步请求创建订单");
//        request.setAmount(amount);
//        request.setPayeeUserId("12345678900987654321");
//        request.setPayerUserId("09876543211234567890");
//        request.setBuyerUserId("09876543211234567890");
//        request.setBuyeryEmail("zhangpu@163.com");
//        request.setBuyeryMobileNo("13898765453");
//        request.setBuyerCertNo("330702194706165014");
//        request.setSignType(SignTypeEnum.MD5.toString());
//        request.setPassword("123456");
////        request.setContext("这是客户端参数:{userName:1,\"password\":\"12121\"}");
////        List<GoodInfo> goodInfos = Lists.newArrayList();
////        GoodInfo g = null;
////        for (int i = 1; i <= 2; i++) {
////            g = new GoodInfo();
////            g.setGoodType(GoodType.actual);
////            g.setName("天子精品" + i);
////            g.setPrice(Money.amout("400.00"));
////            g.setQuantity(1);
////            g.setReferUrl("http://acooly.cn/tianzi");
////            goodInfos.add(g);
////        }
////        request.setGoodsInfos(goodInfos);
//        CreateOrderResponse response = yijiPayClient.request(request);
//    }
//
//    @Test
//    public void testtransactorQuery() {
//        TransactorQueryRequest request = new TransactorQueryRequest();
//        String service = "transactorQuery";
//        request.setService(service);
//        request.setRequestNo(IdGenerator.newOID());
//        request.setPartnerId("20150409922615030003");
//        request.setMerchOrderNo(IdGenerator.newOID());
//        request.setUserId("16091818463208300003");
//        TransactorQueryResponse response = yijiPayClient.request(request);
//        System.out.println("[transactorQuery]响应报文："+ JSON.toJSON(response).toString());
//    }
//    @Test
//    public void testPayOrder() throws Exception {
//        Money amount = Money.amout("1000.00");
//        PayOrderRequest request = new PayOrderRequest();
//        request.setMerchOrderNo("12345678901234567890");
//        request.setAmount(amount);
//        request.setPayerUserId("09876543211234567890");
//        PayOrderResponse response = yijiPayClient.request(request);
//    }
//
//    @Test
//    public void testPayApiNotify() throws Exception {
//        Map<String, String> postData = Maps.newHashMap();
//        postData.put("gid", "G0010000000016061422395915000000");
//        postData.put("partnerId", ApiSdkConstants.PARTNERID);
//        postData.put("merchOrderNo", "12345678901234567890");
//        postData.put("tradeNo", "123456789");
//        postData.put("amount", "111111");
//        HttpResult result = Https.getInstance().post("http://localhost:8090/notify/sender", postData, "utf-8");
//    }
//    @Test
//    public void testwithdrawRedirect() throws Exception {
//        WithdrawRedirectRequest request = new WithdrawRedirectRequest();
//        String service = "withdrawRedirect";
//        // 基本参数
//        request.setService(service);
//        request.setRequestNo(IdGenerator.newOID());
//        request.setMerchOrderNo(IdGenerator.newOID());
//
//        request.setUserId("16091818463208300003");
//        request.setAccountNo("16091818463208300003");
//        request.setAmount(Money.cent(15152));
//        request.setNotifyUrl("http://127.0.0.0/test");
//
//        String redirectUrl = yijiPayClient.redirect(request);
//        System.out.println("[withdrawRedirect]跳转地址："+redirectUrl);
//    }
//
//}
