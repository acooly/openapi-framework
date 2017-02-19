package com.yiji.framework.openapi.core.security.sign;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.stereotype.Component;

/**
 * Sha1Hex 签名和验签
 * <p/>
 * Created by zhangpu on 2015/1/23.
 */
@Component("apiSha1Signer")
public class Sha1Signer extends AbstractMapSourceSigner {

	@Override
	protected String doSign(String waitToSignStr, String key) {
		return DigestUtils.sha1Hex(waitToSignStr + key);
	}

	@Override
	public SignTypeEnum getSinType() {
		return SignTypeEnum.Sha1Hex;
	}
}