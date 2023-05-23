package com.acooly.openapi.test.api;

import com.acooly.core.utils.Money;
import com.acooly.core.utils.system.IPUtil;
import com.acooly.openapi.framework.common.ApiConstants;
import com.acooly.openapi.framework.common.enums.DeviceType;
import com.acooly.openapi.framework.common.message.ApiRequest;
import com.acooly.openapi.framework.common.message.ApiResponse;
import com.acooly.openapi.framework.common.message.builtin.LoginRequest;
import com.acooly.openapi.framework.common.message.builtin.LoginResponse;
import com.acooly.openapi.framework.core.test.AbstractApiServieTests;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.UUID;

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

    {
        accessKey = ApiConstants.ANONYMOUS_ACCESS_KEY;
        secretKey = ApiConstants.ANONYMOUS_SECRET_KEY;
    }

    /**
     * 测试登录接口
     * test#12345678901234567890
     * c3614b2fa098b4905d81509f443e053b
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
    public void testAnno() throws Exception {
        ApiRequest request = new ApiRequest();
        request.setService("anno");
        ApiResponse response = request(request, ApiResponse.class);
        log.info("{}", response);
    }

}
