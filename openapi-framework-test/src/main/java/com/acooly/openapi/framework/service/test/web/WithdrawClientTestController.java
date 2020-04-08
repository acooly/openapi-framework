package com.acooly.openapi.framework.service.test.web;

import com.acooly.core.utils.Servlets;
import com.acooly.openapi.framework.common.OpenApiTools;
import com.acooly.openapi.framework.demo.message.notify.WithdrawApiNotify;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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
@RequestMapping("/openapi/demo/withdraw/client")
public class WithdrawClientTestController {

    @Autowired(required = false)
    OpenApiTools openApiTools = new OpenApiTools("http://localhost:8089/gateway.do",
            "test", "06f7aab08aa2431e6dae6a156fc9e0b4", true);

    @RequestMapping("notifyUrl")
    public void mockNotifyUrl(HttpServletRequest request, HttpServletResponse response) {


        try {
            // 1、接收通知报文；2、验签和解密；3、组织报文为报文对象
            WithdrawApiNotify withdrawApiNotify = openApiTools.notice(request, WithdrawApiNotify.class);
            if (!withdrawApiNotify.isSuccess()) {
                // 通知的业务失败，但通知是正常接收的。你需要进行对应的业务处理。
                log.warn("提现 [客户端] 异步通知业务不成功");
            }
            log.info("提现 [客户端] 接收异步通知：成功 {}", JSON.toJSONString(withdrawApiNotify));
            Servlets.writeText(response, "success");
            log.info("提现 [客户端] 接收异步通知 回写success");
        } catch (Exception e) {
            // 你的异常处理;
            log.error("提现 [客户端] 接收异步通知 失败: {}", e.getMessage());
            Servlets.writeText(response, "failure");
        }
    }
}
