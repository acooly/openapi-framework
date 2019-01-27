package com.acooly.openapi.framework.service.test;

import com.acooly.core.utils.Money;
import com.acooly.openapi.framework.common.ApiConstants;
import com.acooly.openapi.framework.common.enums.ApiServiceResultCode;
import com.acooly.openapi.framework.common.utils.Ids;
import com.acooly.openapi.framework.common.utils.json.JsonMarshallor;
import com.acooly.openapi.framework.core.test.AbstractApiServieTests;
import com.acooly.openapi.framework.service.test.dto.GoodInfo;
import com.acooly.openapi.framework.service.test.enums.GoodType;
import com.acooly.openapi.framework.service.test.notify.PayOrderNotify;
import com.acooly.openapi.framework.service.test.request.CreateOrderRequest;
import com.acooly.openapi.framework.service.test.request.PayOrderRequest;
import com.acooly.openapi.framework.service.test.response.CreateOrderResponse;
import com.acooly.openapi.framework.service.test.response.PayOrderResponse;
import com.google.common.base.Charsets;
import com.google.common.collect.Lists;
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
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;

import static com.acooly.openapi.framework.common.ApiConstants.TEST_SECRET_KEY;
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

    /**
     * 同步请求
     * <p>
     * 1. 请求参数序列化为json，放在http body中传输
     * 2. 安全校验相关参数支持放在http header或者url中
     * 3. 响应安全校验信息放在http header中
     * 4. 响应体为json
     */
    @Test
    public void testOrderCreateSync() throws Exception {
        CreateOrderRequest request = new CreateOrderRequest();
        request.setRequestNo(Ids.getDid());
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
        CreateOrderResponse response = request(request, CreateOrderResponse.class);
        log.info("{}", response);
        log.info("请求单号: {}", request.getRequestNo());
        assertThat(response).isNotNull();
        assertThat(response.isSuccess()).isTrue();
        assertThat(response.getContext()).isEqualTo(content);
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
        request.setService("orderPayFacade");
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
