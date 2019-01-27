package com.acooly.openapi.framework.service.test.web;

import com.acooly.core.utils.Servlets;
import com.acooly.openapi.framework.common.utils.Exceptions;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.io.InputStream;
import java.io.StringWriter;
import java.nio.charset.Charset;
import java.util.Map;

/**
 * @author zhangpu
 * @date 2019-01-27 19:28
 */
@Slf4j
@Controller
@RequestMapping("/openapi/test")
public class OrderPayNotifyReceiverMockController {


    @RequestMapping("notifyUrl")
    public void mockNotifyUrl(HttpServletRequest request) {
        Map<String, String> headers = Servlets.getHeaders(request, "x-api");
        log.info("Notify Header:\n");
        for (Map.Entry<String, String> entry : headers.entrySet()) {
            System.out.println(entry.getKey() + ": " + entry.getValue());
        }
        log.info("Notify Body:\n");
        log.info(getBody(request));
    }

    @RequestMapping("returnUrl")
    public Object mockReturnUrl(HttpServletRequest request) {
        Map<String, String> data = Servlets.getHeaders(request, "x-api");
        log.info("return Header:\n");
        for (Map.Entry<String, String> entry : data.entrySet()) {
            System.out.println(entry.getKey() + ": " + entry.getValue());
        }
        data.put("body", getBody(request));
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
