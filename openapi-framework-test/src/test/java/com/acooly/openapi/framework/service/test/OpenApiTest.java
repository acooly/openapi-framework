package com.acooly.openapi.framework.service.test;

import com.acooly.core.utils.Money;
import com.acooly.openapi.framework.common.enums.ApiServiceResultCode;
import com.acooly.openapi.framework.core.test.AbstractApiServieTests;
import com.acooly.openapi.framework.service.test.dto.GoodInfo;
import com.acooly.openapi.framework.service.test.enums.GoodType;
import com.acooly.openapi.framework.service.test.request.CreateOrderRequest;
import com.acooly.openapi.framework.service.test.request.PayOrderRequest;
import com.acooly.openapi.framework.service.test.request.WithdrawRequest;
import com.acooly.openapi.framework.service.test.response.CreateOrderResponse;
import com.acooly.openapi.framework.service.test.response.PayOrderResponse;
import com.acooly.openapi.framework.service.test.response.WithdrawResponse;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

/** @author qiubo@yiji.com */
@Slf4j
public class OpenApiTest extends AbstractApiServieTests {
  @Test
  public void testSync() throws Exception {
    CreateOrderRequest request = new CreateOrderRequest();
    request.setRequestNo(UUID.randomUUID().toString());
    request.setService("createOrder");
    request.setTitle("同步请求创建订单");
    request.setPayeeUserId("12345678900987654321");
    request.setPayerUserId("09876543211234567890");
    request.setBuyerUserId("09876543211234567890");
    request.setBuyeryEmail("qiuboboy@qq.com");
    request.setBuyeryMobileNo("13898765453");
    request.setBuyerCertNo("330702194706165014");
    request.setPassword(encrypt("12312312"));
    request.setContext("123");
    List<GoodInfo> goodInfos = Lists.newArrayList();
    for (int i = 1; i <= 2; i++) {
      GoodInfo goodInfo = new GoodInfo();
      goodInfo.setGoodType(GoodType.actual);
      goodInfo.setName("天子精品" + i);
      goodInfo.setPrice(Money.amout("400.00"));
      goodInfo.setQuantity(1);
      goodInfo.setReferUrl("http://acooly.cn/tianzi");
      goodInfos.add(goodInfo);
    }
    request.setGoodsInfos(goodInfos);
    CreateOrderResponse response = request(request, CreateOrderResponse.class);
    log.info("{}", response);
    assertThat(response).isNotNull();
    assertThat(response.isSuccess()).isTrue();
  }

  @Test
  public void testRedirect() throws Exception {
    WithdrawRequest request = new WithdrawRequest();
    request.setRequestNo(UUID.randomUUID().toString());
    request.setService("withdraw");
    request.setAmount(new Money("100"));
    WithdrawResponse response = request(request, WithdrawResponse.class);
    log.info("{}", response);
    assertThat(response).isNotNull();
    assertThat(response.isSuccess()).isFalse();
    assertThat(response.getCode()).isEqualTo(ApiServiceResultCode.PARAMETER_ERROR.code());

    request.setNotifyUrl("http://www.baidu.com");
    request.setReturnUrl("http://www.baidu.com");
    response = request(request, WithdrawResponse.class);
    assertThat(response).isNotNull();
    assertThat(response.isSuccess()).isTrue();
  }

  @Test
  public void testNotify() throws Exception {
    PayOrderRequest request = new PayOrderRequest();
    request.setRequestNo(UUID.randomUUID().toString());
    request.setService("payOrder");
    request.setAmount(new Money("100"));
    request.setPayerUserId("xxxxxx");
    request.setNotifyUrl("http://www.baidu.com");
    PayOrderResponse response = request(request, PayOrderResponse.class);
    log.info("{}", response);
    assertThat(response).isNotNull();
    assertThat(response.isSuccess()).isTrue();
    assertThat(response.getCode()).isEqualTo(ApiServiceResultCode.PROCESSING.code());
  }
}
