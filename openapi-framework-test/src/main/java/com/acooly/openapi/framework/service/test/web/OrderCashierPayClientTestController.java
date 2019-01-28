package com.acooly.openapi.framework.service.test.web;

import com.acooly.core.utils.Assert;
import com.acooly.core.utils.Money;
import com.acooly.core.utils.Servlets;
import com.acooly.openapi.framework.client.MessageResult;
import com.acooly.openapi.framework.client.OpenApiClient;
import com.acooly.openapi.framework.common.utils.Exceptions;
import com.acooly.openapi.framework.common.utils.Strings;
import com.acooly.openapi.framework.service.test.request.OrderCashierPayRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.io.StringWriter;
import java.nio.charset.Charset;
import java.util.Map;

/**
 * MOCK 跳转支付客户端的服务
 * <p>
 * 这里提供请求支付端的：returnUrl和notifyUrl
 *
 * @author zhangpu
 * @date 2019-01-27 19:28
 */
@Slf4j
@Controller
@RequestMapping("/openapi/test/client")
public class OrderCashierPayClientTestController {

    String payerUserId = "09876543211234567890";

    @Autowired
    private OpenApiClient openApiClient;


    /**
     * Mock客户端视图收集用户支付信息，并组装后请求OpenAPi跳转支付接口
     *
     * @param request
     * @param response
     */
    @RequestMapping("pay")
    public void mockPay(HttpServletRequest request, HttpServletResponse response) {
        String merchOrderNo = request.getParameter("merchOrderNo");
        String amount = Strings.isBlankDefault(request.getParameter("amount"), "210");
        Assert.hasLength(merchOrderNo, "订单号不能为空");

        OrderCashierPayRequest apiRequest = new OrderCashierPayRequest();
        apiRequest.setService("orderCashierPay");
        apiRequest.setMerchOrderNo(merchOrderNo);
        apiRequest.setAmount(Money.amout(amount));
        apiRequest.setPayerUserId(payerUserId);
        MessageResult messageResult = openApiClient.parse(apiRequest);
        // 可以传参到页面通过页面POST提交（URL:messageResult.getUrl(), Post参数：messageResult.getAllParameters()）
        // 或则这里直接redirect

        com.acooly.openapi.framework.common.utils.Servlets.redirect(response, messageResult.getUrl() + "?" + messageResult.getQueryString());
    }


    @RequestMapping("notifyUrl")
    public void mockNotifyUrl(HttpServletRequest request, HttpServletResponse response) {
        Map<String, String> headers = Servlets.getHeaders(request, "x-api");
        log.info("客户端 接收异步通知 headers: {}", headers);
        log.info("客户端 接收异步通知 Body: {}", getBody(request));
        Servlets.writeText(response, "success");
        log.info("客户端 接收异步通知 回写 success");
    }

    @RequestMapping("returnUrl")
    @ResponseBody
    public Object mockReturnUrl(HttpServletRequest request) {
        Map<String, String> data = Servlets.getHeaders(request, "x-api");
        log.info("return Header:\n");
        for (Map.Entry<String, String> entry : data.entrySet()) {
            System.out.println(entry.getKey() + ": " + entry.getValue());
        }
        data.put("body", getBody(request));
        log.info("客户端 接收同步通知 data: {}", data);
        return data;
    }

    public static String getBody(HttpServletRequest request) {
        try (InputStream in = request.getInputStream()) {
            StringWriter bodyWriter = new StringWriter();
            IOUtils.copy(in, bodyWriter, Charset.forName("UTF-8"));
            return bodyWriter.toString();
        } catch (Exception e) {
            throw Exceptions.runtimeException("读取HttpRequest的body失败", e);
        }
    }

}
