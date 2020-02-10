package com.acooly.openapi.test.api;

import com.acooly.core.utils.Ids;
import com.acooly.openapi.framework.client.OpenApiClient;
import com.acooly.openapi.framework.common.ApiConstants;
import com.acooly.openapi.framework.common.enums.DeviceType;
import com.acooly.openapi.framework.common.message.ApiRequest;
import com.acooly.openapi.framework.common.message.ApiResponse;
import com.acooly.openapi.framework.common.message.builtin.LoginRequest;
import com.acooly.openapi.framework.common.message.builtin.LoginResponse;
import com.acooly.openapi.framework.core.test.AbstractApiServieTests;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * 常规OpenApi动态密码和权限测试
 *
 * @author zhangpu
 * @date 2020-01-05
 */
@Slf4j
public class OpenApiDynamicTest extends AbstractApiServieTests {

    /**
     * 测试服务同步报文，包括：
     * 1、特殊字符, 2、多级报文, 3、加密解密, 4.context回传
     */
    @Test
    public void testLoginAndAuthz() {
        LoginResponse response = login("test", "06f7aab08aa2431e6dae6a156fc9e0b4",
                "zhangpu2", "123123123");
        OpenApiClient openApiClient = getOpenApiClient(response.getAccessKey(), response.getSecretKey());

        ApiRequest request = new ApiRequest();
        request.setRequestNo(Ids.oid());
        request.setService("simpleInfo");
        request.setContext("123123123");
        openApiClient.send(request, ApiResponse.class);

    }


    private LoginResponse login(String AccessKey, String secretKey, String username, String password) {
        OpenApiClient openApiClient = getOpenApiClient(AccessKey, secretKey);
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername(username);
        loginRequest.setPassword(password);
        loginRequest.setCustomerIp("211.231.22.38");
        loginRequest.setChannel("WECHAT");
        loginRequest.setDeviceType(DeviceType.ANDROID);
        loginRequest.setDeviceModel("HUAWEI-MATE10");
        loginRequest.setDeviceId("adfas234sdfsdfa");
        loginRequest.setRequestNo(Ids.gid());
        LoginResponse response = openApiClient.send(loginRequest, LoginResponse.class);
        assertThat(response).isNotNull();
        assertThat(response.isSuccess()).isTrue();
        log.info("测试：匿名登录接口 - [通过]：LoginResponse: {}", response);
        log.info("customerId: {}", response.getCustomerId());
        log.info("AccessKeys: {}", response.getAccessKey());
        log.info("SecretKey: {}", response.getSecretKey());
        return response;
    }


    private OpenApiClient getOpenApiClient(String AccessKey, String secretKey) {
        return new OpenApiClient(this.gatewayUrl, AccessKey, secretKey);
    }
}
