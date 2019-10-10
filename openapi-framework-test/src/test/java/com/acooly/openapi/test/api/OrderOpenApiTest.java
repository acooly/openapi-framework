package com.acooly.openapi.test.api;

import com.acooly.core.utils.Ids;
import com.acooly.core.utils.Money;
import com.acooly.openapi.framework.common.enums.ApiProtocol;
import com.acooly.openapi.framework.core.log.SafetyLog;
import com.acooly.openapi.framework.core.test.AbstractApiServieTests;
import com.acooly.openapi.framework.service.test.dto.GoodInfo;
import com.acooly.openapi.framework.service.test.enums.GoodType;
import com.acooly.openapi.framework.service.test.request.OrderCreateApiRequest;
import com.acooly.openapi.framework.service.test.response.OrderCreateApiResponse;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * 以order为案例，测试同步，异步和跳转
 *
 * @author zhangpu
 * @date 2019-01-27
 */
@Slf4j
public class OrderOpenApiTest extends AbstractApiServieTests {

    static String content = UUID.randomUUID().toString();


    String payerUserId = "09876543211234567890";
    Money amount = Money.amout("200.01");


    @Test
    public void testSafetyProperties() {
        System.out.println(SafetyLog.getSafetyProperties(OrderCreateApiRequest.class));
    }

    /**
     * 同步请求(JSON协议)
     * <p>
     * 1. 请求参数序列化为json，放在http body中传输
     * 2. 安全校验相关参数支持放在http header或者url中
     * 3. 响应安全校验信息放在http header中
     * 4. 响应体为json
     */
    @Test
    public void testOrderCreateSync() throws Exception {
        OrderCreateApiRequest request = new OrderCreateApiRequest();
        // 可修改协议，目前支持：HTTP_FORM_JSON，JSON
        request.setProtocol(ApiProtocol.HTTP_FORM_JSON);
        request.setRequestNo(Ids.RandomNumberGenerator.getNewString(20));
        request.setMerchOrderNo(Ids.RandomNumberGenerator.getNewString(20));
        request.setService("orderCreate");
        request.setTitle("同步请求创建订单\uD83D\uDC3E一休哥\uD83D\uDC3E ");
        request.setAmount(amount);
        request.setPayeeUserId("12345678900987654321");
        request.setPayerUserId(payerUserId);
        request.setBuyerUserId("09876543211234567890");
        request.setBuyeryEmail("qiuboboy@qq.com");
        request.setBuyeryMobileNo("13898765453");
        request.setBuyerCertNo("330702194706165014");
        request.setPassword("12312312");
        request.setContext(content);
        List<GoodInfo> goodInfos = Lists.newArrayList();
        GoodInfo goodInfo = new GoodInfo();
        goodInfo.setGoodType(GoodType.actual);
        goodInfo.setName("天子精品");
        goodInfo.setPrice(Money.amout("400.00"));
        goodInfo.setReferUrl("http://acooly.cn/tianzi");
        goodInfos.add(goodInfo);

        request.setGoodsInfos(goodInfos);
        request.ext("xx", "oo");
        OrderCreateApiResponse response = request(request, OrderCreateApiResponse.class);
        log.info("{}", response);
        log.info("订单号: {}", request.getMerchOrderNo());
        assertThat(response).isNotNull();
        assertThat(response.isSuccess()).isTrue();
        assertThat(response.getContext()).isEqualTo(content);
    }

}
