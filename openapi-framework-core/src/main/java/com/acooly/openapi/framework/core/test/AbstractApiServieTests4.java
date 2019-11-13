package com.acooly.openapi.framework.core.test;

import com.acooly.core.utils.Encodes;
import com.acooly.core.utils.Profiles;
import com.acooly.core.utils.net.HttpResult;
import com.acooly.core.utils.security.Cryptos;
import com.acooly.openapi.framework.common.ApiConstants;
import com.acooly.openapi.framework.common.enums.ApiProtocol;
import com.acooly.openapi.framework.common.enums.SignTypeEnum;
import com.acooly.openapi.framework.common.message.ApiAsyncRequest;
import com.acooly.openapi.framework.common.message.ApiMessage;
import com.acooly.openapi.framework.common.message.ApiRequest;
import com.acooly.openapi.framework.common.utils.ApiUtils;
import com.acooly.openapi.framework.common.utils.json.JsonMarshallor;
import com.acooly.openapi.framework.common.utils.json.ObjectAccessor;
import com.acooly.openapi.framework.core.security.sign.Md5Signer;
import com.github.kevinsawicki.http.HttpRequest;
import com.google.common.base.Strings;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;

import java.util.Date;
import java.util.Map;

import static com.acooly.openapi.framework.common.ApiConstants.TEST_ACCESS_KEY;
import static com.acooly.openapi.framework.common.ApiConstants.TEST_SECRET_KEY;

/**
 * ApiService Test base
 *
 * @author zhangpu
 * @author qzhanbo@yiji.com
 */
@Slf4j
public abstract class AbstractApiServieTests4 {


    static {
        Profiles.setProfile(Profiles.Profile.sdev);
    }


    protected final static String ENCODING = "utf-8";

    protected String signType = SignTypeEnum.MD5.toString();
    protected ApiProtocol protocal = ApiProtocol.HTTP_FORM_JSON;
    protected String gatewayUrl = "http://127.0.0.1:8089/gateway.do";
    protected String key = TEST_SECRET_KEY;
    protected String partnerId = TEST_ACCESS_KEY;
    protected String service = "";
    protected String version = "1.0";
    protected String notifyUrl = "";
    protected String returnUrl = "";

    Md5Signer md5Signer = new Md5Signer();

    protected <T> T request(ApiRequest request, Class<T> clazz) {
        return request(request, clazz, null);
    }

    protected <T> T request(ApiRequest request, Class<T> clazz, ApiTestHandler testHandler) {
        Map<String, String> map = marshall(request);
        if (testHandler != null) {
            map = testHandler.afterMarshall(map);
        }
        HttpResult result = post(map);
        return JsonMarshallor.INSTANCE.parse(result.getBody(), clazz);
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    protected HttpResult post(Map bizParams) {
        Map data = Maps.newHashMap();
        if (service != null) {
            data.put(ApiConstants.SERVICE, this.service);
        }
        if (version != null) {
            data.put(ApiConstants.VERSION, this.version);
        }
        if (partnerId != null) {
            data.put(ApiConstants.PARTNER_ID, this.partnerId);
        }
        if (protocal != null) {
            data.put(ApiConstants.PROTOCOL, this.protocal);
        }
        if (signType != null) {
            data.put(ApiConstants.SIGN_TYPE, this.signType);
        }
        if (notifyUrl != null) {
            data.put(ApiConstants.NOTIFY_URL, this.notifyUrl);
        }
        if (returnUrl != null) {
            data.put(ApiConstants.RETURN_URL, this.returnUrl);
        }
        if (bizParams != null) {
            data.putAll(bizParams);
        }
        return send(data);
    }

    protected HttpResult send(ApiMessage message) {

        if (Strings.isNullOrEmpty(message.getRequestNo())) {
            message.setRequestNo(DigestUtils.md5Hex(new Date().toString()));
        }

        if (message.getProtocol() == null) {
            message.setProtocol(this.protocal);
        }

        if (Strings.isNullOrEmpty(message.getService())) {
            message.setService(this.service);
        }
        if (Strings.isNullOrEmpty(message.getVersion())) {
            message.setVersion(this.version);
        }
        if (Strings.isNullOrEmpty(message.getPartnerId())) {
            message.setPartnerId(this.partnerId);
        }

        if (message instanceof ApiAsyncRequest) {
            if (Strings.isNullOrEmpty(((ApiAsyncRequest) message).getNotifyUrl())) {
                ((ApiAsyncRequest) message).setNotifyUrl(this.notifyUrl);
            }
            if (Strings.isNullOrEmpty(((ApiAsyncRequest) message).getReturnUrl())) {
                ((ApiAsyncRequest) message).setReturnUrl(this.returnUrl);
            }

        }
        Map<String, String> requestData = marshall(message);
        return send(requestData);
    }

    protected <T> T send(ApiMessage message, Class<T> clazz) {
        HttpResult result = send(message);
        return JsonMarshallor.INSTANCE.parse(result.getBody(), clazz);
    }

    protected HttpResult send(Map<String, String> requestData) {
        if (Strings.isNullOrEmpty(requestData.get(ApiConstants.SIGN_TYPE))) {
            requestData.put(ApiConstants.SIGN_TYPE, SignTypeEnum.MD5.code());
        }
        if (Strings.isNullOrEmpty(requestData.get(ApiConstants.SIGN))) {
            requestData.put("sign", md5Signer.sign(ApiUtils.getWaitForSignString(requestData), key));
        }

        log.info("请求报文: {}", requestData);
        HttpRequest httpRequest = HttpRequest.post(gatewayUrl).trustAllCerts()
                .trustAllHosts()
                .followRedirects(false)
                .contentType(HttpRequest.CONTENT_TYPE_FORM)
                .form(requestData);

        HttpResult result = new HttpResult();
        result.setStatus(httpRequest.code());
        result.setBody(httpRequest.body());
        log.info("响应报文: {}", result.getBody());
        return result;
    }

    protected static Map<String, String> marshall(ApiMessage message) {
        return ObjectAccessor.of(message).getAllDataExcludeTransient();
    }


    protected String encrypt(String text) {
        byte[] securityKey = key.substring(0, 16).getBytes();
        byte[] encrypt = Cryptos.aesEncrypt(text.getBytes(), securityKey);
        return Encodes.encodeBase64(encrypt);
    }

    public interface ApiTestHandler {
        /**
         * marshall后,发送前回调
         *
         * @param requestData
         * @return
         */
        Map<String, String> afterMarshall(Map<String, String> requestData);

    }


}
