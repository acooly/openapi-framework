package com.acooly.openapi.test.api;

import com.acooly.core.utils.Ids;
import com.acooly.core.utils.Money;
import com.acooly.core.utils.enums.ResultStatus;
import com.acooly.openapi.framework.common.ApiConstants;
import com.acooly.openapi.framework.common.enums.ApiProtocol;
import com.acooly.openapi.framework.core.test.AbstractApiServieTests;
import com.acooly.openapi.framework.service.test.request.WithdrawApiRequest;
import com.acooly.openapi.framework.service.test.response.WithdrawApiResponse;
import com.github.kevinsawicki.http.HttpRequest;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.Map;

/**
 * 以提现为案例，测试异步接口服务
 *
 * @author zhangpu
 * @date 2019-01-27
 */
@Slf4j
public class WithdrawOpenApiTest extends AbstractApiServieTests {

    Money amount = Money.amout("200");

    /**
     * 下层业务系统发起通知的MOCK实现CONTROLLER
     */
    String CALL_ASYNC_NOTIFY_URL = "http://127.0.0.1:8089/openapi/test/withdraw/server/notifyCall.html";

    String CALL_SEND_NOTIFY_URL = "http://127.0.0.1:8089/openapi/test/withdraw/server/sendNotify.html";

    /**
     * 客户端通知地址
     */
    String TEST_NOTIFY_URL = "http://127.0.0.1:8089/openapi/test/withdraw/client/notifyUrl.html";

    /**
     * 同步请求提现
     * <p>
     * 1. 请求参数序列化为json，放在http body中传输
     * 2. 安全校验相关参数支持放在http header或者url中
     * 3. 响应安全校验信息放在http header中
     * 4. 响应体为json
     */
    @Test
    public void testWithdraw() throws Exception {
        WithdrawApiRequest request = new WithdrawApiRequest();
        request.setProtocol(ApiProtocol.HTTP_FORM_JSON);
        request.setRequestNo(Ids.RandomNumberGenerator.getNewString(20));
        request.setMerchOrderNo(Ids.RandomNumberGenerator.getNewString(20));
        request.setService("withdraw");
        request.setAmount(amount);
        request.setDelay(WithdrawApiRequest.DelayEnum.T1);
        request.setContext("会话信息，透传返回");
        request.ext("xx", "oo");
        // 该地址是test模块使用controller模拟的客户notifyUrl地址,实现在测试中，客户端打印接受的通知信息
        request.setNotifyUrl(TEST_NOTIFY_URL);
        WithdrawApiResponse response = request(request, WithdrawApiResponse.class);
        log.info("提现申请提交成功，订单号: {}", request.getMerchOrderNo());
    }

    /**
     * 通过test模块的控制器mock下层业务系统发起通知的整个过程
     *
     * @throws Exception
     */
    @Test
    public void testAsyncNotify() throws Exception {
        doCallNotify(CALL_ASYNC_NOTIFY_URL);
    }

    @Test
    public void testSendNotify() throws Exception {
        doCallNotify(CALL_SEND_NOTIFY_URL);
    }

    protected void doCallNotify(String callUrl) {
        // 你需要在testWithdraw()后，在日志中获取gid
        String gid = "12123123123123123";
        Map<String, String> parameters = Maps.newHashMap();
        parameters.put(ApiConstants.GID, gid);
        parameters.put(ApiConstants.PARTNER_ID, partnerId);
        parameters.put("merchOrderNo", "62374886002201201786");
        parameters.put("amountIn", "199.60");
        parameters.put("fee", "0.40");
        parameters.put("delay", "T1");
        parameters.put("status", ResultStatus.success.code());
        HttpRequest httpRequest = HttpRequest.post(callUrl)
                .contentType(HttpRequest.CONTENT_TYPE_FORM)
                .form(parameters);
        int status = httpRequest.code();
        String body = httpRequest.body();
        log.info("通知调用结果：{}", body);
    }

}
