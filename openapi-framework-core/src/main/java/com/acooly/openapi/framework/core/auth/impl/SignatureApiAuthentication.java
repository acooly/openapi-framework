package com.acooly.openapi.framework.core.auth.impl;

import com.acooly.core.common.boot.Env;
import com.acooly.core.utils.enums.Messageable;
import com.acooly.openapi.framework.common.ApiConstants;
import com.acooly.openapi.framework.common.exception.ApiServiceException;
import com.acooly.openapi.framework.common.utils.ApiUtils;
import com.acooly.openapi.framework.common.utils.Strings;
import com.acooly.openapi.framework.core.auth.ApiAuthentication;
import com.acooly.openapi.framework.core.auth.realm.AuthInfoRealm;
import com.acooly.openapi.framework.core.exception.impl.ApiServiceAuthenticationException;
import com.acooly.openapi.framework.core.security.sign.SignTypeEnum;
import com.acooly.openapi.framework.core.security.sign.Signer;
import com.acooly.openapi.framework.core.security.sign.SignerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Map;

/**
 * 签名认证实现
 *
 * @author zhangpu
 */
@Component
public class SignatureApiAuthentication implements ApiAuthentication {
  private static final Logger logger = LoggerFactory.getLogger(SignatureApiAuthentication.class);
  private static NotSupport notSupport = new NotSupport();
  @Resource protected SignerFactory<Map<String, String>> signerFactory;
  @Resource protected AuthInfoRealm authInfoRealm;

  /**
   * 认证
   *
   * @param requestData
   */
  @Override
  public void authenticate(Map<String, String> requestData) {
    try {
      String signType =
          Strings.isBlankDefault(
              ApiUtils.getParameter(requestData, ApiConstants.SIGN_TYPE), SignTypeEnum.MD5.name());
      String requestSign = ApiUtils.getParameter(requestData, ApiConstants.SIGN);
      String partnerId = ApiUtils.getParameter(requestData, ApiConstants.PARTNER_ID);
      if ("test".equals(partnerId)) {
        if (Env.isOnline()) {
          throw new ApiServiceException(notSupport);
        }
      }
      Signer<Map<String, String>> signer = signerFactory.getSigner(signType);
      signer.verify(
          requestSign, (String) authInfoRealm.getAuthenticationInfo(partnerId), requestData);
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
  public void signature(Map<String, String> response) {
    String signType = response.get(ApiConstants.SIGN_TYPE);
    String partnerId = response.get(ApiConstants.PARTNER_ID);
    signature(response, partnerId, signType);
  }

  @Override
  public void signature(Map<String, String> responseData, String partnerId, String signType) {
    try {
      String sign =
          signerFactory
              .getSigner(signType)
              .sign(responseData, authInfoRealm.getAuthenticationInfo(partnerId));
      responseData.put(ApiConstants.SIGN, sign);
    } catch (ApiServiceException asae) {
      throw asae;
    } catch (Exception e) {
      logger.warn("签名异常", e);
      throw new ApiServiceAuthenticationException("签名错误");
    }
  }

  public void setSignerFactory(SignerFactory<Map<String, String>> signerFactory) {
    this.signerFactory = signerFactory;
  }

  public void setAuthInfoRealm(AuthInfoRealm authInfoRealm) {
    this.authInfoRealm = authInfoRealm;
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
