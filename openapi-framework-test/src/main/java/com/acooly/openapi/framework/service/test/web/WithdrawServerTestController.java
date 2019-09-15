package com.acooly.openapi.framework.service.test.web;

import com.acooly.core.common.facade.ResultBase;
import com.acooly.core.utils.enums.ResultStatus;
import com.acooly.openapi.framework.common.ApiConstants;
import com.acooly.openapi.framework.common.utils.Servlets;
import com.acooly.openapi.framework.facade.api.OpenApiRemoteService;
import com.acooly.openapi.framework.facade.order.ApiNotifyOrder;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

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
    public Object mockNotify(HttpServletRequest request, HttpServletResponse response) {
        // 1、根据gid获获取和partnerId（用户分库分表的标志），这里一般是下次业务系统持久化，这里通过传入参数方式mock
        String gid = com.acooly.core.utils.Servlets.getParameter(request, ApiConstants.GID);
        String partnerId = com.acooly.core.utils.Servlets.getParameter(request, ApiConstants.PARTNER_ID);

        // 2、组织mock异步通知报文
        // 这里线mock默认值，也可以从请求报文中覆盖
        Map<String, String> parameters = Maps.newHashMap();
        parameters.put("amountIn", "199.60");
        parameters.put("fee", "0.40");
        parameters.put("delay", "T1");
        parameters.put("status", ResultStatus.success.code());
        parameters.putAll(Servlets.getParameters(request));
        ApiNotifyOrder order = new ApiNotifyOrder();
        order.setGid(gid);
        order.setPartnerId(partnerId);
        order.setParameters(parameters);
        ResultBase apiNotifyResult = openApiRemoteService.asyncNotify(order);
        log.info("MOCK下层业务服务 调用OpenApi发送异步通知 order: {}", order);
        log.info("MOCK下层业务服务 调用OpenApi发送异步通知 result: {},", apiNotifyResult);
        return apiNotifyResult;
    }
}
