package com.yiji.framework.openapi.core.marshall.crypt;

import java.io.UnsupportedEncodingException;

import com.yiji.framework.openapi.common.enums.ApiServiceResultCode;
import com.yiji.framework.openapi.common.exception.ApiServiceException;
import com.yiji.framework.openapi.common.utils.Cryptos;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.acooly.core.utils.Encodes;
import com.acooly.core.utils.Exceptions;
import com.yiji.framework.openapi.core.auth.realm.AuthInfoRealm;

@Component
public class ApiMarshallCryptServiceImpl implements ApiMarshallCryptService {

	private static final Logger logger = LoggerFactory.getLogger(ApiMarshallCryptServiceImpl.class);

	@Autowired
	private AuthInfoRealm authInfoRealm;

	/**
	 * 无编码和向量AES解密
	 */
	@Override
	public String encrypt(String property, String text, String key) {
		try {
			byte[] securityKey = getSecurityKey(key);
			byte[] encrypt = Cryptos.aesEncrypt(text.getBytes("UTF-8"), securityKey);
			return Encodes.encodeBase64(encrypt);
		} catch (Exception e) {
			logger.warn("数据项[{}]加密失败:{}", property, e.getMessage());
			throw new ApiServiceException(ApiServiceResultCode.PARAMETER_ERROR, "数据项" + property + "加密失败.");
		}
	}

	/**
	 * 无编码和向量AES加密
	 * <p/>
	 * 返回：密文的HexString格式
	 */
	@Override
	public String decrypt(String property, String text, String key) {
		try {
			byte[] securityKey = getSecurityKey(key);
			byte[] data = Encodes.decodeBase64(text);
			return Cryptos.aesDecrypt(data, securityKey);
		} catch (Exception e) {
			logger.warn("数据项[{}]解密失败:{}", property, e.getMessage());
			throw new ApiServiceException(ApiServiceResultCode.PARAMETER_ERROR, "数据项" + property + "解密失败.");
		}
	}

	protected byte[] getSecurityKey(String key) {
		String securityKey = (String) authInfoRealm.getAuthenticationInfo(key);
		try {
			return securityKey.substring(0, 16).getBytes("UTF-8");
		} catch (UnsupportedEncodingException e) {
			throw Exceptions.unchecked(e);
		}
	}

	public void setAuthInfoRealm(AuthInfoRealm authInfoRealm) {
		this.authInfoRealm = authInfoRealm;
	}

}
