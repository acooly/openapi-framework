/**
 * openapi-framework
 * <p>
 * Copyright 2014 Acooly.cn, Inc. All rights reserved.
 *
 * @author zhangpu
 * @date 2019-09-16 09:27
 */
package com.acooly.openapi.framework.common;

import com.acooly.core.utils.Servlets;
import com.acooly.core.utils.Strings;
import com.acooly.openapi.framework.common.dto.ApiMessageContext;
import com.acooly.openapi.framework.common.utils.json.JsonMarshallor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;
import java.util.TreeMap;

import static com.acooly.openapi.framework.common.ApiConstants.BODY;

/**
 * OpenApi工具类
 *
 * @author zhangpu
 * @date 2019-09-16 09:27
 */
@Slf4j
public class OpenApis {


    public static String getRequestNo(Map<String, String> requestData) {
        return requestData.getOrDefault(ApiConstants.REQUEST_NO, ApiConstants.ORDER_NO);
    }

    /**
     * 解析报文体
     *
     * @param message 报文
     * @param clazz   报文对象
     * @param <T>
     * @return
     */
    public static <T> T parseMessage(String message, Class<T> clazz) {
        return JsonMarshallor.INSTANCE.parse(message, clazz);
    }


    /**
     * 获取http请求会话信息
     *
     * @param request
     * @return
     */
    public static ApiMessageContext getApiRequestContext(HttpServletRequest request) {
        ApiMessageContext context = new ApiMessageContext();
        context.header(ApiConstants.X_API_PROTOCOL, request.getHeader(ApiConstants.X_API_PROTOCOL));
        context.header(ApiConstants.X_API_ACCESS_KEY, request.getHeader(ApiConstants.X_API_ACCESS_KEY));
        context.header(ApiConstants.X_API_SIGN_TYPE, request.getHeader(ApiConstants.X_API_SIGN_TYPE));
        context.header(ApiConstants.X_API_SIGN, request.getHeader(ApiConstants.X_API_SIGN));
        context.header(HttpHeaders.USER_AGENT, request.getHeader(HttpHeaders.USER_AGENT));
        context.setParameters(Servlets.getParameters(request));
        String body = Servlets.getBody(request);
        if (Strings.isBlank(body)) {
            body = context.getValue(BODY);
        }
        context.setBody(body);
        return context;
    }

    /**
     * 初始化响应的MessageContext
     *
     * @param apiRequestContext
     * @return
     */
    public static ApiMessageContext getApiResponseContext(ApiMessageContext apiRequestContext) {
        ApiMessageContext context = new ApiMessageContext();
        context.header(ApiConstants.X_API_PROTOCOL, apiRequestContext.getProtocol());
        context.header(ApiConstants.X_API_ACCESS_KEY, apiRequestContext.getAccessKey());
        context.header(ApiConstants.X_API_SIGN_TYPE, apiRequestContext.getSignType());
        return context;
    }


    /**
     * 生成待签字符串
     *
     * @param params
     * @return
     */
    public static String getWaitForSignString(Map<String, String> params) {
        String waitToSignStr = null;
        Map<String, String> sortedMap = new TreeMap<>(params);
        if (sortedMap.containsKey("sign")) {
            sortedMap.remove("sign");
        }
        StringBuilder stringToSign = new StringBuilder();
        if (sortedMap.size() > 0) {
            for (Map.Entry<String, String> entry : sortedMap.entrySet()) {
                if (entry.getValue() != null) {
                    stringToSign.append(entry.getKey()).append("=").append(entry.getValue()).append("&");
                }
            }
            stringToSign.deleteCharAt(stringToSign.length() - 1);
            waitToSignStr = stringToSign.toString();
        }
        return waitToSignStr;
    }

}
