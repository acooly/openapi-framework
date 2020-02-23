package com.acooly.openapi.framework.core.test;

import com.acooly.core.utils.Asserts;
import com.acooly.core.utils.Ids;
import com.acooly.core.utils.Profiles;
import com.acooly.openapi.framework.common.OpenApiTools;
import com.acooly.openapi.framework.common.enums.SignTypeEnum;
import com.acooly.openapi.framework.common.message.ApiMessage;
import com.acooly.openapi.framework.common.message.ApiRequest;
import com.acooly.openapi.framework.common.utils.json.ObjectAccessor;
import com.google.common.base.Strings;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
    static {
        Profiles.setProfile(Profiles.Profile.sdev);
    }

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
        Asserts.notEmpty(request.getVersion());
        if (Strings.isNullOrEmpty(request.getService())) {
            request.setService(service);
        }
        if (Strings.isNullOrEmpty(request.getService())) {
            guessServiceName(request);
        }
        Asserts.notEmpty(request.getService());
        if (Strings.isNullOrEmpty(request.getRequestNo())) {
            request.setRequestNo(Ids.RandomNumberGenerator.getNewString(20));
        }
        OpenApiTools openApiClient = new OpenApiTools(gatewayUrl, accessKey, secretKey);
        return openApiClient.send(request, clazz);
    }

    public String sign(String body) {
        return DigestUtils.md5Hex(body + secretKey);
    }


    /**
     * 猜一猜服务名
     *
     * @param request
     */
    protected void guessServiceName(ApiRequest request) {
        if (!Strings.isNullOrEmpty(request.getService())) {
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
}
