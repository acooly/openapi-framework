package com.acooly.openapi.framework.core.test;

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
import com.github.kevinsawicki.http.HttpRequest;
import com.google.common.base.Strings;
import org.apache.commons.codec.digest.DigestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

import java.util.List;
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
  protected String protocal = ApiProtocol.JSON.code();
  protected String gatewayUrl = "http://127.0.0.1:8080/gateway.do";
  protected String key = OpenApiConstants.DEF_SECRETKEY;
  protected String partnerId = "zhangpu";
  protected String service = "";
  protected String version = "1.0";
  protected String notifyUrl = "";
  protected String returnUrl = "";

  protected Signer signer = new Md5Signer();

  protected static Map<String, String> marshall(ApiMessage message) {
    return ObjectAccessor.of(message).getAllDataExcludeTransient();
  }

  protected <T> T request(ApiRequest request, Class<T> clazz) {
    if (Strings.isNullOrEmpty(request.getPartnerId())) {
      request.setPartnerId(partnerId);
    }
    Assert.hasText(request.getPartnerId());
    if (Strings.isNullOrEmpty(request.getVersion())) {
      request.setVersion(version);
    }
    Assert.hasText(request.getVersion());
    if (Strings.isNullOrEmpty(request.getService())) {
      request.setService(service);
    }
    Assert.hasText(request.getService());

    String body = JsonMarshallor.INSTANCE.marshall(request);
    HttpRequest httpRequest =
        HttpRequest.post(gatewayUrl)
            .header("signType", "MD5")
            .header("sign", sign(body))
            .send(body);
    Map<String, List<String>> headers = httpRequest.headers();
    String responseBody = httpRequest.body();
    List<String> signList = headers.get("sign");
    if (signList != null) {
      String sign = signList.get(0);
      String signType = headers.get("signType").get(0);
      if (!sign(responseBody).equals(sign)) {
        throw new RuntimeException("验证失败");
      }
    }
    return JsonMarshallor.INSTANCE.parse(responseBody, clazz);
  }

  public String sign(String body) {
    return DigestUtils.md5Hex(body + key);
  }

  protected String encrypt(String text) {
    byte[] securityKey = key.substring(0, 16).getBytes();
    byte[] encrypt = Cryptos.aesEncrypt(text.getBytes(), securityKey);
    return Encodes.encodeBase64(encrypt);
  }
}
