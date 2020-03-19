package com.acooly.openapi.framework.demo.service.web;

import com.acooly.core.common.exception.BusinessException;
import com.acooly.core.common.facade.ResultBase;
import com.acooly.core.common.web.AbstractStandardEntityController;
import com.acooly.core.utils.Profiles;
import com.acooly.core.utils.Servlets;
import com.acooly.core.utils.Strings;
import com.acooly.openapi.framework.common.dto.ApiMessageContext;
import com.acooly.openapi.framework.common.message.ApiNotify;
import com.acooly.openapi.framework.demo.message.enums.OrderPayStatus;
import com.acooly.openapi.framework.demo.message.notify.OrderCashierPayNotify;
import com.acooly.openapi.framework.demo.message.response.OrderCashierPayApiRedirect;
import com.acooly.openapi.framework.facade.OpenApis;
import com.acooly.openapi.framework.facade.api.OpenApiRemoteService;
import com.acooly.openapi.framework.facade.order.ApiNotifyOrder;
import com.acooly.openapi.framework.facade.order.ApiVerifyOrder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * MOCK 跳转支付客户端的服务
 * <p>
 * 这里提供请求支付端的：returnUrl和notifyUrl
 *
 * @author zhangpu
 * @date 2019-01-27 19:28
 */
@Slf4j
@Profile({"default", "dev", "sdev", "test", "stest", "net", "snet"})
@Controller
@RequestMapping("/openapi/demo/orderCashierPay/server")
public class OrderCashierPayServerTestController extends AbstractStandardEntityController {

    // 也可以是注入dubbo客户端
    // @Reference(version = "1.0")
    @Autowired
    private OpenApiRemoteService openApiRemoteService;

    /**
     * 接受跳转请求，验证参数，解析数据并显示收银台界面
     * <p>
     * 一步搞定：解析请求，验证请求和解析报文
     *
     * @param request
     * @param response
     * @param model
     * @return
     */
    @RequestMapping("cashier")
    public String mockCashier(HttpServletRequest request, HttpServletResponse response, Model model) {
        OpenApis.ApiRedirectContext<OrderCashierPayApiRedirect> context = null;
        try {
            // 一步搞定：解析请求，验证请求和解析报文
            context = OpenApis.redirectParse(request, OrderCashierPayApiRedirect.class, openApiRemoteService);
            log.info("收银台 显示界面: {}", "/openapi/demo/cashier/serverCashier");
            // 传值到界面
            model.addAttribute("entity", context.getApiRedirect());
            model.addAttribute("gid", context.getGid());
            model.addAttribute("partnerId", context.getPartnerId());
        } catch (Exception e) {
            // 如果验签失败（非法请求），则直接跳回或按需显示错误界面。
            final OrderCashierPayNotify notify = new OrderCashierPayNotify();
            handleException(response, e, context);
            redirectBack(response, context, notify);
        }
        return "/openapi/demo/cashier/serverCashier";
    }

    /**
     * 逻辑处理同:cashier方法。
     * 分三步搞定：解析请求，验证请求和解析报文
     */
    @RequestMapping("cashier1")
    public String mockCashier1(HttpServletRequest request, HttpServletResponse response, Model model) {
        // 1、接收跳转来的请求参数,OpenApis工具类在acooly-openapi-common中
        // 注意：从OpenApi跳转到下层的请求，因为受Redirect协议的限制，没有body体和header头，全部通过QueryString传入值。
        // 下面的ApiMessageContext提供的所有获取关键值的方法都兼容头参数和体参数
        ApiMessageContext messageContext = OpenApis.redirectParseRequest(request);
        log.info("收银台 接收跳转参数：{}", messageContext.getParameters());
        try {
            // 2、调用OpenApi远程服务验证签名
            ApiVerifyOrder apiVerifyOrder = new ApiVerifyOrder(messageContext);
            ResultBase resultBase = openApiRemoteService.verify(apiVerifyOrder);
            log.info("收银台 验签：{}", resultBase.success());
            if (!resultBase.success()) {
                throw new BusinessException(resultBase.getStatus());
            }

            // 3、do business，根据拿到的redirect报文进行业务操作
            // 这里需要对跳转过来的参数进行解析，可以选择JSON.parseRequest，这里简单使用OpenApi的工具处理
            // 请引用对应的openapi-message模块，已找到你需要的Redirect对象
            OrderCashierPayApiRedirect redirect = OpenApis.redirectParseMessage(messageContext.getBody(), OrderCashierPayApiRedirect.class);
            log.info("收银台 解析参数: {}", redirect);
            log.info("收银台 显示界面: {}", "/openapi/demo/cashier/serverCashier");
            // 传值到界面
            model.addAttribute("entity", redirect);
            model.addAttribute("gid", messageContext.getGid());
            model.addAttribute("partnerId", messageContext.getPartnerId());
        } catch (Exception e) {
            // 如果验签失败（非法请求），则直接跳回。
            final OrderCashierPayNotify notify = new OrderCashierPayNotify();
            handleException(response, e, messageContext);
            redirectBack(response, messageContext, notify);
        }
        return "/openapi/demo/cashier/serverCashier";
    }


