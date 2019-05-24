package com.acooly.openapi.framework.service.test;

import com.acooly.core.utils.Money;
import com.acooly.openapi.framework.client.OpenApiClient;
import com.acooly.openapi.framework.common.ApiConstants;
import com.acooly.openapi.framework.common.enums.ApiServiceResultCode;
import com.acooly.openapi.framework.common.message.ApiResponse;
import com.acooly.openapi.framework.common.message.builtin.AuthRequest;
import com.acooly.openapi.framework.common.message.builtin.AuthRequest.ExpiredBody;
import com.acooly.openapi.framework.common.utils.json.JsonMarshallor;
import com.acooly.openapi.framework.core.test.AbstractApiServieTests;
import com.acooly.openapi.framework.common.message.builtin.LoginRequest;
import com.acooly.openapi.framework.common.message.builtin.LoginResponse;
import com.acooly.openapi.framework.service.test.dto.GoodInfo;
import com.acooly.openapi.framework.service.test.enums.GoodType;
import com.acooly.openapi.framework.service.test.notify.PayOrderNotify;
import com.acooly.openapi.framework.service.test.request.OrderCreateRequest;
import com.acooly.openapi.framework.service.test.request.PayOrderRequest;
import com.acooly.openapi.framework.service.test.request.WithdrawRequest;
import com.acooly.openapi.framework.service.test.response.OrderCreateResponse;
import com.acooly.openapi.framework.service.test.response.PayOrderResponse;
import com.acooly.openapi.framework.service.test.response.WithdrawResponse;
import com.github.kevinsawicki.http.HttpRequest;
import com.google.common.base.Charsets;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.io.ByteStreams;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.junit.Test;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;

import static com.acooly.openapi.framework.common.ApiConstants.*;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author qiubo@yiji.com
 */
@Slf4j
public class OpenApiTest extends AbstractApiServieTests {

    static String content = UUID.randomUUID().toString();

    /**
     * 同步请求
     * <p>
     * 1. 请求参数序列化为json，放在http body中传输
     * 2. 安全校验相关参数支持放在http header或者url中
     * 3. 响应安全校验信息放在http header中
     * 4. 响应体为json
     */
    @Test
    public void testSync() throws Exception {
        OrderCreateRequest request = new OrderCreateRequest();
        request.setRequestNo(UUID.randomUUID().toString());
        request.setService("orderCreate");
        request.setTitle("同步请求创建订单");
        request.setPayeeUserId("12345678900987654321");
        request.setPayerUserId("09876543211234567890");
        request.setBuyerUserId("09876543211234567890");
        request.setBuyeryEmail("qiuboboy@qq.com");
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
        assertThat(response.getContext()).isEqualTo(content);
    }

    @Test
    public void testSync1() throws Exception {
        OrderCreateRequest request = new OrderCreateRequest();
        request.setPartnerId("test");
        request.setRequestNo(UUID.randomUUID().toString());
        request.setService("createOrder");
        request.setTitle("同步请求创建订单");
        request.setPayeeUserId("12345678900987654321");
        request.setPayerUserId("09876543211234567890");
        request.setBuyerUserId("09876543211234567890");
        request.setBuyeryEmail("qiuboboy@qq.com");
        request.setBuyeryMobileNo("13898765453");
        request.setBuyerCertNo("330702194706165014");
        request.setPassword("12312312");
        request.setContext(content);
        OpenApiClient openApiClient = new OpenApiClient("http://127.0.0.1:8089/gateway.do", TEST_ACCESS_KEY, TEST_SECRET_KEY);
        OrderCreateResponse response = openApiClient.send(request, OrderCreateResponse.class);
        log.info("{}", response);
        assertThat(response).isNotNull();
        assertThat(response.isSuccess()).isTrue();
        assertThat(response.getContext()).isEqualTo(content);
    }

    @Test
    public void testSync_ParamCheckError() throws Exception {
        OrderCreateRequest request = new OrderCreateRequest();
        request.setRequestNo(UUID.randomUUID().toString());
        request.setService("createOrder");
        request.setTitle("同步请求创建订单");
        request.setPayeeUserId("12345678900987654321");
        request.setPayerUserId("09876543211234567890");
        request.setBuyerUserId("09876543211234567890");
        request.setBuyeryEmail("qiubo");
        request.setBuyeryMobileNo("138987654531");
        request.setBuyerCertNo("33070219470665014");
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
        assertThat(response.isSuccess()).isFalse();
        assertThat(response.getCode()).isEqualTo("PARAMETER_ERROR");
        assertThat(response.getDetail()).contains("buyeryMobileNo").contains("buyerCertNo").contains("buyeryEmail");

        assertThat(response.getContext()).isEqualTo(content);
    }

    /**
     * 跳转请求
     * <p>
     * 1. 请求参数序列化为json，放在http body中传输
     * 2. 安全校验相关参数支持放在http header或者url中
     * 3. 服务端响应302
     * 3. 响应安全校验信息存储在http header中Location参数中
     * 5. 响应内容为Location url中的body参数
     */
    @Test
    public void testRedirect() throws Exception {
        WithdrawRequest request = new WithdrawRequest();
        request.setRequestNo(UUID.randomUUID().toString());
        request.setService("withdraw");
        request.setAmount(new Money("100"));
        request.setContext(content);
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
        assertThat(response.getContext()).isEqualTo(content);
    }

