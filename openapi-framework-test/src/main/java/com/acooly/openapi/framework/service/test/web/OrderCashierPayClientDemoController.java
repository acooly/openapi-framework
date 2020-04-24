package com.acooly.openapi.framework.service.test.web;

import com.acooly.core.common.web.AbstractStandardEntityController;
import com.acooly.core.utils.Ids;
import com.acooly.core.utils.Money;
import com.acooly.core.utils.Servlets;
import com.acooly.openapi.framework.client.OpenApiClient;
import com.acooly.openapi.framework.common.ApiConstants;
import com.acooly.openapi.framework.common.OpenApiTools;
import com.acooly.openapi.framework.common.dto.ApiMessageContext;
import com.acooly.openapi.framework.demo.message.notify.DemoOrderCashierPayNotify;
import com.acooly.openapi.framework.demo.message.request.DemoOrderCashierPayApiRequest;
import com.acooly.openapi.framework.demo.message.request.DemoOrderCreateApiRequest;
import com.acooly.openapi.framework.demo.message.response.DemoOrderCreateApiResponse;
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
@RequestMapping("/openapi/demo/orderCashierPay/client")
public class OrderCashierPayClientDemoController extends AbstractStandardEntityController {

    String payerUserId = "09876543211234567890";

    /**
     * 注意：
     * 1、openApiClient可以通过参数配置：AccessKeys,SecretKey和网关地址
     * 2、可以手动通过构造函数设置和初始化
     */
    @Autowired
    private OpenApiClient openApiClient;

    @Autowired(required = false)
    OpenApiTools openApiTools = new OpenApiTools("http://localhost:8089/gateway.do",
            ApiConstants.TEST_ACCESS_KEY, ApiConstants.TEST_SECRET_KEY, true);

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
    private DemoOrderCreateApiRequest getOrderCreateApiRequest(HttpServletRequest httpRequest) {
        DemoOrderCreateApiRequest request = new DemoOrderCreateApiRequest();
        try {
            bindNotValidator(httpRequest, request);
        } catch (Exception e) {
            //ig;
        }

        request.setAmount(Money.amout("1200"));
        request.setBuyeryMobileNo("13387648765");
        request.setPayeeUserId(Ids.did());
        request.setMerchOrderNo(Ids.did());
        request.setTitle("测试订单");

        request.setService("demoOrderCreate");
        request.setRequestNo(Ids.getDid());
        request.setPayerUserId(request.getPayeeUserId());
        request.setBuyerUserId("09876543211234567890");
        request.setBuyeryEmail("zhangpu@acooly.cn");
        request.setBuyerCertNo("330702194706165014");
        request.setPassword("12312312");
        request.setContext("这是同步接口demoOrderCreate的context内容");
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
        DemoOrderCreateApiRequest orderCreateApiRequest = getOrderCreateApiRequest(request);
        openApiTools.send(orderCreateApiRequest, DemoOrderCreateApiResponse.class);

        // 2.请求支付网关收银台跳转支付
        String merchOrderNo = orderCreateApiRequest.getMerchOrderNo();
        String amount = orderCreateApiRequest.getAmount().toString();
        DemoOrderCashierPayApiRequest apiRequest = new DemoOrderCashierPayApiRequest();
        apiRequest.setRequestNo(Ids.gid());
        apiRequest.setService("demoOrderCashierPay");
        apiRequest.setMerchOrderNo(merchOrderNo);
        apiRequest.setAmount(Money.amout(amount));
        apiRequest.setPayerUserId(payerUserId);
        apiRequest.setContext("这是跳转接口demoOrderCashierPay的context内容");
        apiRequest.setReturnUrl("http://127.0.0.1:8089/openapi/demo/orderCashierPay/client/returnUrl.html");
        apiRequest.setNotifyUrl("http://127.0.0.1:8089/openapi/demo/orderCashierPay/client/notifyUrl.html");
        openApiTools.redirectSend(apiRequest, response);
    }

    @RequestMapping("returnUrl")
    @ResponseBody
    public Object mockReturnUrl(HttpServletRequest request) {
        ApiMessageContext messageContext = null;
        try {
            DemoOrderCashierPayNotify orderCashierPayNotify = openApiTools.notice(request, DemoOrderCashierPayNotify.class);
            messageContext = openApiClient.verify(request);
            log.info("客户端 接收同步通知 成功 {}", orderCashierPayNotify);
        } catch (Exception e) {
            log.info("客户端 接收同步通知 处理失败：{}", e.getMessage());
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
