/*
 * acooly.com Inc.
 * Copyright (c) 2016 All Rights Reserved
 */

/*
 * 修订记录:
 * kuli@yiji.com 2016-06-06 17:55 创建
 */
package com.acooly.openapi.framework.common.utils;

import com.acooly.core.utils.Servlets;
import com.acooly.core.utils.Strings;
import com.acooly.core.utils.system.IPUtil;
import com.acooly.openapi.framework.common.ApiConstants;
import com.acooly.openapi.framework.common.dto.ApiMessageContext;
import com.acooly.openapi.framework.common.enums.ApiServiceResultCode;
import com.acooly.openapi.framework.common.exception.ApiServiceException;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.net.HttpHeaders;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.validator.routines.UrlValidator;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;
import java.util.TreeMap;

import static com.acooly.openapi.framework.common.ApiConstants.BODY;

/**
 * openApi 框架专用工具类
 *
 * @author acooly
 */
@Slf4j
public final class ApiUtils {

    private static UrlValidator httpUrlValidator = null;

    static {
        String[] schemes = {"http", "https"};
        httpUrlValidator = new UrlValidator(schemes);
    }

    private ApiUtils() {
    }

    public static Map parseJsonBody(String jsonBody) {
        try {
            return (JSONObject) JSON.parse(jsonBody);
        } catch (Exception e) {
            throw new ApiServiceException(ApiServiceResultCode.JSON_BODY_PARSING_FAILED);
        }
    }

    /**
     * 兼容老协议的：requestNo和orderNo
     *
     * @param requestData
     * @return
     */
    public static String getRequestNo(Map<String, String> requestData) {
        return requestData.getOrDefault(ApiConstants.REQUEST_NO, ApiConstants.ORDER_NO);
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
     * 获取http请求会话信息
     * 主要包括：请求头，请求参数和请求体，并对参数的获取做了协议兼容和适配（getValue）
     * 可用于下层服务接受跳转请求时，解析请求参数的第一步。
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
        context.header(ApiConstants.REQUEST_IP, IPUtil.getIpAddr(request));
        context.setParameters(Servlets.getParameters(request));
        String body = null;
        try {
            body = Servlets.getBody(request);
        } catch (Exception e) {
            log.error("读取请求报文体失败: {}", e.getMessage());
        }
        if (Strings.isBlank(body)) {
            body = context.getValue(BODY);
        }
        context.setBody(body);
        return context;
    }


    public static boolean isHttpUrl(String str) {
        return httpUrlValidator.isValid(str);
    }

    public static void checkOpenAPIUrl(String str, String name) {
        if (Strings.isBlank(str)) {
            throw new ApiServiceException(ApiServiceResultCode.PARAMETER_ERROR, name + "不能为空");
        }
        if (isHttpUrl(str)) {
            if (str.contains("?")) {
                throw new ApiServiceException(
                        ApiServiceResultCode.PARAMETER_ERROR, "必须传入格式正确的" + name + "参数,请求参数不能包含?");
            }
        } else {
            throw new ApiServiceException(
                    ApiServiceResultCode.PARAMETER_ERROR, "必须传入格式正确的" + name + "参数");
        }
    }

    public static String getParameter(Map<String, String> requestData, String key) {
        return requestData.get(key);
    }

    public static boolean isJson(String json) {
        try {
            JSON.parse(json);
            String first = Strings.substring(json, 0, 1);
            String last = Strings.substring(json, json.length() - 1, json.length());
            return (Strings.contains("[{", first) && Strings.contains("]}", last));
        } catch (Exception e) {
            //ig
        }
        return false;
    }


    @Override
    public String toString() {
        return super.toString();
    }
}
