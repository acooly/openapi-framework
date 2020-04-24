package com.acooly.openapi.test.api;

import com.acooly.core.utils.Ids;
import com.acooly.openapi.framework.common.enums.ApiServiceResultCode;
import com.acooly.openapi.framework.common.message.ApiRequest;
import com.acooly.openapi.framework.common.message.ApiResponse;
import com.acooly.openapi.framework.common.message.builtin.LoginRequest;
import com.acooly.openapi.framework.common.message.builtin.LoginResponse;
import com.acooly.openapi.framework.core.test.AbstractApiServieTests;
import com.acooly.openapi.framework.demo.message.request.DemoOrderCashierPayApiRequest;
import com.acooly.openapi.framework.demo.message.request.DemoOrderCreateApiRequest;
import com.acooly.openapi.framework.demo.message.request.DemoWithdrawApiRequest;
import com.acooly.openapi.framework.demo.message.response.DemoOrderCreateApiResponse;
import com.acooly.openapi.framework.demo.message.response.DemoWithdrawApiResponse;
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
        testEmojiAndSpecialChar();
    }

    /**
     * 测试服务同步报文，包括：
     * 1、特殊字符, 2、多级报文, 3、加密解密, 4.context回传
     */
    @Test
    public void testSync() {
        DemoOrderCreateApiRequest request = OpenApiBenchmarkTestUtils.buildOrderCreateApiRequest();
        DemoOrderCreateApiResponse response = request(request, DemoOrderCreateApiResponse.class);
        assertThat(response).isNotNull();
        assertThat(response.isSuccess()).isTrue();
        assertThat(response.isSuccess()).isTrue();
        assertThat(response.getContext()).isEqualTo(OpenApiBenchmarkTestUtils.CONTEXT);
        log.info("[通过] 测试：标准同步接口：demoOrderCreateApi ");
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
        log.info("[通过] 测试：JSR303参数错误验证。");
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
        log.info("[通过] 测试：请求号唯一");
    }


    /**
     * 测试: 异步接口必须输入对应的notifyUrl
     */
    @Test
    public void testAsyncRequiredNotifyUrlError() {
        // 异步接口，必须传入notifyUrl
        DemoWithdrawApiRequest request = OpenApiBenchmarkTestUtils.buildWithdrawApiRequest();
        // 手动标记为null，期望返回错误码:PARAMETER_ERROR, detail:notifyUrl不能为空
        request.setNotifyUrl(null);
        DemoWithdrawApiResponse response = request(request, DemoWithdrawApiResponse.class);
        assertThat(response).isNotNull();
        assertThat(response.isSuccess()).isFalse();
        assertThat(response.getCode()).isEqualTo(ApiServiceResultCode.PARAMETER_ERROR.code());
        assertThat(response.getDetail()).isEqualTo("notifyUrl不能为空");
        log.info("[通过] 测试：异步报文的notifyUrl不为空");
    }

    /**
     * 测试: 跳转必须输入对应的notifyUrl和returnUrl
     */
    @Test
    public void testRedirectRequiredNotifyUrlAndReturnUrl() {
        // 异步接口，必须传入notifyUrl
        DemoOrderCashierPayApiRequest request = OpenApiBenchmarkTestUtils.buildOrderCashierPayApiRequest();
        // 手动标记为null，期望返回错误码:PARAMETER_ERROR, detail:returnUrl不能为空
        request.setReturnUrl(null);
        DemoWithdrawApiResponse response = request(request, DemoWithdrawApiResponse.class);
        assertThat(response).isNotNull();
        assertThat(response.isSuccess()).isFalse();
        assertThat(response.getCode()).isEqualTo(ApiServiceResultCode.PARAMETER_ERROR.code());
        assertThat(response.getDetail()).isEqualTo("returnUrl不能为空");
        log.info("[通过] 测试：跳转报文的returnUrl不为空");
    }


    /**
     * 测试表情符号和特殊字符
     */
    @Test
    public void testEmojiAndSpecialChar() {
        ApiRequest request = new ApiRequest();
        request.setService("demoSimpleInfo");
        String specialStr = "😁🀀εǚ☏©\uD83D\uDC3E ";
        request.setContext(specialStr);
        ApiResponse response = request(request, ApiResponse.class);
        assertThat(response).isNotNull();
        assertThat(response.isSuccess()).isTrue();
        assertThat(response.getContext()).isEqualTo(specialStr);
        log.info("[通过] 测试：表情符号和特殊字符");
    }

}
