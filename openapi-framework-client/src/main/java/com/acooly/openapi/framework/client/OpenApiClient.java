package com.acooly.openapi.framework.client;

import com.acooly.core.common.exception.AppConfigException;
import com.acooly.core.utils.Assert;
import com.acooly.core.utils.Encodes;
import com.acooly.core.utils.Strings;
import com.acooly.core.utils.security.Cryptos;
import com.acooly.openapi.framework.common.ApiConstants;
import com.acooly.openapi.framework.common.OpenApis;
import com.acooly.openapi.framework.common.annotation.OpenApiField;
import com.acooly.openapi.framework.common.dto.ApiMessageContext;
import com.acooly.openapi.framework.common.enums.ApiProtocol;
import com.acooly.openapi.framework.common.enums.ApiServiceResultCode;
import com.acooly.openapi.framework.common.enums.SignTypeEnum;
import com.acooly.openapi.framework.common.exception.ApiServiceException;
import com.acooly.openapi.framework.common.message.ApiRequest;
import com.acooly.openapi.framework.common.utils.json.JsonMarshallor;
import com.github.kevinsawicki.http.HttpRequest;
import com.google.common.base.Charsets;
import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.net.URLDecoder;
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
    private String signType = SignTypeEnum.MD5.toString();
    private String gatewayUrl;
    private String accessKey;
    private String secretKey;

    private boolean showLog = true;

    public OpenApiClient(String gatewayUrl, String accessKey, String secretKey) {
        this(gatewayUrl, accessKey, secretKey, true);
    }

    public OpenApiClient(String gatewayUrl, String accessKey, String secretKey, boolean showLog) {
        Assert.hasText(gatewayUrl, "网关地址不能为空");
        Assert.hasText(accessKey, "accessKey不能为空");
        Assert.hasText(secretKey, "secretKey不能为空");
        Assert.isTrue(gatewayUrl.endsWith(".do") || gatewayUrl.endsWith(".mock"), "请求地址必须以.do或者.mock结尾");
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

        MessageResult messageResult = parse(request);

        if (showLog) {
            log.info("请求-> body:{},header:{} ", messageResult.getBody(), messageResult.getHeaders());
        }
        HttpRequest httpRequest =
                HttpRequest.post(gatewayUrl).headers(messageResult.getHeaders()).contentType("application/json").followRedirects(false).send(messageResult.getBody());
        Map<String, String> apiHeaders = getApiHeaders(httpRequest);
        String sign = null;
        String signType;
        String responseBody;

        if (httpRequest.code() == 302) {
            apiHeaders.put("Location", httpRequest.header("Location"));
            String location = apiHeaders.get("Location");
            if (showLog) {
                log.info("响应-> header:{}", apiHeaders);
            }
            Map<String, String> queryStringMap =
                    Splitter.on("&").withKeyValueSeparator("=").split(location.substring(location.indexOf('?') + 1));
            sign = queryStringMap.get(ApiConstants.SIGN);
            try {
                responseBody = URLDecoder.decode(queryStringMap.get(ApiConstants.BODY), Charsets.UTF_8.name());
            } catch (UnsupportedEncodingException e) {
                throw new ApiServiceException(ApiServiceResultCode.INTERNAL_ERROR, e);
            }
        } else {
            responseBody = httpRequest.body();
            if (showLog) {
                log.info("响应-> body:{}, header:{},", responseBody, apiHeaders);
            }
            sign = apiHeaders.get(ApiConstants.X_API_SIGN);
        }
        if (!sign(responseBody).equals(sign)) {
            throw new RuntimeException("验证失败");
        }
        return JsonMarshallor.INSTANCE.parse(responseBody, clazz);
    }


    /**
     * 解析报文
     *
     * @param request
     * @return
     */
    public MessageResult parse(ApiRequest request) {
        if (Strings.isBlank(request.getVersion())) {
            request.setVersion(DEFAULT_VERSION);
        }
        if (Strings.isBlank(request.getPartnerId())) {
            request.setPartnerId(this.accessKey);
        }
        Assert.hasText(request.getService(), "service不能为空");
        request.check();


        List<Field> fields = securityFieldsMap.get(request.getClass());
        if (fields == null) {
            List<Field> fieldList = Lists.newArrayList();
            for (Field field : request.getClass().getDeclaredFields()) {
                OpenApiField annotation = field.getAnnotation(OpenApiField.class);
                if (annotation != null && annotation.security()) {
                    field.setAccessible(true);
                    fieldList.add(field);
                }
            }
            fields = fieldList;
            securityFieldsMap.put(request.getClass(), fields);
        }

        if (fields.size() != 0) {
            fields.forEach(field -> {
                try {
                    Object o = field.get(request);
                    if (o != null) {
                        String encrypt = encrypt(o.toString());
                        field.set(request, encrypt);
                    }
                } catch (Exception e) {
                    throw new AppConfigException(e);
                }
            });
        }

        String body = JsonMarshallor.INSTANCE.marshall(request);
        Map<String, String> requestHeader = Maps.newHashMap();
        requestHeader.put(ApiConstants.X_API_ACCESS_KEY, accessKey);
        requestHeader.put(ApiConstants.X_API_SIGN_TYPE, signType);
        requestHeader.put(ApiConstants.X_API_SIGN, sign(body));

        MessageResult result = new MessageResult();
        result.setUrl(this.getGatewayUrl());
        result.setBody(body);
        result.setHeaders(requestHeader);
        return result;
    }

    /**
     * 验证签名
     *
     * @param request
     * @return
     */
    public ApiMessageContext verify(HttpServletRequest request) {
        ApiMessageContext messageContext = OpenApis.getApiRequestContext(request);
        String protocol = messageContext.getProtocol();
        boolean verfiyResult = true;
        if (Strings.equalsIgnoreCase(protocol, ApiProtocol.JSON.code())) {
            // JSON协议
            verfiyResult = Strings.equals(messageContext.getSign(), sign(messageContext.getBody()));
        } else {
            // 待补充兼容4.0及以下的FORM_JSON协议
        }
        if (!verfiyResult) {
            throw new ApiServiceException(ApiServiceResultCode.UNAUTHENTICATED_ERROR);
        }
        return messageContext;
    }


    public String sign(String body) {
        return DigestUtils.md5Hex(body + secretKey);
    }

    public String encrypt(String text) {
        byte[] securityKey = secretKey.substring(0, 16).getBytes();
        byte[] encrypt = Cryptos.aesEncrypt(text.getBytes(), securityKey);
        return Encodes.encodeBase64(encrypt);
    }

    private Map<String, String> getApiHeaders(HttpRequest httpRequest) {
        Map<String, String> apiHeaders = Maps.newHashMap();
        apiHeaders.put(ApiConstants.X_API_ACCESS_KEY, httpRequest.header(ApiConstants.X_API_ACCESS_KEY));
        apiHeaders.put(ApiConstants.X_API_SIGN_TYPE, httpRequest.header(ApiConstants.X_API_SIGN_TYPE));
        apiHeaders.put(ApiConstants.X_API_SIGN, httpRequest.header(ApiConstants.X_API_SIGN));
        apiHeaders.put(ApiConstants.X_API_PROTOCOL, httpRequest.header(ApiConstants.X_API_PROTOCOL));
        return apiHeaders;
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
}
