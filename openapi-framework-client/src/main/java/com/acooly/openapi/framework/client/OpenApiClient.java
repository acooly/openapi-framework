package com.acooly.openapi.framework.client;

import com.acooly.core.common.exception.AppConfigException;
import com.acooly.core.utils.Assert;
import com.acooly.core.utils.Asserts;
import com.acooly.core.utils.Encodes;
import com.acooly.core.utils.Strings;
import com.acooly.core.utils.security.Cryptos;
import com.acooly.openapi.framework.common.ApiConstants;
import com.acooly.openapi.framework.common.annotation.OpenApiField;
import com.acooly.openapi.framework.common.dto.ApiMessageContext;
import com.acooly.openapi.framework.common.enums.ApiProtocol;
import com.acooly.openapi.framework.common.enums.ApiServiceResultCode;
import com.acooly.openapi.framework.common.enums.SignType;
import com.acooly.openapi.framework.common.exception.ApiServiceException;
import com.acooly.openapi.framework.common.message.ApiMessage;
import com.acooly.openapi.framework.common.message.ApiRequest;
import com.acooly.openapi.framework.common.utils.ApiUtils;
import com.acooly.openapi.framework.common.utils.json.JsonMarshallor;
import com.acooly.openapi.framework.common.utils.json.ObjectAccessor;
import com.alibaba.fastjson.JSON;
import com.github.kevinsawicki.http.HttpRequest;
import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.sun.org.apache.regexp.internal.RE;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

/**
 * @author qiuboboy@qq.com
 * @date 2018-08-16 20:06
 */
@Slf4j
public class OpenApiClient {
    private static final String DEFAULT_VERSION = "1.0";
    private static Map<Class, List<Field>> securityFieldsMap = Maps.newConcurrentMap();
    private String signType = SignType.MD5.code();
    private String gatewayUrl;
    private String accessKey;
    private String secretKey;

    private boolean showLog = true;

    public OpenApiClient(String gatewayUrl, String accessKey, String secretKey) {
        this(gatewayUrl, accessKey, secretKey, true);
    }

    public OpenApiClient(String gatewayUrl, String accessKey, String secretKey, boolean showLog) {
        Asserts.notEmpty(gatewayUrl, "网关地址");
        Asserts.notEmpty(accessKey, "accessKey");
        Asserts.notEmpty(secretKey, "secretKey");
        Asserts.isTrue(gatewayUrl.endsWith(".do") || gatewayUrl.endsWith(".mock"), "请求地址必须以.do或者.mock结尾");
        this.gatewayUrl = gatewayUrl;
        this.accessKey = accessKey;
        this.secretKey = secretKey;
        this.showLog = showLog;
    }

    /**
     * 发送请求到网关,并对标注加密的字段自动加密
     *
     * @param request 请求对象
     * @param clazz   响应类型
     * @param <T>     响应类
     * @return 响应对象
     */
    public <T> T send(ApiRequest request, Class<T> clazz) {
        ApiMessageContext context = parseRequest(request);
        HttpRequest httpRequest = HttpRequest.post(gatewayUrl);
        if (request.getProtocol() == ApiProtocol.JSON) {
            httpRequest.headers(context.getHeaders()).contentType(HttpRequest.CONTENT_TYPE_JSON).followRedirects(false).send(context.getBody());
        } else {
            httpRequest.trustAllCerts().trustAllHosts().followRedirects(false).contentType(HttpRequest.CONTENT_TYPE_FORM)
                    .form(context.getParameters());
        }
        ApiMessageContext responseContext = parseResponse(httpRequest);
        if (request.getProtocol() == ApiProtocol.JSON) {
            log.info("响应-> body:{}, header:{}", responseContext.getBody(), responseContext.getHeaders());
            if (!sign(responseContext.getBody()).equals(responseContext.getSign())) {
                throw new RuntimeException("验证失败");
            }
        } else {
            log.info("响应-> {}", responseContext.getBody());
            Map<String, Object> jsonObject = JsonMarshallor.INSTANCE.parse(responseContext.getBody(), Map.class);
            Map<String, String> map = Maps.transformValues(jsonObject, v -> (v != null ? v.toString() : null));
            String waitForSign = ApiUtils.getWaitForSignString(map);
            log.debug("响应[FORM_JSON] waitForSign: {}", waitForSign);
            if (!sign(ApiUtils.getWaitForSignString(map)).equals(map.get(ApiConstants.SIGN))) {
                throw new RuntimeException("验证失败");
            }
        }
        ApiMessage response = JsonMarshallor.INSTANCE.parse(responseContext.getBody(), clazz);
        doSecurity(response, CryptoType.decrypt);
        return (T) response;
    }


    /**
     * 跳转请求解析报文
     * 专用于客户端跳转请求的报文解析，获取redirectUrl
     *
     * @param request
     * @return
     */
    public ApiMessageContext parse(ApiRequest request) {
        doDefaultVal(request);
        doSecurity(request, CryptoType.encrypt);
        ApiMessageContext context = new ApiMessageContext();
        if (request.getProtocol() == ApiProtocol.JSON) {
            // 客户端的序列化调整为通用JSON序列化
            String body = JSON.toJSONString(request);
            context.parameter(ApiConstants.BODY, Encodes.urlEncode(body));
            context.parameter(ApiConstants.PROTOCOL, ApiProtocol.JSON.code());
            context.parameter(ApiConstants.ACCESS_KEY, accessKey);
            context.parameter(ApiConstants.SIGN_TYPE, signType);
            context.parameter(ApiConstants.SIGN, sign(body));
            context.setBody(body);
        } else {
        }
        context.setUrl(this.getGatewayUrl());

        return context;
    }


