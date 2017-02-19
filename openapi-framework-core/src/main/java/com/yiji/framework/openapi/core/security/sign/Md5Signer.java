/*
 * www.yiji.com Inc.
 * Copyright (c) 2014 All Rights Reserved
 */
package com.yiji.framework.openapi.core.security.sign;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.stereotype.Component;

/**
 * MD5签名实现
 *
 * @author zhangpu
 * @date 2014年8月3日
 */
@Component("apiMd5Signer")
public class Md5Signer extends AbstractMapSourceSigner {

	@Override
	protected String doSign(String waitToSignStr, String key) {
		return DigestUtils.md5Hex(waitToSignStr + key);
	}

	@Override
	public SignTypeEnum getSinType() {
		return SignTypeEnum.MD5;
	}

}
