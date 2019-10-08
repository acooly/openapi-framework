package com.acooly.openapi.framework.service.test.web;

import com.acooly.core.common.web.AbstractStandardEntityController;
import com.acooly.core.utils.Money;
import com.acooly.core.utils.Servlets;
import com.acooly.openapi.framework.client.OpenApiClient;
import com.acooly.openapi.framework.common.dto.ApiMessageContext;
import com.acooly.core.utils.Ids;
import com.acooly.openapi.framework.service.test.request.OrderCashierPayApiRequest;
import com.acooly.openapi.framework.service.test.request.OrderCreateApiRequest;
import com.acooly.openapi.framework.service.test.response.OrderCreateApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 接入方MOCK 跳转支付客户端的服务
 * <p>
 * 这里提供请求支付端的：returnUrl和notifyUrl
 *
 * @author zhangpu
 * @date 2019-01-27 19:28
 */
@Slf4j
@Controller
@RequestMapping("/openapi/test/orderCashierPay/client")
public class OrderCashierPayClientTestController extends AbstractStandardEntityController {

    String payerUserId = "09876543211234567890";

    /**
     * 注意：
     * 1、openApiClient可以通过参数配置：AccessKey,SecretKey和网关地址
     * 2、可以手动通过构造函数设置和初始化
     */
    @Autowired
    private OpenApiClient openApiClient;


    /**
     * 接入方：订单支付界面：MOCK
     *
     * @param request
     * @param response
     */
    @Override
    public String index(HttpServletRequest request, HttpServletResponse response, Model model) {
        model.addAttribute("merchOrderNo", Ids.getDid());
        return "/openapi/test/cashier/clientPay";
    }

    /**
     * 创建支付订单
     *
     * @param httpRequest
     */
    private OrderCreateApiRequest getOrderCreateApiRequest(HttpServletRequest httpRequest) {
        OrderCreateApiRequest request = new OrderCreateApiRequest();
        try {
            bindNotValidator(httpRequest, request);
        } catch (Exception e) {
            //ig;
        }

        request.setService("orderCreate");
        request.setRequestNo(Ids.getDid());
        request.setPayerUserId(request.getPayeeUserId());
        request.setBuyerUserId("09876543211234567890");
        request.setBuyeryEmail("zhangpu@acooly.cn");
        request.setBuyerCertNo("330702194706165014");
        request.setPassword("12312312");
        return request;
    }

    /**
     * 接入方：Mock客户端视图收集用户支付信息，并组装后请求OpenAPi跳转支付接口
     *
     * @param request
     * @param response
     */
    @RequestMapping("pay")
    public void mockPay(HttpServletRequest request, HttpServletResponse response) {
        // 1.创建支付订单 mock
        OrderCreateApiRequest orderCreateApiRequest = getOrderCreateApiRequest(request);
        openApiClient.send(orderCreateApiRequest, OrderCreateApiResponse.class);

        // 2.请求支付网关收银台跳转支付
        String merchOrderNo = orderCreateApiRequest.getMerchOrderNo();
        String amount = orderCreateApiRequest.getAmount().toString();
        OrderCashierPayApiRequest apiRequest = new OrderCashierPayApiRequest();
        apiRequest.setRequestNo(Ids.getDid());
        apiRequest.setService("orderCashierPay");
        apiRequest.setMerchOrderNo(merchOrderNo);
        apiRequest.setAmount(Money.amout(amount));
        apiRequest.setPayerUserId(payerUserId);
        apiRequest.setReturnUrl("http://127.0.0.1:8089/openapi/test/orderCashierPay/client/returnUrl.html");
        apiRequest.setNotifyUrl("http://127.0.0.1:8089/openapi/test/orderCashierPay/client/notifyUrl.html");
        ApiMessageContext messageContext = openApiClient.parse(apiRequest);
        // 可以传参到页面通过页面POST提交（URL:messageResult.getUrl(), Post参数：messageResult.getAllParameters()）
        // 或则这里直接redirect
        String redirectUrl = messageContext.buildRedirectUrl();
        Servlets.redirect(response, redirectUrl);
    }

    @RequestMapping("returnUrl")
    @ResponseBody
    public Object mockReturnUrl(HttpServletRequest request) {
        ApiMessageContext messageContext = null;
        try {
            messageContext = openApiClient.verify(request);
            log.info("客户端 接收同步通知 验签成功。");
            log.info("客户端 接收同步通知 header: {}", messageContext.getHeaders());
            log.info("客户端 接收同步通知 params: {}", messageContext.getParameters());
            log.info("客户端 接收同步通知 body: {}", messageContext.getBody());
        } catch (Exception e) {
            log.info("客户端 接收同步通知 验签失败！");
        }
        return messageContext.getParameters();
    }


    @RequestMapping("notifyUrl")
    public void mockNotifyUrl(HttpServletRequest request, HttpServletResponse response) {
        try {
            ApiMessageContext messageContext = openApiClient.verify(request);
            log.info("客户端 接收异步通知 验签成功。");
            log.info("客户端 接收异步通知 header: {}", messageContext.getHeaders());
            log.info("客户端 接收异步通知 params: {}", messageContext.getParameters());
            log.info("客户端 接收异步通知 body: {}", messageContext.getBody());
            Servlets.writeText(response, "success");
            log.info("客户端 接收异步通知 回写 success");
        } catch (Exception e) {
            log.info("客户端 接收异步通知 验签失败！");
        }

    }


}
