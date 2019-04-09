package com.acooly.openapi.framework.core.security.sign;

import com.acooly.openapi.framework.common.enums.SignTypeEnum;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.stereotype.Component;

/**
 * SHA256HEX 签名和验签
 *
 * <p>Created by zhangpu on 2015/1/23.
 */
@Component("apiSha256Signer")
public class Sha256Signer extends AbstractSigner {

  @Override
  protected String doSign(String waitToSignStr, String key) {
    return DigestUtils.sha256Hex(waitToSignStr + key);
  }

  @Override
  public SignTypeEnum getSinType() {
    return SignTypeEnum.SHA256HEX;
  }
}
