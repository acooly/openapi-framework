package com.acooly.openapi.framework.core.security.sign;

import com.acooly.core.utils.security.Cryptos;
import com.acooly.openapi.framework.common.enums.SignTypeEnum;
import org.springframework.stereotype.Component;

/**
 * HMACSHA1 签名和验签
 *
 * <p>Created by zhangpu on 2015/1/23.
 */
@Component("apiHmacSha1Signer")
public class HmacSha1Signer extends AbstractSigner {

  @Override
  protected String doSign(String waitToSignStr, String key) {
    return Cryptos.hmacSha1(waitToSignStr, key);
  }

  @Override
  public SignTypeEnum getSinType() {
    return SignTypeEnum.HMACSHA1HEX;
  }
}
