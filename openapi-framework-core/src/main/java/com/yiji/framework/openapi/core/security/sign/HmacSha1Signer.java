package com.yiji.framework.openapi.core.security.sign;

import org.springframework.stereotype.Component;

import com.acooly.core.utils.Cryptos;

/**
 * HMACSHA1 签名和验签
 * <p/>
 * Created by zhangpu on 2015/1/23.
 */
@Component("apiHmacSha1Signer")
public class HmacSha1Signer extends AbstractMapSourceSigner {

	@Override
	protected String doSign(String waitToSignStr, String key) {
		return Cryptos.hmacSha1(waitToSignStr, key);
	}

	@Override
	public SignTypeEnum getSinType() {
		return SignTypeEnum.HmacSha1Hex;
	}
}