    /**
     * MOCK 收银台支付处理
     *
     * @param request
     * @param response
     * @param model
     * @throws Exception
     */
    @RequestMapping("cashierPay")
    public String mockCashierPay(HttpServletRequest request, HttpServletResponse response, Model model) throws Exception {

        ApiMessageContext messageContext = OpenApis.redirectParseRequest(request);
        try {
            // 1、接收支付请求参数。这里直接使用redirect报文作为数据实体，你可以根据实际情况使用交易系统实际的数据实体。
            OrderCashierPayApiRedirect redirect = new OrderCashierPayApiRedirect();
            bindNotValidator(request, redirect);
            log.info("收银台 支付 接受界面的支付请求。数据:{} ", redirect);

            // 2、支付逻辑处理
            log.info("收银台 支付 逻辑处理，比如：调用交易余额支付等...");

            // 3. 支付成功，同步通知：跳回客户请求页面（returnUrl）
            // 全局准备会跳对象，同步跳回通知和异步通知建议使用一个报文结构
            final OrderCashierPayNotify notify = new OrderCashierPayNotify();
            notify.setAmount(redirect.getAmount());
            notify.setMerchOrderNo(redirect.getMerchOrderNo());
            notify.setPayerUserId(redirect.getPayerUserId());
            OrderPayStatus orderPayStatus = OrderPayStatus.processing;
            String payStatus = Servlets.getParameter(request, "payStatus");
            if (Strings.isNotBlank(payStatus)) {
                orderPayStatus = OrderPayStatus.find(payStatus);
            }
            notify.setPayStatus(orderPayStatus);
            ApiNotifyOrder apiNotifyOrder = redirectBack(response, messageContext, notify);

            // 4. 调用异步通知

            callOpenApiNotify(apiNotifyOrder, orderPayStatus);
        } catch (Exception e) {
            // 注意：真实实现的时候，这里需要判断是否服务端收银台自己需要处理的错误，并跳回收银台对应的界面
            handleException(response, e, messageContext);
        }

        return null;
    }

    /**
     * 错误处理
     * <p>
     * 发生错误，则设置错误码和消息，会跳给客户端
     *
     * @param response
     * @param e
     * @param messageContext
     */
    protected void handleException(HttpServletResponse response, Exception e, ApiMessageContext messageContext) {
        final OrderCashierPayNotify notify = new OrderCashierPayNotify();
        if (e instanceof BusinessException) {
            BusinessException be = (BusinessException) e;
            notify.setCode(be.getCode());
            notify.setMessage(be.getMessage());
        }
        redirectBack(response, messageContext, notify);
    }


    /**
     * 跳回客户端
     *
     * @param response
     * @param messageContext
     * @param apiNotify
     * @return
     */
    protected ApiNotifyOrder redirectBack(HttpServletResponse response, ApiMessageContext messageContext, ApiNotify apiNotify) {
        final ApiNotifyOrder order = new ApiNotifyOrder();
        order.setNotifyMessage(apiNotify);
        order.setGid(messageContext.getGid());
        order.setPartnerId(messageContext.getPartnerId());
        OpenApis.redirectSendBack(response, order, openApiRemoteService);
        return order;
    }

    /**
     * 调用OpenApi发送异步通知
     *
     * @param order
     */
    protected void callOpenApiNotify(final ApiNotifyOrder order, final OrderPayStatus orderPayStatus) {
        // 5、异步通知：这里直接MOCK启动新线程进行异步通知
        new Thread(() -> {
            log.info("收银台 异步通知 等待2秒后启动...");
            try {
                Thread.sleep(2 * 1000);
            } catch (Exception e) {
            }
            // 设置异步通知结果为成功

            OrderCashierPayNotify apiNotify = (OrderCashierPayNotify) order.getNotifyMessage();
            if (orderPayStatus == OrderPayStatus.failure) {
                apiNotify.setPayStatus(OrderPayStatus.failure);
            } else {
                apiNotify.setPayStatus(OrderPayStatus.success);
            }
            openApiRemoteService.asyncNotify(order);
        }).start();

    }


}
