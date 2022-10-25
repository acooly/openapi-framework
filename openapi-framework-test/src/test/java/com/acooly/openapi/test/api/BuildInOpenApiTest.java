package com.acooly.openapi.test.api;

import com.acooly.core.utils.Money;
import com.acooly.core.utils.system.IPUtil;
import com.acooly.openapi.framework.common.enums.DeviceType;
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
        accessKey = "210323212041022F0007";
        secretKey = "8f00a552a051c3cb3c480f2620b662d1";
    }

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
