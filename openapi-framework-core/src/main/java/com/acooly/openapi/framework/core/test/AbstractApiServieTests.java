package com.acooly.openapi.framework.core.test;

import com.acooly.openapi.framework.common.ApiConstants;
import com.acooly.openapi.framework.common.enums.ApiProtocol;
import com.acooly.openapi.framework.common.enums.ApiServiceResultCode;
import com.acooly.openapi.framework.common.enums.SignTypeEnum;
import com.acooly.openapi.framework.common.exception.ApiServiceException;
import com.acooly.openapi.framework.common.message.ApiMessage;
import com.acooly.openapi.framework.common.message.ApiRequest;
import com.acooly.openapi.framework.common.utils.Cryptos;
import com.acooly.openapi.framework.common.utils.Encodes;
import com.acooly.openapi.framework.common.utils.json.JsonMarshallor;
import com.acooly.openapi.framework.core.marshall.ObjectAccessor;
import com.acooly.openapi.framework.core.security.sign.Md5Signer;
import com.acooly.openapi.framework.core.security.sign.Signer;
import com.github.kevinsawicki.http.HttpRequest;
import com.google.common.base.Charsets;
import com.google.common.base.Splitter;
import com.google.common.base.Strings;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.List;
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

  protected static final String ENCODING = "utf-8";
  protected Logger logger = LoggerFactory.getLogger(this.getClass());
  protected String signType = SignTypeEnum.MD5.toString();
  protected String protocal = ApiProtocol.JSON.code();
  protected String gatewayUrl = "http://127.0.0.1:8089/gateway.do";
  protected String accessKey = TEST_ACCESS_KEY;
  protected String secretKey = TEST_SECRET_KEY;
  protected String partnerId = "xfinpay.com";
  protected String service = "";
  protected String version = "1.0";
  protected String notifyUrl = "";
  protected String returnUrl = "";

  protected boolean showLog = true;

  protected Signer signer = new Md5Signer();

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
    String body = JsonMarshallor.INSTANCE.marshall(request);
    Map<String, String> requestHeader = Maps.newTreeMap();
    requestHeader.put(ApiConstants.ACCESS_KEY, accessKey);
    requestHeader.put(ApiConstants.SIGN_TYPE, "MD5");
    requestHeader.put(ApiConstants.SIGN, sign(body));
    if (showLog) {
      log.info("请求-> header:{} body:{}", requestHeader, body);
    }
    HttpRequest httpRequest =
        HttpRequest.post(gatewayUrl).headers(requestHeader).followRedirects(false).send(body);
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

  protected String encrypt(String text) {
    byte[] securityKey = secretKey.substring(0, 16).getBytes();
    byte[] encrypt = Cryptos.aesEncrypt(text.getBytes(), securityKey);
    return Encodes.encodeBase64(encrypt);
  }
}