    /**
     * 登录请求
     * <p>
     * 1. 使用匿名账户登录
     * 2. 账户信息校验成功后，下发新的accessKey\secretKey
     * 3. 后续请求使用新的accessKey\secretKey
     */
    @Test
    public void testLogin() throws Exception {
        // 1. 使用匿名认证信息登录
        OpenApiClient openApiClient = new OpenApiClient("http://127.0.0.1:8089/gateway.do", ANONYMOUS_ACCESS_KEY, ANONYMOUS_SECRET_KEY);

        LoginRequest request = new LoginRequest();
        request.setRequestNo(UUID.randomUUID().toString());
        request.setService("login");
        request.setUsername("qiu23bo");
        request.setPartnerId("test");
        request.setPassword("passwd");
        LoginResponse response = openApiClient.send(request, LoginResponse.class);
        log.info("{}", response);
//
//        // 登录成功后，使用下发的认证信息访问服务
//        openApiClient = new OpenApiClient("http://127.0.0.1:8089/gateway.do", response.getAccessKey(), response.getSecretKey());
//        LoginAssertRequest loginAssertRequest = new LoginAssertRequest();
//        loginAssertRequest.setRequestNo(UUID.randomUUID().toString());
//        loginAssertRequest.setPartnerId("test");
//        loginAssertRequest.setService("loginAssert");
//        LoginAssertResponse loginAssertResponse =
//                openApiClient.send(loginAssertRequest, LoginAssertResponse.class);
//        assertThat(loginAssertResponse).isNotNull();
//        assertThat(loginAssertResponse.isSuccess()).isTrue();
//        assertThat(loginAssertResponse.getAccessKey()).isEqualTo(response.getAccessKey());

        //测试认证服务

        ExpiredBody expiredBody = new ExpiredBody();
        String body = "sdfdsfdfdfsd";
        //设置请求参数
        expiredBody.setBody(body);
        //请求有效期
        expiredBody.setExpireDate(new Date());
        AuthRequest authRequest = new AuthRequest();
        authRequest.setRequestNo(UUID.randomUUID().toString());
        authRequest.setService("auth");
        authRequest.setPartnerId("test");
        //签名使用的accessKey
        authRequest.setAccessKey(response.getAccessKey());
        //需要传输的数据
        authRequest.setExpiredBody(expiredBody);
        //使用secretKey签名
        authRequest.setSign(openApiClient.sign(expiredBody.getSignBody()));
        //认证签名是否正确
        ApiResponse apiResponse = openApiClient.send(authRequest, ApiResponse.class);
        assertThat(apiResponse).isNotNull();
        assertThat(apiResponse.isSuccess()).isTrue();
    }

    /**
     * 支持Content-Type=application/x-www-form-urlencoded
     * <p>
     * 1. 请求数据放在form表单中的body参数中
     * 2. 安全校验相关参数支持放在http header或者url中
     */
    @Test
    public void testForm() throws Exception {
        OrderCreateRequest request = new OrderCreateRequest();
        request.setRequestNo(UUID.randomUUID().toString());
        request.setService("createOrder");
        request.setTitle("同步请求创建订单");
        request.setPayeeUserId("12345678900987654321");
        request.setPayerUserId("09876543211234567890");
        request.setBuyerUserId("09876543211234567890");
        request.setBuyeryMobileNo("13898765453");
//    request.setBuyeryEmail("qiuboboy@qq.com");
//    request.setBuyerCertNo("330702194706165014");
//    request.setPassword(encrypt("12312312"));
        request.setContext(content);
        request.setPartnerId(partnerId);
        request.ext("xx", "oo");
        String body = JsonMarshallor.INSTANCE.marshall(request);
        Map<String, String> requestHeader = Maps.newTreeMap();
        requestHeader.put(ApiConstants.ACCESS_KEY, accessKey);
        requestHeader.put(ApiConstants.SIGN_TYPE, "MD5");
        requestHeader.put(ApiConstants.SIGN, sign(body));
        if (showLog) {
            log.info("请求-> header:{} body:{}", requestHeader, body);
        }
        Map<String, String> params = Maps.newTreeMap();
        params.put(BODY, body);
        HttpRequest httpRequest =
                HttpRequest.post(gatewayUrl).headers(requestHeader).followRedirects(false).form(params);
        System.out.println(httpRequest.body());
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
        request.setContext(content);
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
            PayOrderNotify payOrderNotify =
                    JsonMarshallor.INSTANCE.parse(requestBody, PayOrderNotify.class);
            log.info("notify requestBody:\n{}", payOrderNotify);
            assertThat(payOrderNotify.getContext()).isEqualTo(content);
            String sign = t.getRequestHeaders().get(ApiConstants.SIGN).get(0);
            assertThat(sign).isNotEmpty();
            assertThat(t.getRequestHeaders().get(ApiConstants.SIGN_TYPE).get(0)).isEqualTo("MD5");
            if (!DigestUtils.md5Hex(requestBody + TEST_SECRET_KEY).equals(sign)) {
                throw new RuntimeException("验证失败");
            }
            String response = ApiConstants.NOTIFY_SUCCESS_CONTENT;
            t.sendResponseHeaders(200, response.length());
            OutputStream os = t.getResponseBody();
            os.write(response.getBytes(Charsets.UTF_8));
            os.close();
            countDownLatch.countDown();
        }
    }
}
