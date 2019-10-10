package com.acooly.openapi.framework.service.test.web;

import com.acooly.core.utils.Servlets;
import com.acooly.openapi.framework.client.OpenApiClient;
import com.acooly.openapi.framework.common.dto.ApiMessageContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * MOCK withdraw服务客户端接受异步通知
 *
 * @author zhangpu
 * @date 2019-01-27 19:28
 */
@Slf4j
@Controller
@RequestMapping("/openapi/test/withdraw/client")
public class WithdrawClientTestController {


    @RequestMapping("notifyUrl")
    public void mockNotifyUrl(HttpServletRequest request, HttpServletResponse response) {
        OpenApiClient openApiClient = new OpenApiClient("http://localhost:8089/gateway.do",
                "test", "06f7aab08aa2431e6dae6a156fc9e0b4", true);
        ApiMessageContext apiMessageContext = openApiClient.verify(request);
        log.info("MOCK客户端 接收异步通知 验证签名: 通过");
        log.info("MOCK客户端 接收异步通知 headers: {}", apiMessageContext.getHeaders());
        log.info("MOCK客户端 接收异步通知 params : {}", apiMessageContext.getParameters());
        log.info("MOCK客户端 接收异步通知 body   : {}", apiMessageContext.getBody());
        Servlets.writeText(response, "success");
        log.info("客户端 接收成功回写 success");
    }
}
