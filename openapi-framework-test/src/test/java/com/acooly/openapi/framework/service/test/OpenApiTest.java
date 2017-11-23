package com.acooly.openapi.framework.service.test;

import com.acooly.core.utils.Money;
import com.acooly.openapi.framework.common.ApiConstants;
import com.acooly.openapi.framework.common.enums.ApiServiceResultCode;
import com.acooly.openapi.framework.common.login.LoginRequest;
import com.acooly.openapi.framework.common.login.LoginResponse;
import com.acooly.openapi.framework.core.test.AbstractApiServieTests;
import com.acooly.openapi.framework.service.test.api.LoginAssertApiService.LoginAssertRequest;
import com.acooly.openapi.framework.service.test.api.LoginAssertApiService.LoginAssertResponse;
import com.acooly.openapi.framework.service.test.dto.GoodInfo;
import com.acooly.openapi.framework.service.test.enums.GoodType;
import com.acooly.openapi.framework.service.test.request.CreateOrderRequest;
import com.acooly.openapi.framework.service.test.request.PayOrderRequest;
import com.acooly.openapi.framework.service.test.request.WithdrawRequest;
import com.acooly.openapi.framework.service.test.response.CreateOrderResponse;
import com.acooly.openapi.framework.service.test.response.PayOrderResponse;
import com.acooly.openapi.framework.service.test.response.WithdrawResponse;
import com.google.common.base.Charsets;
import com.google.common.collect.Lists;
import com.google.common.io.ByteStreams;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;

import static com.acooly.openapi.framework.common.ApiConstants.ANONYMOUS_ACCESS_KEY;
import static com.acooly.openapi.framework.common.ApiConstants.ANONYMOUS_SECRET_KEY;
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
  public void testLogin() throws Exception {

    //1. 使用匿名认证信息登录
    accessKey = ANONYMOUS_ACCESS_KEY;
    secretKey = ANONYMOUS_SECRET_KEY;
    LoginRequest request = new LoginRequest();
    request.setRequestNo(UUID.randomUUID().toString());
    request.setService("login");
    request.setUsername("qiubo");
    request.setPassword(encrypt("passwd"));
    LoginResponse response = request(request, LoginResponse.class);
    log.info("{}", response);

    //登录成功后，使用下发的认证信息访问服务
    accessKey = response.getAccessKey();
    secretKey = response.getSecretKey();
    LoginAssertRequest loginAssertRequest = new LoginAssertRequest();
    loginAssertRequest.setRequestNo(UUID.randomUUID().toString());
    loginAssertRequest.setService("loginAssert");
    LoginAssertResponse loginAssertResponse =
        request(loginAssertRequest, LoginAssertResponse.class);
    assertThat(loginAssertResponse).isNotNull();
    assertThat(loginAssertResponse.isSuccess()).isTrue();
    assertThat(loginAssertResponse.getAccessKey()).isEqualTo(accessKey);
  }

  @Test
  public void testNotify() throws Exception {
    CountDownLatch doneSignal = new CountDownLatch(1);

    int port = 10090;
    HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);
    server.createContext("/", new TestHandler(doneSignal));
    server.setExecutor(null); // creates a default executor
    server.start();

    PayOrderRequest request = new PayOrderRequest();
    request.setRequestNo(UUID.randomUUID().toString());
    request.setService("payOrder");
    request.setAmount(new Money("100"));
    request.setPayerUserId("xxxxxx");
    request.setNotifyUrl("http://127.0.0.1:" + port + "/");
    PayOrderResponse response = request(request, PayOrderResponse.class);
    log.info("{}", response);
    assertThat(response).isNotNull();
    assertThat(response.isSuccess()).isTrue();
    assertThat(response.getCode()).isEqualTo(ApiServiceResultCode.PROCESSING.code());
    doneSignal.await();
  }

  static class TestHandler implements HttpHandler {
    private CountDownLatch countDownLatch;

    public TestHandler(CountDownLatch countDownLatch) {
      this.countDownLatch = countDownLatch;
    }

    @Override
    public void handle(HttpExchange t) throws IOException {
      String requestBody = new String(ByteStreams.toByteArray(t.getRequestBody()), Charsets.UTF_8);
      log.info("notify requestBody:\n{}", requestBody);
      //
      // assertThat(t.getRequestHeaders().get(ApiConstants.PARTNER_ID).get(0)).isEqualTo("test");
      assertThat(t.getRequestHeaders().get(ApiConstants.SIGN)).isNotEmpty();
      assertThat(t.getRequestHeaders().get(ApiConstants.SIGN_TYPE).get(0)).isEqualTo("MD5");
      String response = ApiConstants.NOTIFY_SUCCESS_CONTENT;
      t.sendResponseHeaders(200, response.length());
      OutputStream os = t.getResponseBody();
      os.write(response.getBytes(Charsets.UTF_8));
      os.close();
      countDownLatch.countDown();
    }
  }
}
