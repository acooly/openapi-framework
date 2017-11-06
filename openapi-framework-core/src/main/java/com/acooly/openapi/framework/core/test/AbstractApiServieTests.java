package com.acooly.openapi.framework.core.test;

import com.acooly.core.utils.net.HttpResult;
import com.acooly.core.utils.net.Https;
import com.acooly.openapi.framework.common.ApiConstants;
import com.acooly.openapi.framework.common.enums.ApiProtocol;
import com.acooly.openapi.framework.common.message.ApiMessage;
import com.acooly.openapi.framework.common.message.ApiRequest;
import com.acooly.openapi.framework.common.utils.Cryptos;
import com.acooly.openapi.framework.common.utils.Encodes;
import com.acooly.openapi.framework.common.utils.json.JsonMarshallor;
import com.acooly.openapi.framework.core.OpenApiConstants;
import com.acooly.openapi.framework.core.marshall.ObjectAccessor;
import com.acooly.openapi.framework.core.security.sign.Md5Signer;
import com.acooly.openapi.framework.core.security.sign.SignTypeEnum;
import com.acooly.openapi.framework.core.security.sign.Signer;
import com.google.common.base.Strings;
import com.google.common.collect.Maps;
import org.apache.commons.codec.digest.DigestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.Map;

/**
 * ApiService Test base
 *
 * @author zhangpu
 * @author qiubo@qq.com
 */
public abstract class AbstractApiServieTests {

  protected static final String ENCODING = "utf-8";
  protected Logger logger = LoggerFactory.getLogger(this.getClass());
  protected String signType = SignTypeEnum.MD5.toString();
  protected String protocal = ApiProtocol.HTTP_FORM_JSON.code();
  protected String gatewayUrl = "http://127.0.0.1:8080/gateway";
  protected String key = OpenApiConstants.DEF_SECRETKEY;
  protected String partnerId = "zhangpu";
  protected String service = "";
  protected String version = "1.0";
  protected String notifyUrl = "";
  protected String returnUrl = "";

  protected Signer<Map<String, String>> signer = new Md5Signer();

  protected static Map<String, String> marshall(ApiMessage message) {
    return ObjectAccessor.of(message).getAllDataExcludeTransient();
  }

  public static void main(String[] args) {
    ApiRequest a = new ApiRequest();
    Map<String, String> m = ObjectAccessor.of(a).getAllDataExcludeTransient();
    System.out.println(m);
  }

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
    //        if (bizParams != null && bizParams.get(ApiConstants.REQUEST_NO) == null) {
    //            data.put(ApiConstants.REQUEST_NO,
    // DigestUtils.md5Hex(UUID.randomUUID().toString()));
    //        }
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

    if (Strings.isNullOrEmpty(message.getSignType())) {
      message.setSignType(this.signType);
    }

    if (Strings.isNullOrEmpty(message.getProtocol())) {
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

    if (message instanceof ApiRequest) {
      if (Strings.isNullOrEmpty(((ApiRequest) message).getNotifyUrl())) {
        ((ApiRequest) message).setNotifyUrl(this.notifyUrl);
      }
      if (Strings.isNullOrEmpty(((ApiRequest) message).getReturnUrl())) {
        ((ApiRequest) message).setReturnUrl(this.returnUrl);
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
    if (Strings.isNullOrEmpty(requestData.get(ApiConstants.SIGN))) {
      requestData.put("sign", signer.sign(requestData, key));
    }
    logger.info("请求报文: {}", requestData);
    HttpResult result = Https.getInstance().post(gatewayUrl, requestData, ENCODING);
    logger.info("响应报文: {}", result.getBody());
    return result;
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
