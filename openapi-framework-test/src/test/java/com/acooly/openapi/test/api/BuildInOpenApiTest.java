package com.acooly.openapi.test.api;

import com.acooly.core.utils.Money;
import com.acooly.openapi.framework.common.ApiConstants;
import com.acooly.openapi.framework.common.enums.ApiServiceResultCode;
import com.acooly.openapi.framework.common.enums.DeviceType;
import com.acooly.openapi.framework.common.utils.IPUtil;
import com.acooly.openapi.framework.common.utils.json.JsonMarshallor;
import com.acooly.openapi.framework.core.test.AbstractApiServieTests;
import com.acooly.openapi.framework.common.message.builtin.LoginRequest;
import com.acooly.openapi.framework.common.message.builtin.LoginResponse;
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


}
