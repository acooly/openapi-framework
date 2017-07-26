package com.acooly.openapi.framework.core.security.sign;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.stereotype.Component;

/**
 * Sha256Hex 签名和验签
 * <p/>
 * Created by zhangpu on 2015/1/23.
 */
@Component("apiSha256Signer")
public class Sha256Signer extends AbstractMapSourceSigner {

	@Override
	protected String doSign(String waitToSignStr, String key) {
		return DigestUtils.sha256Hex(waitToSignStr + key);
	}

	@Override
	public SignTypeEnum getSinType() {
		return SignTypeEnum.Sha256Hex;
	}
}
