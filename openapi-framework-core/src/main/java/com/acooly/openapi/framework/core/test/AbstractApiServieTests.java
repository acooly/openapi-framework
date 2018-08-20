package com.acooly.openapi.framework.core.test;

import com.acooly.openapi.framework.client.OpenApiClient;
import com.acooly.openapi.framework.common.enums.SignTypeEnum;
import com.acooly.openapi.framework.common.message.ApiMessage;
import com.acooly.openapi.framework.common.message.ApiRequest;
import com.acooly.openapi.framework.core.marshall.ObjectAccessor;
import com.google.common.base.Strings;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

import java.util.Map;

import static com.acooly.openapi.framework.common.ApiConstants.TEST_ACCESS_KEY;
import static com.acooly.openapi.framework.common.ApiConstants.TEST_SECRET_KEY;

/**
 * ApiService Test base
 *
 * @author zhangpu
 * @author qiubo@qq.com
 */
@Slf4j
public abstract class AbstractApiServieTests {

    protected Logger logger = LoggerFactory.getLogger(this.getClass());
    protected String signType = SignTypeEnum.MD5.toString();
    protected String gatewayUrl = "http://127.0.0.1:8089/gateway.do";
    protected String accessKey = TEST_ACCESS_KEY;
    protected String secretKey = TEST_SECRET_KEY;
    protected String partnerId = "test";
    protected String service = "";
    protected String version = "1.0";
    protected String notifyUrl = "";
    protected String returnUrl = "";

    protected boolean showLog = true;

    protected static Map<String, String> marshall(ApiMessage message) {
        return ObjectAccessor.of(message).getAllDataExcludeTransient();
    }

    protected <T> T request(ApiRequest request, Class<T> clazz) {
        if (Strings.isNullOrEmpty(request.getPartnerId())) {
            request.setPartnerId(partnerId);
        }
        if (Strings.isNullOrEmpty(request.getVersion())) {
            request.setVersion(version);
        }
        Assert.hasText(request.getVersion());
        if (Strings.isNullOrEmpty(request.getService())) {
            request.setService(service);
        }
        Assert.hasText(request.getService());
        OpenApiClient openApiClient = new OpenApiClient(gatewayUrl, accessKey, secretKey);
        return openApiClient.send(request, clazz);
    }

    public String sign(String body) {
        return DigestUtils.md5Hex(body + secretKey);
    }
}