    /**
     * 跳转接口跳回的验证签名
     * gateway - redirect -> returnUrl
     *
     * @param request
     * @return
     */
    public ApiMessageContext verify(HttpServletRequest request) {
        ApiMessageContext messageContext = ApiUtils.getApiRequestContext(request);
        String protocol = messageContext.getProtocol();
        boolean verifyResult;
        if (Strings.equalsIgnoreCase(protocol, ApiProtocol.JSON.code())) {
            // JSON协议
            verifyResult = Strings.equals(messageContext.getSign(), sign(messageContext.getBody()));
        } else {
            // 待补充兼容4.0及以下的FORM_JSON协议
            Map<String, String> params = messageContext.getParameters();
            String waitForSign = ApiUtils.getWaitForSignString(params);
            verifyResult = Strings.equals(messageContext.getSign(), sign(waitForSign));
        }
        if (!verifyResult) {
            throw new ApiServiceException(ApiServiceResultCode.UNAUTHENTICATED_ERROR);
        }
        return messageContext;
    }


    public String sign(String body) {
        log.debug("Request Sign Body: {}", body);
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
        byte[] encrypt = Cryptos.aesEncrypt(text.getBytes(), securityKey);
        return Encodes.encodeBase64(encrypt);
    }

    /**
     * 解密
     *
     * @param encrytText
     * @return
     */
    public String decrypt(String encrytText) {
        byte[] securityKey = secretKey.substring(0, 16).getBytes();
        return Cryptos.aesDecrypt(encrytText.getBytes(), securityKey);
    }


    protected ApiMessageContext parseResponse(HttpRequest httpRequest) {
        ApiMessageContext context = new ApiMessageContext();
        if (httpRequest.code() == 302) {
            String location = httpRequest.header("Location");
            if (showLog) {
                log.info("跳转-> Location: {}", location);
            }
            context.setUrl(location);
            Map<String, String> queryStringMap = Splitter.on("&").withKeyValueSeparator("=").split(Strings.substringAfter(location, "?"));
            context.setParameters(queryStringMap);
            context.setBody(Encodes.urlDecode(queryStringMap.get(ApiConstants.BODY)));
        } else {
            context.setHeaders(getApiHeaders(httpRequest));
            context.setBody(httpRequest.body());
        }
        return context;
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
            if (showLog) {
                log.info("请求-> body:{},header:{} ", context.getBody(), context.getHeaders());
            }
        } else {
            request.setProtocol(null);
            Map<String, String> data = ObjectAccessor.of(request).getAllDataExcludeTransient();
            data.put(ApiConstants.SIGN_TYPE, signType);
            data.put("sign", sign(ApiUtils.getWaitForSignString(data)));
            context.setParameters(data);
            if (showLog) {
                log.info("请求-> {}", context.getParameters());
            }
        }
        context.setUrl(this.gatewayUrl);
        return context;
    }


    private Map<String, String> getApiHeaders(HttpRequest httpRequest) {
        Map<String, String> apiHeaders = Maps.newHashMap();
        apiHeaders.put(ApiConstants.X_API_ACCESS_KEY, httpRequest.header(ApiConstants.X_API_ACCESS_KEY));
        apiHeaders.put(ApiConstants.X_API_SIGN_TYPE, httpRequest.header(ApiConstants.X_API_SIGN_TYPE));
        apiHeaders.put(ApiConstants.X_API_SIGN, httpRequest.header(ApiConstants.X_API_SIGN));
        apiHeaders.put(ApiConstants.X_API_PROTOCOL, httpRequest.header(ApiConstants.X_API_PROTOCOL));
        return apiHeaders;
    }


    protected void doDefaultVal(ApiRequest request) {
        if (Strings.isBlank(request.getVersion())) {
            request.setVersion(DEFAULT_VERSION);
        }
        if (Strings.isBlank(request.getPartnerId())) {
            request.setPartnerId(this.accessKey);
        }
        guessServiceName(request);
        Asserts.notEmpty(request.getService(), "service不能为空");
        request.check();
    }

    protected void doSecurity(ApiMessage message, CryptoType cryptoType) {
        List<Field> fields = securityFieldsMap.get(message.getClass());
        if (fields == null) {
            List<Field> fieldList = Lists.newArrayList();
            for (Field field : message.getClass().getDeclaredFields()) {
                OpenApiField annotation = field.getAnnotation(OpenApiField.class);
                if (annotation != null && annotation.security()) {
                    field.setAccessible(true);
                    fieldList.add(field);
                }
            }
            fields = fieldList;
            securityFieldsMap.put(message.getClass(), fields);
        }

        if (fields.size() != 0) {
            fields.forEach(field -> {
                try {
                    Object o = field.get(message);
                    if (o != null) {
                        String value = null;
                        if (cryptoType == CryptoType.encrypt) {
                            value = encrypt(o.toString());
                        } else {
                            value = decrypt(o.toString());
                        }
                        field.set(message, value);
                    }
                } catch (Exception e) {
                    throw new AppConfigException(e);
                }
            });
        }
    }

    /**
     * 猜一猜服务名
     *
     * @param request
     */
    protected void guessServiceName(ApiRequest request) {
        if (Strings.isNoneBlank(request.getService())) {
            return;
        }

        String className = request.getClass().getSimpleName();
        String serviceName = null;
        if (StringUtils.contains(className, "ApiRequest")) {
            serviceName = StringUtils.substringBeforeLast(className, "ApiRequest");
        } else if (StringUtils.contains(className, "Request")) {
            serviceName = StringUtils.substringBeforeLast(className, "Request");
        }
        request.setService(StringUtils.uncapitalize(serviceName));
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

    public static enum CryptoType {
        encrypt, decrypt
    }
}
