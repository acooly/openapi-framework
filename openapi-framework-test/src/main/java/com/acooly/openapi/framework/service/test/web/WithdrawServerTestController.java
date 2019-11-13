package com.acooly.openapi.framework.service.test.web;

import com.acooly.core.common.facade.ResultBase;
import com.acooly.core.utils.Ids;
import com.acooly.core.utils.Money;
import com.acooly.core.utils.Servlets;
import com.acooly.core.utils.enums.ResultStatus;
import com.acooly.openapi.framework.common.ApiConstants;
import com.acooly.openapi.framework.facade.api.OpenApiRemoteService;
import com.acooly.openapi.framework.facade.order.ApiNotifyOrder;
import com.acooly.openapi.framework.service.test.notify.WithdrawApiNotify;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * MOCK 下层系统通过facade调用OpenApi-notify通知客户端（notifyUrl）
 *
 * @author zhangpu
 * @date 2019-01-27 19:28
 */
@Slf4j
@Controller
@RequestMapping("/openapi/test/withdraw/server")
public class WithdrawServerTestController {

    // 也可以是注入dubbo客户端
    // @Reference(version = "1.0")
    @Autowired
    private OpenApiRemoteService openApiRemoteService;

    /**
     * Mock下层业务系统通过facade调用OpenApi对外异步通知
     *
     * @param request
     * @param response
     */
    @RequestMapping("notifyCall")
    @ResponseBody
    public Object mockAsyncNotify(HttpServletRequest request, HttpServletResponse response) {
        // 1、根据gid获获取和partnerId（用户分库分表的标志），这里一般是下次业务系统持久化，这里通过传入参数方式mock
        String gid = Servlets.getParameter(request, ApiConstants.GID);
        String partnerId = Servlets.getParameter(request, ApiConstants.PARTNER_ID);
        String merchOrderNo = Servlets.getParameter(request, ApiConstants.MERCH_ORDER_NO);

        // 2、组织mock异步通知报文
        // 这里线mock默认值，也可以从请求报文中覆盖
        // 注意：异步通知的协议是以发送请求时的协议为准
        WithdrawApiNotify notify = new WithdrawApiNotify();
        notify.setMerchOrderNo(merchOrderNo);
        notify.setAmountIn(Money.amout("199.60"));
        notify.setFee(Money.amout("0.40"));
        notify.setStatus(ResultStatus.success);
        ApiNotifyOrder order = new ApiNotifyOrder();
        order.setGid(gid);
        order.setPartnerId(partnerId);
        order.setNotifyMessage(notify);
        ResultBase apiNotifyResult = openApiRemoteService.asyncNotify(order);
        log.info("MOCK下层业务服务 调用OpenApi发送异步通知 order: {}", order);
        log.info("MOCK下层业务服务 调用OpenApi发送异步通知 result: {},", apiNotifyResult);
        return apiNotifyResult;
    }


    /**
     * Mock下层业务系统通过facade调用OpenApi对外发送无订单异步通知
     * <p>
     * 用于单边通知场景。例如：外部的动账通知从渠道接受后，业务处理后，穿透通知给接入方。
     * 注意：无订单通知的ApiNotifyOrder中parameters参数无效，必须采用ApiNotify对象报文方式组装通知报文数据，
     * 也就是说必须由OpenApi层先定义通知报文对象，保障OpenApi层对通知可识别，可文档化。
     *
     * @param request
     * @param response
     */
    @RequestMapping("sendNotify")
    @ResponseBody
    public Object mockSendNotify(HttpServletRequest request, HttpServletResponse response) {
        // 1、根据gid获获取和partnerId（用户分库分表的标志），这里一般是下次业务系统持久化，这里通过传入参数方式mock
        String gid = Servlets.getParameter(request, ApiConstants.GID);
        String partnerId = Servlets.getParameter(request, ApiConstants.PARTNER_ID);
        String merchOrderNo = Servlets.getParameter(request, ApiConstants.MERCH_ORDER_NO);

        // 2、组织mock异步通知报文
        // 注意：异步通知的协议是以发送请求时的协议为准
        WithdrawApiNotify notify = new WithdrawApiNotify();
        // 必选报文
        notify.setRequestNo(Ids.getDid());
        // 注意：因为没有OpenApi的原始订单，所以默认协议是JSON，这里可以设置为老协议
//        notify.setProtocol(ApiProtocol.HTTP_FORM_JSON);
        notify.setService("withdraw");

        // 业务报文
        notify.setMerchOrderNo(merchOrderNo);
        notify.setAmountIn(Money.amout("199.60"));
        notify.setFee(Money.amout("0.40"));
        notify.setStatus(ResultStatus.success);

        // 组织发送通知的order
        ApiNotifyOrder order = new ApiNotifyOrder();
        order.setGid(gid);
        order.setPartnerId(partnerId);
        order.setUrl("http://127.0.0.1:8089/openapi/test/withdraw/client/notifyUrl.html");
        order.setNotifyMessage(notify);
        // 发送无请求订单异步通知
        ResultBase apiNotifyResult = openApiRemoteService.sendNotify(order);
        log.info("MOCK下层业务服务 调用OpenApi发送无订单异步通知 order: {}", order);
        log.info("MOCK下层业务服务 调用OpenApi发送无订单异步通知 result: {},", apiNotifyResult);
        return apiNotifyResult;
    }

}
