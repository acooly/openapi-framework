package com.acooly.openapi.framework.service.test.web;

import com.acooly.core.utils.mapper.BeanCopier;
import com.acooly.openapi.framework.common.utils.Exceptions;
import com.acooly.openapi.framework.common.utils.Servlets;
import com.acooly.openapi.framework.common.utils.json.JsonMarshallor;
import com.acooly.openapi.framework.facade.api.OpenApiRemoteService;
import com.acooly.openapi.framework.facade.order.ApiNotifyOrder;
import com.acooly.openapi.framework.facade.result.ApiNotifyResult;
import com.acooly.openapi.framework.service.test.enums.OrderPayStatus;
import com.acooly.openapi.framework.service.test.response.OrderCashierPayNotify;
import com.acooly.openapi.framework.service.test.response.OrderCashierPayRedirect;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

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
@RequestMapping("/openapi/test/server")
public class OrderCashierPayServerTestController {

    // 也可以是注入dubbo客户端
    // @Reference(version = "1.0")
    @Autowired
    private OpenApiRemoteService openApiRemoteService;

    @RequestMapping("cashier")
    public void mockCashier(HttpServletRequest request, HttpServletResponse response) throws Exception {
        // 1、接收和解析OpenApi跳转来的请求参数
        String jsonString = getBody(request);
        log.info("收银台 接收跳转请求参数：\n{}", jsonString);
        // 这里需要对跳转过来的参数进行解析，可以选择JSON.parse，这里简单使用OpenApi的工具处理
        OrderCashierPayRedirect redirect = JsonMarshallor.INSTANCE.parse(jsonString, OrderCashierPayRedirect.class);
        log.info("收银台 解析跳转请求参数: {}", redirect);

        // 2、do business
        log.info("显示界面,用户交互操作，MOCK处理完成后，准备通知客户端处理结果...");

        // 3、组装回跳给客户端的数据，并调用OpenApi-facade接口进行前面和组装URL
        // 同步报文这里假设为processing
        final OrderCashierPayNotify notify = new OrderCashierPayNotify();
        BeanCopier.copy(redirect, notify);
        notify.setPayStatus(OrderPayStatus.processing);

        Map<String, String> parameters = Maps.newHashMap();
        BeanCopier.copy(notify, parameters);
        // 处理完成后，组装跳转返回给请求客户端的参数。这里需要根据OpenAPI对外定义的服务的Notify报文传入通知参数
        final ApiNotifyOrder order = new ApiNotifyOrder();
        order.setParameters(parameters);
        ApiNotifyResult apiNotifyResult = openApiRemoteService.syncReturn(order);

        // 4、直接跳转到客户端URL

        String retrunUrl = apiNotifyResult.getCompleteReturnUrl();
        log.info("收银台 同步通知 returnUrl: {}", retrunUrl);
        Servlets.redirect(response, retrunUrl);

        // 5、这里直接MOCK启动新线程进行异步通知

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(2 * 1000);
                } catch (Exception e) {
                    //ig
                }
                // 设置异步通知结果为成功
                log.info("收银台 异步通知 等待2秒后启动...");
                order.setParameter("payStatus", OrderPayStatus.success.code());
                openApiRemoteService.asyncNotify(order);
            }
        });
        thread.start();


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
