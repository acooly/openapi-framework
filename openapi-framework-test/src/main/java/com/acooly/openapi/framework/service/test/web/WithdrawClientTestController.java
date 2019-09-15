package com.acooly.openapi.framework.service.test.web;

import com.acooly.core.utils.Servlets;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

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
        Map<String, String> headers = Servlets.getHeaders(request, "x-api");
        Map<String, String> parameters = Servlets.getParameters(request);
        String body = Servlets.getBody(request);
        log.info("MOCK客户端 接收异步通知 headers: {}", headers);
        log.info("MOCK客户端 接收异步通知 params : {}", parameters);
        log.info("MOCK客户端 接收异步通知 body   : {}", body);
        Servlets.writeText(response, "success");
        log.info("客户端 接收成功回写 success");
    }
}
