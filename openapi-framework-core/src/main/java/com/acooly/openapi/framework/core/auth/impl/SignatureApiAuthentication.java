package com.acooly.openapi.framework.core.auth.impl;

import com.acooly.core.common.boot.Env;
import com.acooly.core.utils.enums.Messageable;
import com.acooly.openapi.framework.common.ApiConstants;
import com.acooly.openapi.framework.common.exception.ApiServiceException;
import com.acooly.openapi.framework.core.auth.ApiAuthentication;
import com.acooly.openapi.framework.core.auth.realm.AuthInfoRealm;
import com.acooly.openapi.framework.core.exception.impl.ApiServiceAuthenticationException;
import com.acooly.openapi.framework.common.context.ApiContext;
import com.acooly.openapi.framework.common.context.ApiContextHolder;
import com.acooly.openapi.framework.common.enums.SignTypeEnum;
import com.acooly.openapi.framework.core.security.sign.Signer;
import com.acooly.openapi.framework.core.security.sign.SignerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Map;

import static com.acooly.openapi.framework.common.ApiConstants.TEST_ACCESS_KEY;

/**
 * 签名认证实现
 *
 * @author zhangpu
 */
@Component
public class SignatureApiAuthentication implements ApiAuthentication {
  private static final Logger logger = LoggerFactory.getLogger(SignatureApiAuthentication.class);
  private static NotSupport notSupport = new NotSupport();
  @Resource protected SignerFactory<ApiContext> signerFactory;
  @Resource protected AuthInfoRealm authInfoRealm;

  /** 认证 */
  @Override
  public void authenticate(ApiContext apiContext) {
    try {
      String requestSign = apiContext.getSign();
      String accessKey = apiContext.getAccessKey();
      if (TEST_ACCESS_KEY.equals(accessKey)) {
        if (Env.isOnline()) {
          throw new ApiServiceException(notSupport);
        }
      }
      Signer<ApiContext> signer = signerFactory.getSigner(apiContext.getSignType());
      signer.verify(
          requestSign, (String) authInfoRealm.getAuthenticationInfo(accessKey), apiContext);
    } catch (ApiServiceException asae) {
      throw asae;
    } catch (Exception e) {
      logger.warn("签名认证异常", e);
      throw new ApiServiceAuthenticationException();
    }
  }

  /**
   * 签名
   *
   * @param response
   */
  @Override
  public String signature(Map<String, String> response) {
    String signType = response.get(ApiConstants.SIGN_TYPE);
    //fixme
//    String partnerId = response.get(ApiConstants.PARTNER_ID);
//    return signature(response, partnerId, signType);
    return null;
  }

  @Override
  public String signature(Map<String, String> responseData, String partnerId, String signType) {
    try {
      String sign =
          signerFactory
              .getSigner(SignTypeEnum.valueOf(signType))
              .sign(
                  ApiContextHolder.getApiContext(), authInfoRealm.getAuthenticationInfo(partnerId));
      return sign;
    } catch (ApiServiceException asae) {
      throw asae;
    } catch (Exception e) {
      logger.warn("签名异常", e);
      throw new ApiServiceAuthenticationException("签名错误");
    }
  }

  @Override
  public String signature(String body, String accessKey, String signType) {
    try {
      if(accessKey==null){
        return "";
      }
      String sign =
          signerFactory
              .getSigner(SignTypeEnum.valueOf(signType))
              .sign(
                  ApiContextHolder.getApiContext(), authInfoRealm.getAuthenticationInfo(accessKey));
      return sign;
    } catch (ApiServiceException asae) {
      throw asae;
    } catch (Exception e) {
      logger.warn("签名异常", e);
      throw new ApiServiceAuthenticationException("签名错误");
    }
  }

  public static class NotSupport implements Messageable {

    @Override
    public String code() {
      return "TEST_NOT_SUPPORT_IN_PRODUCTION";
    }

    @Override
    public String message() {
      return "生产环境不能使用测试帐号";
    }
  }
}
