package com.acooly.openapi.framework.common;

import com.acooly.openapi.framework.common.annotation.OpenApiField;
import com.acooly.openapi.framework.common.dto.ApiMessageContext;
import com.acooly.openapi.framework.common.enums.ApiProtocol;
import com.acooly.openapi.framework.common.enums.ApiServiceResultCode;
import com.acooly.openapi.framework.common.enums.SignType;
import com.acooly.openapi.framework.common.exception.ApiClientException;
import com.acooly.openapi.framework.common.exception.ApiServiceException;
import com.acooly.openapi.framework.common.message.ApiMessage;
import com.acooly.openapi.framework.common.message.ApiRequest;
import com.acooly.openapi.framework.common.utils.ApiUtils;
import com.acooly.openapi.framework.common.utils.json.JsonMarshallor;
import com.github.kevinsawicki.http.HttpRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.DigestUtils;

import javax.crypto.Cipher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Field;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * OpenApi 通用客户端工具
 * 不依赖ACOOLY，支持OpenApi-V5
 *
 * @author zhangpu@acooly.cn
 * @date 2020-02-23
 */
@Slf4j
public class OpenApiTools {
    private static final String DEFAULT_VERSION = "1.0";
    private static final int HTTP_STATUS_REDIRECT_START = 300;
    private static final int HTTP_STATUS_REDIRECT_END = 400;
    private static Map<Class, List<Field>> SECURITY_FIELDS_CACHE = new ConcurrentHashMap();

    /**
     * 网关访问参数
     */
    private String signType = SignType.MD5.code();
    private String gatewayUrl = "http://127.0.0.1:8089";
    private String accessKey = "test";
    private String secretKey = "06f7aab08aa2431e6dae6a156fc9e0b4";

    /**
     * 通讯参数
     */
    private int readTimeout = 30 * 1000;
    private int connTimeout = 10 * 1000;
    private String charset = "utf-8";

    private boolean showLog = true;

    public OpenApiTools() {
    }

    public OpenApiTools(String gatewayUrl, String accessKey, String secretKey) {
        this(gatewayUrl, accessKey, secretKey, true);
    }

    public OpenApiTools(String gatewayUrl, String accessKey, String secretKey, boolean showLog) {
        ApiUtils.notBlank(gatewayUrl, "网关地址不能为空");
        ApiUtils.notBlank(accessKey, "accessKey不能为空");
        ApiUtils.notBlank(secretKey, "secretKey不能为空");
        this.gatewayUrl = gatewayUrl;
        this.accessKey = accessKey;
        this.secretKey = secretKey;
        this.showLog = showLog;
    }

    /**
     * [同步接口] 发送同步请求到网关.
     * 并对标注加密的字段自动加密
     *
     * @param request 请求对象
     * @param clazz   响应类型
     * @param <T>     响应类
     * @return 响应对象
     */
    public <T> T send(ApiRequest request, Class<T> clazz) {
        ApiMessageContext context = parseRequest(request);
        if (showLog) {
            log.info("请求-> body:{},header:{} ", context.getBody(), context.getHeaders());
        }
        HttpRequest httpRequest = HttpRequest.post(this.gatewayUrl)
                .readTimeout(this.readTimeout).connectTimeout(this.connTimeout)
                .contentType(getContextType(request.getProtocol()), this.charset)
                .trustAllCerts().trustAllHosts().followRedirects(false)
                .headers(context.getHeaders()).send(context.getBody());
        ApiMessageContext responseContext = parseResponse(httpRequest);
        if (showLog) {
            log.info("响应-> body:{}, header:{}", responseContext.getBody(), responseContext.getHeaders());
        }

        // 验签
        if (request.getProtocol() == ApiProtocol.JSON) {
            if (!sign(responseContext.getBody()).equals(responseContext.getSign())) {
                throw new ApiServiceException(ApiServiceResultCode.UNAUTHENTICATED_ERROR, request.getProtocol().code());
            }
        } else {
            log.info("响应-> {}", responseContext.getBody());
            Map<String, Object> jsonObject = JsonMarshallor.INSTANCE.parse(responseContext.getBody(), Map.class);
            Map<String, String> map = ApiUtils.transformMap(jsonObject);
            if (!sign(ApiUtils.getWaitForSignString(map)).equals(map.get(ApiConstants.SIGN))) {
                throw new ApiServiceException(ApiServiceResultCode.UNAUTHENTICATED_ERROR, request.getProtocol().code());
            }
        }

        ApiMessage response = JsonMarshallor.INSTANCE.parse(responseContext.getBody(), clazz);
        // 解密
        doSecurity(response, CryptoType.decrypt);
        return (T) response;
    }


    /**
     * 跳转接口请求
     * <p>
     * 用于：直接解析跳转接口请求报文，通过HttpServletResponse直接发送redirect
     *
     * @param request
     * @param response
     */
    public void redirectSend(ApiRequest request, HttpServletResponse response) {
        ApiMessageContext context = redirectParse(request);
        String location = context.buildRedirectUrl();
        try {
            response.sendRedirect(location);
        } catch (Exception e) {
            throw new RuntimeException("跳转发送失败");
        }
    }

