package com.acooly.openapi.test.api;

import com.acooly.core.utils.Ids;
import com.acooly.openapi.framework.common.enums.ApiServiceResultCode;
import com.acooly.openapi.framework.common.message.ApiRequest;
import com.acooly.openapi.framework.common.message.ApiResponse;
import com.acooly.openapi.framework.common.message.builtin.LoginRequest;
import com.acooly.openapi.framework.common.message.builtin.LoginResponse;
import com.acooly.openapi.framework.core.test.AbstractApiServieTests;
import com.acooly.openapi.framework.demo.message.request.OrderCashierPayApiRequest;
import com.acooly.openapi.framework.demo.message.request.OrderCreateApiRequest;
import com.acooly.openapi.framework.demo.message.request.WithdrawApiRequest;
import com.acooly.openapi.framework.demo.message.response.OrderCreateApiResponse;
import com.acooly.openapi.framework.demo.message.response.WithdrawApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * 常规OpenApi基准测试和验证
 *
 * @author zhangpu
 * @date 2019-11-14
 */
@Slf4j
public class OpenApiBenchmarkTest extends AbstractApiServieTests {

    @Test
    public void testBenchmark() {
        testSync();
        testSyncValidateError();
        testRequestNoDuplicateError();
        testRequestNoDuplicateError();
        testAsyncRequiredNotifyUrlError();
        testRedirectRequiredNotifyUrlAndReturnUrl();
    }

    /**
     * 测试服务同步报文，包括：
     * 1、特殊字符, 2、多级报文, 3、加密解密, 4.context回传
     */
    @Test
    public void testSync() {
        OrderCreateApiRequest request = OpenApiBenchmarkTestUtils.buildOrderCreateApiRequest();
        OrderCreateApiResponse response = request(request, OrderCreateApiResponse.class);
        assertThat(response).isNotNull();
        assertThat(response.isSuccess()).isTrue();
        assertThat(response.isSuccess()).isTrue();
        assertThat(response.getContext()).isEqualTo(OpenApiBenchmarkTestUtils.CONTEXT);
        log.info("测试：标准同步接口 - [通过]");
    }


    /**
     * 测试参数错误
     */
    @Test
    public void testSyncValidateError() {
        LoginRequest request = OpenApiBenchmarkTestUtils.buildLoginRequest();
        // 标志username为null，可预计报参数错误
        request.setUsername(null);
        LoginResponse response = request(request, LoginResponse.class);
        assertThat(response).isNotNull();
        assertThat(response.isSuccess()).isFalse();
        assertThat(response.getCode()).isEqualTo(ApiServiceResultCode.PARAMETER_ERROR.code());
        log.info("测试：JSR303参数错误验证 - [通过]");
    }

    /**
     * 测试:请求号重复
     */
    @Test
    public void testRequestNoDuplicateError() {
        LoginRequest request = OpenApiBenchmarkTestUtils.buildLoginRequest();
        String requestNo = Ids.RandomNumberGenerator.getNewString(20);
        // 第一次请求
        request.setRequestNo(requestNo);
        request(request, LoginResponse.class);
        // 第二次请求
        request.setRequestNo(requestNo);
        LoginResponse response = request(request, LoginResponse.class);
        assertThat(response).isNotNull();
        assertThat(response.isSuccess()).isFalse();
        assertThat(response.getCode()).isEqualTo(ApiServiceResultCode.REQUEST_NO_NOT_UNIQUE.code());
        log.info("测试：请求号唯一 - [通过]");
    }


    /**
     * 测试: 异步接口必须输入对应的notifyUrl
     */
    @Test
    public void testAsyncRequiredNotifyUrlError() {
        // 异步接口，必须传入notifyUrl
        WithdrawApiRequest request = OpenApiBenchmarkTestUtils.buildWithdrawApiRequest();
        // 手动标记为null，期望返回错误码:PARAMETER_ERROR, detail:notifyUrl不能为空
        request.setNotifyUrl(null);
        WithdrawApiResponse response = request(request, WithdrawApiResponse.class);
        assertThat(response).isNotNull();
        assertThat(response.isSuccess()).isFalse();
        assertThat(response.getCode()).isEqualTo(ApiServiceResultCode.PARAMETER_ERROR.code());
        assertThat(response.getDetail()).isEqualTo("notifyUrl不能为空");
        log.info("测试：异步报文的notifyUrl不为空 - [通过]");
    }

    /**
     * 测试: 跳转必须输入对应的notifyUrl和returnUrl
     */
    @Test
    public void testRedirectRequiredNotifyUrlAndReturnUrl() {
        // 异步接口，必须传入notifyUrl
        OrderCashierPayApiRequest request = OpenApiBenchmarkTestUtils.buildOrderCashierPayApiRequest();
        // 手动标记为null，期望返回错误码:PARAMETER_ERROR, detail:returnUrl不能为空
        request.setReturnUrl(null);
        WithdrawApiResponse response = request(request, WithdrawApiResponse.class);
        assertThat(response).isNotNull();
        assertThat(response.isSuccess()).isFalse();
        assertThat(response.getCode()).isEqualTo(ApiServiceResultCode.PARAMETER_ERROR.code());
        assertThat(response.getDetail()).isEqualTo("returnUrl不能为空");
        log.info("测试：跳转报文的returnUrl不为空 - [通过]");
    }


    @Test
    public void testServiceInnerBusinessException() {
        ApiRequest request = new ApiRequest();
        request.setService("simpleInfo");
        request.setContext("1212");
        ApiResponse response = request(request, ApiResponse.class);
        assertThat(response).isNotNull();
        assertThat(response.isSuccess()).isFalse();
        assertThat(response.getCode()).isEqualTo("TEST_ERROR_CODE");
    }

}
