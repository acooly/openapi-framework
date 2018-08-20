package com.acooly.openapi.framework.client;

import com.acooly.core.common.exception.AppConfigException;
import com.acooly.core.utils.Encodes;
import com.acooly.core.utils.security.Cryptos;
import com.acooly.openapi.framework.common.ApiConstants;
import com.acooly.openapi.framework.common.annotation.OpenApiField;
import com.acooly.openapi.framework.common.enums.ApiServiceResultCode;
import com.acooly.openapi.framework.common.enums.SignTypeEnum;
import com.acooly.openapi.framework.common.exception.ApiServiceException;
import com.acooly.openapi.framework.common.message.ApiRequest;
import com.acooly.openapi.framework.common.utils.json.JsonMarshallor;
import com.github.kevinsawicki.http.HttpRequest;
import com.google.common.base.Charsets;
import com.google.common.base.Splitter;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.util.Assert;

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
        Assert.hasText(request.getPartnerId(), "partnerId不能为空");
        if (Strings.isNullOrEmpty(request.getVersion())) {
            request.setVersion(DEFAULT_VERSION);
        }
        Assert.hasText(request.getService(), "service不能为空");
        request.check();

        List<Field> fields = securityFieldsMap.get(clazz);
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
            securityFieldsMap.put(clazz, fields);
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
        Map<String, String> requestHeader = Maps.newTreeMap();
        requestHeader.put(ApiConstants.ACCESS_KEY, accessKey);
        requestHeader.put(ApiConstants.SIGN_TYPE, signType);
        requestHeader.put(ApiConstants.SIGN, sign(body));
        if (showLog) {
            log.info("请求-> header:{} body:{}", requestHeader, body);
        }
        HttpRequest httpRequest =
                HttpRequest.post(gatewayUrl).headers(requestHeader).contentType("application/json").followRedirects(false).send(body);
        Map<String, List<String>> responseHeader = httpRequest.headers();
        String sign = null;
        String signType;
        String responseBody;

        if (httpRequest.code() == 302) {
            Map<String, List<String>> logHeader = Maps.newLinkedHashMap();
            logHeader.put("Location", responseHeader.get("Location"));
            String location = responseHeader.get("Location").get(0);
            if (showLog) {
                log.info("响应-> header:{}", logHeader);
            }
            Map<String, String> queryStringMap =
                    Splitter.on("&")
                            .withKeyValueSeparator("=")
                            .split(location.substring(location.indexOf('?') + 1));
            sign = queryStringMap.get(ApiConstants.SIGN);
            signType = queryStringMap.get(ApiConstants.SIGN);
            try {
                responseBody =
                        URLDecoder.decode(queryStringMap.get(ApiConstants.BODY), Charsets.UTF_8.name());
            } catch (UnsupportedEncodingException e) {
                throw new ApiServiceException(ApiServiceResultCode.INTERNAL_ERROR, e);
            }
        } else {
            responseBody = httpRequest.body();
            if (showLog) {
                Map<String, List<String>> logHeader = Maps.newLinkedHashMap();
                logHeader.put(ApiConstants.SIGN_TYPE, responseHeader.get(ApiConstants.SIGN_TYPE));
                logHeader.put(ApiConstants.SIGN, responseHeader.get(ApiConstants.SIGN));
                log.info("响应-> header:{}, body:{}", logHeader, responseBody);
            }

            List<String> signList = responseHeader.get(ApiConstants.SIGN);
            if (signList != null) {
                sign = signList.get(0);
                signType = responseHeader.get(ApiConstants.SIGN_TYPE).get(0);
                Assert.notNull(signType);
            }
        }
        if (!sign(responseBody).equals(sign)) {
            throw new RuntimeException("验证失败");
        }
        return JsonMarshallor.INSTANCE.parse(responseBody, clazz);
    }

    public String sign(String body) {
        return DigestUtils.md5Hex(body + secretKey);
    }

    public String encrypt(String text) {
        byte[] securityKey = secretKey.substring(0, 16).getBytes();
        byte[] encrypt = Cryptos.aesEncrypt(text.getBytes(), securityKey);
        return Encodes.encodeBase64(encrypt);
    }
}