    /**
     * 跳转接口解析
     * <p>
     * 用于：解析需要跳转的报文，然后返回数据给客户端（前后端分离），由客户端发送调整情况
     *
     * @param request
     * @return
     */
    public ApiMessageContext redirectParse(ApiRequest request) {
        doDefaultVal(request);
        doSecurity(request, CryptoType.encrypt);
        ApiMessageContext context = new ApiMessageContext();
        if (request.getProtocol() == ApiProtocol.JSON) {
            String body = JsonMarshallor.INSTANCE.marshall(request);
            context.parameter(ApiConstants.BODY, ApiUtils.urlEncode(body));
            context.parameter(ApiConstants.PROTOCOL, ApiProtocol.JSON.code());
            context.parameter(ApiConstants.ACCESS_KEY, accessKey);
            context.parameter(ApiConstants.SIGN_TYPE, signType);
            context.parameter(ApiConstants.SIGN, sign(body));
            context.setBody(body);
        } else {
        }
        context.setUrl(this.getGatewayUrl());
        log.info("跳转请求-> body:{}", context.getBody());
        return context;
    }


    /**
     * 通知接收
     * <p>
     * <li>[同步通知] 跳转接口跳回：gateway - redirect -> returnUrl</li>
     * <li>[异步通知] 异步接口通知：gateway -> notifyUrl</li>
     *
     * @param request 通知请求
     * @return 解析结果
     */
    public ApiMessageContext noticeParse(HttpServletRequest request) {
        ApiMessageContext messageContext = ApiUtils.getApiRequestContext(request);
        log.info("通知接收-> body:{}", messageContext.getBody());
        boolean verifyResult;
        String protocol = messageContext.getProtocol();
        if (ApiProtocol.JSON.code().equalsIgnoreCase(protocol)) {
            // JSON协议.V5
            verifyResult = sign(messageContext.getBody()).equals(messageContext.getSign());
        } else {
            // 待补充兼容4.0及以下的FORM_JSON协议
            Map<String, String> params = messageContext.getParameters();
            String waitForSign = ApiUtils.getWaitForSignString(params);
            verifyResult = sign(waitForSign).equals(messageContext.getSign());
        }
        if (!verifyResult) {
            throw new ApiServiceException(ApiServiceResultCode.UNAUTHENTICATED_ERROR);
        }
        return messageContext;
    }

    /**
     * 通知接收并解析
     *
     * @param request 通知请求
     * @param clazz   通知报文类型
     * @param <T>     通知报文
     * @return
     */
    public <T> T notice(HttpServletRequest request, Class<T> clazz) {
        ApiMessageContext context = noticeParse(request);
        ApiMessage response = JsonMarshallor.INSTANCE.parse(context.getBody(), clazz);
        doSecurity(response, CryptoType.decrypt);
        return (T) response;
    }

    /**
     * 签名
     *
     * @param body
     * @return
     */
    public String sign(String body) {
        return DigestUtils.md5Hex(body + secretKey);
    }

    /**
     * 加密
     *
     * @param text
     * @return
     */
    public String encrypt(String text) {
        byte[] securityKey = secretKey.substring(0, 16).getBytes();
        byte[] encrypt = ApiUtils.aes(text.getBytes(), securityKey, Cipher.ENCRYPT_MODE);
        return Base64.encodeBase64String(encrypt);
    }

    /**
     * 解密
     *
     * @param encrytText
     * @return
     */
    public String decrypt(String encrytText) {
        byte[] securityKey = secretKey.substring(0, 16).getBytes();
        byte[] decryptResult = ApiUtils.aes(encrytText.getBytes(), securityKey, Cipher.DECRYPT_MODE);
        return new String(decryptResult);
    }


    protected String getContextType(ApiProtocol apiProtocol) {
        return apiProtocol == ApiProtocol.JSON ? HttpRequest.CONTENT_TYPE_JSON : HttpRequest.CONTENT_TYPE_FORM;
    }


    protected ApiMessageContext parseRequest(ApiRequest request) {
        ApiMessageContext context = new ApiMessageContext();
        doDefaultVal(request);
        doSecurity(request, CryptoType.encrypt);
        if (request.getProtocol() == ApiProtocol.JSON) {
            String body = JsonMarshallor.INSTANCE.marshall(request);
            context.setBody(body);
            context.header(ApiConstants.X_API_ACCESS_KEY, accessKey);
            context.header(ApiConstants.X_API_SIGN_TYPE, signType);
            context.header(ApiConstants.X_API_SIGN, sign(body));
            context.header(ApiConstants.X_API_PROTOCOL, ApiProtocol.JSON.code());
        } else {
            throw new UnsupportedOperationException("暂时不兼容V4");
        }
        context.setUrl(this.gatewayUrl);
        return context;
    }


