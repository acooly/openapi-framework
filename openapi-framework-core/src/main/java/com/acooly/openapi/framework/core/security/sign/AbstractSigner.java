package com.acooly.openapi.framework.core.security.sign;

import com.acooly.openapi.framework.common.ApiConstants;
import com.acooly.openapi.framework.core.exception.impl.ApiServiceAuthenticationException;
import com.acooly.openapi.framework.core.executer.ApiContext;
import com.acooly.openapi.framework.core.util.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.TreeMap;

/** Created by zhangpu on 2015/1/23. */
public abstract class AbstractSigner implements Signer<ApiContext> {

  private static final Logger logger = LoggerFactory.getLogger(AbstractSigner.class);

  @Override
  public String sign(ApiContext apiContext, Object key) {
    return doSign(apiContext, key, false).getF();
  }

  @Override
  public void verify(String sign, String key, ApiContext apiContext)
      throws ApiServiceAuthenticationException {
    Pair<String, String> signPair = doSign(apiContext, key, true);
    if (!sign.equals(signPair.getF())) {
      throw new ApiServiceAuthenticationException("待签名字符串[" + signPair.getS() + "]");
    }
  }

  protected Pair<String, String> doSign(ApiContext apiContext, Object key, boolean isRequest) {
    String waitToSignStr = doStringToSign(apiContext, isRequest);
    String signature = doSign(waitToSignStr, (String) key);
    logger.debug("请求报文待签字符串:{},key:{},signature:{}", waitToSignStr, key, signature);
    Pair<String, String> pair = new Pair<>(signature, waitToSignStr);
    return pair;
  }

  protected String doStringToSign(ApiContext apiContext, boolean isRequest) {
    Map<String, String> sortedMap = new TreeMap<>();
    sortedMap.put(ApiConstants.SIGN_TYPE, apiContext.getSignType().name());
    sortedMap.put(ApiConstants.PROTOCOL, apiContext.getProtocol().name());
    sortedMap.put(ApiConstants.SERVICE, apiContext.getServiceName());
    sortedMap.put(ApiConstants.VERSION, apiContext.getServiceVersion());
    sortedMap.put(ApiConstants.PARTNER_ID, apiContext.getPartnerId());
    String body;
    if (isRequest) {
      body = apiContext.getRequestBody();
    } else {
      body = apiContext.getResponseBody();
    }
    String waitToSignStr = null;
    StringBuilder stringToSign = new StringBuilder();
    if (sortedMap.size() > 0) {
      for (Map.Entry<String, String> entry : sortedMap.entrySet()) {
        if (entry.getValue() != null) {
          stringToSign.append(entry.getKey()).append("=").append(entry.getValue()).append("&");
        }
      }
      stringToSign.deleteCharAt(stringToSign.length() - 1);
      stringToSign.append(body);
      waitToSignStr = stringToSign.toString();
      logger.debug("代签名字符串:{}", waitToSignStr);
    }
    return waitToSignStr;
  }

  protected abstract String doSign(String waitToSignStr, String key);
}
