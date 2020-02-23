/*
 * acooly.com Inc.
 * Copyright (c) 2016 All Rights Reserved
 */

/*
 * 修订记录:
 * kuli@yiji.com 2016-06-06 17:55 创建
 */
package com.acooly.openapi.framework.common.utils;

import com.acooly.openapi.framework.common.ApiConstants;
import com.acooly.openapi.framework.common.dto.ApiMessageContext;
import com.acooly.openapi.framework.common.exception.ApiParameterException;
import com.alibaba.fastjson.JSON;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.security.GeneralSecurityException;
import java.util.*;

import static com.acooly.openapi.framework.common.ApiConstants.BODY;

/**
 * openApi 框架专用工具类
 *
 * @author acooly
 */
public final class ApiUtils {

    public static final String USER_AGENT = "User-Agent";

    private ApiUtils() {
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
        context.header(USER_AGENT, request.getHeader(USER_AGENT));
        context.header(ApiConstants.REQUEST_IP, getIpAddr(request));
        context.setParameters(getHttpParameters(request));
        String body = null;
        try {
            body = getHttpBody(request, null);
        } catch (Exception e) {
            //ig
        }
        if (isBlank(body)) {
            body = context.getValue(BODY);
        }
        context.setBody(body);
        return context;
    }


    public static String getParameter(Map<String, String> requestData, String key) {
        return requestData.get(key);
    }

    public static boolean isJson(String json) {
        try {
            JSON.parse(json);
            String first = substring(json, 0, 1);
            String last = substring(json, json.length() - 1, json.length());
            return ("[{".contains(first) && "]}".contains(last));
        } catch (Exception e) {
            //ig
        }
        return false;
    }


    public static String getIpAddr(HttpServletRequest request) {
        if (request == null) {
            return "unknown";
        } else {
            String ip = request.getHeader("x-forwarded-for");
            if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getHeader("Proxy-Client-IP");
            }

            if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getHeader("WL-Proxy-Client-IP");
            }

            if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getRemoteAddr();
            }

            return ip;
        }
    }

    public static Map<String, String> getHttpParameters(ServletRequest request) {
        Enumeration<String> paramNames = request.getParameterNames();
        Map<String, String> params = new LinkedHashMap();
        while (paramNames != null && paramNames.hasMoreElements()) {
            String key = paramNames.nextElement();
            String value = request.getParameter(key);
            if (!isBlank(value)) {
                params.put(key, value);
            }
        }
        return params;
    }

    public static Map<String, String> transformMap(Map<String, Object> objectMap) {
        Map<String, String> map = new HashMap<>();
        for (Map.Entry<String, Object> entry : objectMap.entrySet()) {
            if (entry.getValue() != null) {
                map.put(entry.getKey(), entry.getValue().toString());
            }
        }
        return map;
    }

    public static String getHttpBody(HttpServletRequest request, String encoding) {
        try (InputStream in = request.getInputStream()) {
            if (encoding == null || "".equals(encoding)) {
                encoding = "UTF-8";
            }
            return copyToString(in, Charset.forName(encoding));
        } catch (Exception e) {
            throw new RuntimeException("读取HttpRequest的body失败", e);
        }
    }

    public static String copyToString(InputStream in, Charset charset) throws IOException {
        if (in == null) {
            return "";
        }
        StringBuilder out = new StringBuilder();
        InputStreamReader reader = new InputStreamReader(in, charset);
        char[] buffer = new char[4096];
        int bytesRead = -1;
        while ((bytesRead = reader.read(buffer)) != -1) {
            out.append(buffer, 0, bytesRead);
        }
        return out.toString();
    }


    public static boolean isNoneBlank(String text) {
        return !isBlank(text);
    }

    public static boolean isBlank(String text) {
        int strLen;
        if (text == null || (strLen = text.length()) == 0) {
            return true;
        }
        for (int i = 0; i < strLen; i++) {
            if (!Character.isWhitespace(text.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    public static String substring(final String str, int start, int end) {
        if (str == null) {
            return null;
        }

        // handle negatives
        if (end < 0) {
            // remember end is negative
            end = str.length() + end;
        }
        if (start < 0) {
            // remember start is negative
            start = str.length() + start;
        }

        // check length next
        if (end > str.length()) {
            end = str.length();
        }

        // if start is greater than end, return ""
        if (start > end) {
            return "";
        }

        if (start < 0) {
            start = 0;
        }
        if (end < 0) {
            end = 0;
        }

        return str.substring(start, end);
    }

    public static String defaultString(final String str, final String defaultStr) {
        return str == null ? defaultStr : str;
    }

    public static String trimToEmpty(final String str) {
        return str == null ? "" : str.trim();
    }

    public static boolean startsWithIgnoreCase(String text, String prefix) {
        return trimToEmpty(text).toLowerCase().startsWith(trimToEmpty(prefix).toLowerCase());
    }

    public static boolean contains(final String text, final String searchText) {
        if (isBlank(text) || isBlank(searchText)) {
            return false;
        }
        return text.indexOf(searchText) > -1;
    }

    public static String substringBeforeLast(final String str, final String separator) {
        if (isBlank(str) || isBlank(separator)) {
            return str;
        }
        final int pos = str.lastIndexOf(separator);
        if (pos == -1) {
            return str;
        }
        return str.substring(0, pos);
    }

    public static String uncapitalize(final String str) {
        int strLen;
        if (str == null || (strLen = str.length()) == 0) {
            return str;
        }

        final int firstCodepoint = str.codePointAt(0);
        final int newCodePoint = Character.toLowerCase(firstCodepoint);
        if (firstCodepoint == newCodePoint) {
            // already capitalized
            return str;
        }
        // cannot be longer than the char array
        final int[] newCodePoints = new int[strLen];
        int outOffset = 0;
        // copy the first codepoint
        newCodePoints[outOffset++] = newCodePoint;
        for (int inOffset = Character.charCount(firstCodepoint); inOffset < strLen; ) {
            final int codepoint = str.codePointAt(inOffset);
            // copy the remaining ones
            newCodePoints[outOffset++] = codepoint;
            inOffset += Character.charCount(codepoint);
        }
        return new String(newCodePoints, 0, outOffset);
    }

    public static String substringAfter(final String str, final String separator) {
        if (isBlank(str)) {
            return str;
        }
        if (separator == null) {
            return "";
        }
        final int pos = str.indexOf(separator);
        if (pos == -1) {
            return "";
        }
        return str.substring(pos + separator.length());
    }

    public static void notBlank(Object object, String message) {
        if (object == null) {
            if (isBlank(message)) {
                message = "参数不能为空值或空字符串";
            }
            throw new ApiParameterException(message);
        }
    }


    public static byte[] aes(byte[] input, byte[] key, int mode) {
        try {
            SecretKey secretKey = new SecretKeySpec(key, "AES");
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(mode, secretKey);
            return cipher.doFinal(input);
        } catch (GeneralSecurityException e) {
            throw new RuntimeException(e);
        }
    }

    public static String urlEncode(String part) {
        try {
            return URLEncoder.encode(part, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    public static String urlDecode(String part) {

        try {
            return URLDecoder.decode(part, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }
}
