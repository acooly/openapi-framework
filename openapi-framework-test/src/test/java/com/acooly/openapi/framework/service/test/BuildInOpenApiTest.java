package com.acooly.openapi.framework.service.test;

import com.acooly.core.utils.Money;
import com.acooly.openapi.framework.common.ApiConstants;
import com.acooly.openapi.framework.common.enums.ApiServiceResultCode;
import com.acooly.openapi.framework.common.enums.DeviceType;
import com.acooly.openapi.framework.common.utils.IPUtil;
import com.acooly.openapi.framework.common.utils.json.JsonMarshallor;
import com.acooly.openapi.framework.core.test.AbstractApiServieTests;
import com.acooly.openapi.framework.common.message.builtin.LoginRequest;
import com.acooly.openapi.framework.common.message.builtin.LoginResponse;
import com.acooly.openapi.framework.service.test.notify.PayOrderNotify;
import com.acooly.openapi.framework.service.test.request.PayOrderRequest;
import com.acooly.openapi.framework.service.test.response.PayOrderResponse;
import com.google.common.base.Charsets;
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
import java.util.UUID;
import java.util.concurrent.CountDownLatch;

import static com.acooly.openapi.framework.common.ApiConstants.TEST_SECRET_KEY;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * 内置Api服务测试
 *
 * @author zhangpu
 * @date 2019-01-27
 */
@Slf4j
public class BuildInOpenApiTest extends AbstractApiServieTests {

    static String content = UUID.randomUUID().toString();


    String payerUserId = "09876543211234567890";
    Money amount = Money.amout("200");

    /**
     * 测试登录接口
     */
    @Test
    public void testLogin() throws Exception {
        LoginRequest request = new LoginRequest();
        request.setUsername("zhangpu");
        request.setPassword("11111111");
        request.setChannel("test");
        request.setDeviceId(UUID.randomUUID().toString());
        request.setDeviceType(DeviceType.IPHONE);
        request.setDeviceModel("AAA");
        request.setCustomerIp(IPUtil.getFirstNoLoopbackIPV4Address());
        LoginResponse response = request(request, LoginResponse.class);
        log.info("{}", response);
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