    protected ApiMessageContext parseResponse(HttpRequest httpRequest) {
        ApiMessageContext context = new ApiMessageContext();
        if (httpRequest.code() > HTTP_STATUS_REDIRECT_START && httpRequest.code() < HTTP_STATUS_REDIRECT_END) {
            String location = httpRequest.header(HttpRequest.HEADER_LOCATION);
            if (showLog) {
                log.info("跳转-> Location: {}", location);
            }
            context.setUrl(location);
            Map<String, String> queryStringMap = new HashMap<>();
            String queryString = ApiUtils.substringAfter(location, "?");
            for (String pair : queryString.split("&")) {
                if (ApiUtils.isBlank(pair)) {
                    continue;
                }
                String[] kv = pair.split("=");
                if (kv == null || kv.length != 2) {
                    continue;
                }
                queryStringMap.put(kv[0], kv[1]);
            }
            context.setParameters(queryStringMap);
            context.setBody(ApiUtils.urlDecode(queryStringMap.get(ApiConstants.BODY)));
        } else {
            context.setHeaders(getApiHeaders(httpRequest));
            context.setBody(httpRequest.body());
        }
        return context;
    }


    protected Map<String, String> getApiHeaders(HttpRequest httpRequest) {
        Map<String, String> apiHeaders = new HashMap();
        apiHeaders.put(ApiConstants.X_API_ACCESS_KEY, httpRequest.header(ApiConstants.X_API_ACCESS_KEY));
        apiHeaders.put(ApiConstants.X_API_SIGN_TYPE, httpRequest.header(ApiConstants.X_API_SIGN_TYPE));
        apiHeaders.put(ApiConstants.X_API_SIGN, httpRequest.header(ApiConstants.X_API_SIGN));
        apiHeaders.put(ApiConstants.X_API_PROTOCOL, httpRequest.header(ApiConstants.X_API_PROTOCOL));
        return apiHeaders;
    }


    protected void doDefaultVal(ApiRequest request) {
        if (ApiUtils.isBlank(request.getRequestNo())) {
            request.setRequestNo(UUID.randomUUID().toString());
        }
        if (ApiUtils.isBlank(request.getVersion())) {
            request.setVersion(DEFAULT_VERSION);
        }
        if (ApiUtils.isBlank(request.getPartnerId())) {
            request.setPartnerId(this.accessKey);
        }
        guessServiceName(request);
        ApiUtils.notBlank(request.getService(), ApiConstants.SERVICE);
        request.check();
    }

    protected void doSecurity(ApiMessage message, CryptoType cryptoType) {
        List<Field> fields = SECURITY_FIELDS_CACHE.get(message.getClass());
        if (fields == null) {
            List<Field> fieldList = new ArrayList();
            for (Field field : message.getClass().getDeclaredFields()) {
                OpenApiField annotation = field.getAnnotation(OpenApiField.class);
                if (annotation != null && annotation.security() && field.getType().isAssignableFrom(String.class)) {
                    field.setAccessible(true);
                    fieldList.add(field);
                }
            }
            fields = fieldList;
            SECURITY_FIELDS_CACHE.put(message.getClass(), fields);
        }

        if (fields.size() == 0) {
            return;
        }

        for (Field field : fields) {
            try {
                Object o = field.get(message);
                if (o == null) {
                    continue;
                }
                String value = (cryptoType == CryptoType.encrypt ? encrypt(o.toString()) : decrypt(o.toString()));
                field.set(message, value);
            } catch (Exception e) {
                throw new ApiClientException(ApiServiceResultCode.CRYPTO_ERROR, null, e);
            }
        }
    }

    /**
     * 猜一猜服务名
     *
     * @param request
     */
    protected void guessServiceName(ApiRequest request) {
        if (ApiUtils.isNoneBlank(request.getService())) {
            return;
        }
        String className = request.getClass().getSimpleName();
        String serviceName = null;
        for (String postfix : ApiConstants.API_REQUEST_POSTFIX) {
            if (ApiUtils.contains(className, postfix)) {
                serviceName = ApiUtils.substringBeforeLast(className, postfix);
                break;
            }
        }
        request.setService(ApiUtils.uncapitalize(serviceName));
    }

    public String getSignType() {
        return signType;
    }

    public void setSignType(String signType) {
        this.signType = signType;
    }

    public String getGatewayUrl() {
        return gatewayUrl;
    }

    public void setGatewayUrl(String gatewayUrl) {
        this.gatewayUrl = gatewayUrl;
    }

    public String getAccessKey() {
        return accessKey;
    }

    public void setAccessKey(String accessKey) {
        this.accessKey = accessKey;
    }

    public String getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    public boolean isShowLog() {
        return showLog;
    }

    public void setShowLog(boolean showLog) {
        this.showLog = showLog;
    }

    public int getReadTimeout() {
        return readTimeout;
    }

    public void setReadTimeout(int readTimeout) {
        this.readTimeout = readTimeout;
    }

    public int getConnTimeout() {
        return connTimeout;
    }

    public void setConnTimeout(int connTimeout) {
        this.connTimeout = connTimeout;
    }

    public String getCharset() {
        return charset;
    }

    public void setCharset(String charset) {
        this.charset = charset;
    }

    public enum CryptoType {
        encrypt, decrypt
    }


}
