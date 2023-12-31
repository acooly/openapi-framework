package com.acooly.openapi.test.api;

import com.acooly.core.utils.Ids;
import com.acooly.core.utils.Money;
import com.acooly.core.utils.system.IPUtil;
import com.acooly.openapi.framework.common.enums.DeviceType;
import com.acooly.openapi.framework.common.message.builtin.LoginRequest;
import com.acooly.openapi.framework.demo.message.dto.GoodsInfo;
import com.acooly.openapi.framework.demo.message.enums.GoodType;
import com.acooly.openapi.framework.demo.message.request.DemoOrderCashierPayApiRequest;
import com.acooly.openapi.framework.demo.message.request.DemoOrderCreateApiRequest;
import com.acooly.openapi.framework.demo.message.request.DemoWithdrawApiRequest;
import com.google.common.collect.Lists;

import java.util.List;
import java.util.UUID;

/**
 * 常规OpenApi基准测试和验证
 *
 * @author zhangpu
 * @date 2019-11-14
 */
public class OpenApiBenchmarkTestUtils {

    public static String CONTEXT = UUID.randomUUID().toString();
    public static String PAYER_USER_ID = "09876543211234567890";
    public static Money AMOUNT = Money.amout("200.01");

    /**
     * 客户端通知地址
     */
    public static String TEST_NOTIFY_URL = "http://127.0.0.1:8089/openapi/test/withdraw/client/notifyUrl.html";


    public static DemoOrderCreateApiRequest buildOrderCreateApiRequest() {
        DemoOrderCreateApiRequest request = new DemoOrderCreateApiRequest();
        request.setRequestNo(Ids.RandomNumberGenerator.getNewString(20));
        request.setMerchOrderNo(Ids.RandomNumberGenerator.getNewString(20));
        request.setService("demoOrderCreate");
        String specString = "同步请求创建订单\uD83D\uDC3E一休哥\uD83D\uDC3E ";
        request.setTitle(specString);
        request.setAmount(AMOUNT);
        request.setPayeeUserId("12345678900987654321");
        request.setPayerUserId(PAYER_USER_ID);
        request.setBuyerUserId("09876543211234567890");
        request.setBuyeryEmail("qiuboboyqq.com");
        request.setBuyeryMobileNo("13898765430");
        request.setBuyerCertNo("411525198805116966");
        request.setPassword("12312312");
        request.setContext(CONTEXT);
        List<GoodsInfo> goodsInfos = Lists.newArrayList();
        GoodsInfo goodsInfo = new GoodsInfo();
        goodsInfo.setGoodType(GoodType.actual);
        goodsInfo.setName("天子精品");
        goodsInfo.setPrice(Money.amout("400.00"));
        goodsInfo.setReferUrl("http://acooly.cn/tianzi");
        goodsInfos.add(goodsInfo);
        request.setGoodsInfos(goodsInfos);
        request.ext("xx", "oo");
        return request;
    }


    public static LoginRequest buildLoginRequest() {
        LoginRequest request = new LoginRequest();
        request.setUsername("zhangpu");
        request.setPassword("11111111");
        request.setChannel("test");
        request.setDeviceId(UUID.randomUUID().toString());
        request.setDeviceType(DeviceType.IPHONE);
        request.setDeviceModel("AAA");
        request.setCustomerIp(IPUtil.getFirstNoLoopbackIPV4Address());
        return request;
    }

    public static DemoWithdrawApiRequest buildWithdrawApiRequest() {
        DemoWithdrawApiRequest request = new DemoWithdrawApiRequest();
        request.setRequestNo(Ids.RandomNumberGenerator.getNewString(20));
        request.setMerchOrderNo(Ids.RandomNumberGenerator.getNewString(20));
        request.setUserId(request.getMerchOrderNo());
        request.setService("demoWithdraw");
        request.setAmount(AMOUNT);
        request.setDelay(DemoWithdrawApiRequest.DelayEnum.T1);
        request.setContext("会话信息，透传返回");
        request.ext("xx", "oo");
        request.setNotifyUrl(TEST_NOTIFY_URL);
        return request;
    }

    public static DemoOrderCashierPayApiRequest buildOrderCashierPayApiRequest() {
        DemoOrderCashierPayApiRequest apiRequest = new DemoOrderCashierPayApiRequest();
        apiRequest.setRequestNo(getTestId());
        apiRequest.setService("demoOrderCashierPay");
        apiRequest.setMerchOrderNo(getTestId());
        apiRequest.setAmount(AMOUNT);
        apiRequest.setPayerUserId(PAYER_USER_ID);
        apiRequest.setReturnUrl("http://127.0.0.1:8089/openapi/test/orderCashierPay/client/returnUrl.html");
        apiRequest.setNotifyUrl("http://127.0.0.1:8089/openapi/test/orderCashierPay/client/notifyUrl.html");
        return apiRequest;
    }


    public static String getTestId() {
        return Ids.RandomNumberGenerator.getNewString(20);
    }

}
