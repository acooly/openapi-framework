/**
 * openapi-client-demo
 * <p>
 * Copyright 2014 Acooly.cn, Inc. All rights reserved.
 *
 * @author zhangpu
 * @date 2020-03-05 11:07
 */
package com.acooly.openapi.test.openapitools;

import com.acooly.core.utils.Ids;
import com.acooly.core.utils.Money;
import com.acooly.openapi.framework.common.ApiConstants;
import com.acooly.openapi.framework.common.OpenApiTools;
import com.acooly.openapi.framework.common.message.ApiRequest;
import com.acooly.openapi.framework.common.message.ApiResponse;
import com.acooly.openapi.test.openapitools.message.*;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

/**
 * @author zhangpu
 * @date 2020-03-05 11:07
 */
@Slf4j
public class OpenApiToolsTest {

    private String gateway = "http://127.0.0.1:8089/gateway.do";
    private String accessKey = ApiConstants.TEST_ACCESS_KEY;
    private String secretKey = ApiConstants.TEST_SECRET_KEY;

    OpenApiTools openApiTools = new OpenApiTools(gateway, accessKey, secretKey);

    /**
     * 测试: 同步简单接口
     * <p>
     * login（动态秘钥）
     */
    @Test
    public void testLogin() {
        LoginApiRequest apiRequest = new LoginApiRequest();
        apiRequest.setUsername("zhangpu");
        apiRequest.setPassword(openApiTools.encrypt("Ab112121212"));
        LoginApiResponse apiResponse = openApiTools.send(apiRequest, LoginApiResponse.class);
        log.info(JSON.toJSONString(apiResponse));
    }

    /**
     * 测试：特殊字符报文
     */
    @Test
    public void testSimpleInfo() {
        String specialStr = "😁🀀εǚ☏©\uD83D\uDC3E ";
        ApiRequest request = new ApiRequest();
        request.setService("simpleInfo");
        request.setContext(specialStr);
        openApiTools.send(request, ApiResponse.class);
    }


    /**
     * 测试：分页查询
     */
    @Test
    public void testGoodsList() {
        GoodsListApiRequest apiRequest = new GoodsListApiRequest();
        // 页号
        apiRequest.setStart(1);
        // 页大小
        apiRequest.setLimit(5);
        GoodsListApiResponse apiResponse = openApiTools.send(apiRequest, GoodsListApiResponse.class);
        log.info("\n{}", JSON.toJSONString(apiResponse));
    }


    /**
     * 测试：复杂结构查询
     * 1、列表
     * 2、列表每个成员包含子列表
     * 3、树形结构
     * 4、列表成员包含子对象
     */
    @Test
    public void testTreeNodeList() {
        TreeNodeListApiRequest apiRequest = new TreeNodeListApiRequest();
        TreeNodeListApiResponse apiResponse = openApiTools.send(apiRequest, TreeNodeListApiResponse.class);
        log.info(JSON.toJSONString(apiResponse));
    }


    /**
     * 测试：异步接口（提现）
     *
     * 1、这里发送同步请求，收到同步响应（处理中）
     * 2、服务器网关端完成业务处理后，会发送（POST）异步通知结果给请求时传入的notifyUrl
     * 3、notifyUrl指向的是一个客户端的controller，进行接收处理，处理逻辑详见：WithdrawClientTestController
     * @see com.acooly.openapi.framework.service.test.web.WithdrawClientTestController
     */
    @Test
    public void testWithdrawAsync() {
        WithdrawApiRequest request = new WithdrawApiRequest();
        request.setRequestNo(Ids.RandomNumberGenerator.getNewString(20));
        request.setMerchOrderNo(Ids.RandomNumberGenerator.getNewString(20));
        request.setUserId(request.getMerchOrderNo());
        request.setService("withdraw");
        request.setAmount(Money.amout("100"));
        request.setDelay("T1");
        request.setContext("会话信息，透传返回");
        request.ext("xx", "oo");
        // 异步通知地址，该地址是Demo模块使用controller模拟的客户notifyUrl地址。
        // 因为是本地同一容器内验收，host和port可省，真实情况，请填写完整的URL
        // 这里提供一个controller来接受，处理后，打印结果到后台Console
        request.setNotifyUrl("http://127.0.0.1:8089/openapi/demo/withdraw/client/notifyUrl.html");
        WithdrawApiResponse response = openApiTools.send(request, WithdrawApiResponse.class);
        log.info("提现 申请成功，订单号: {}", request.getMerchOrderNo());
    }

}
