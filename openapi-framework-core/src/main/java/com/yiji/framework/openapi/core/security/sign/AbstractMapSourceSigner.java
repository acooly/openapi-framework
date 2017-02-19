package com.yiji.framework.openapi.core.security.sign;

import java.util.Map;
import java.util.TreeMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.yiji.framework.openapi.core.exception.impl.ApiServiceAuthenticationException;
import com.yiji.framework.openapi.core.util.Pair;

/**
 * Created by zhangpu on 2015/1/23.
 */
public abstract class AbstractMapSourceSigner implements Signer<Map<String, String>> {

	private static final Logger logger = LoggerFactory.getLogger(AbstractMapSourceSigner.class);

	@Override
	public String sign(Map<String, String> stringStringMap, Object key) {
		return doSign(stringStringMap, key).getF();
	}

	@Override
	public void verify(String sign, String key, Map<String, String> stringStringMap)
			throws ApiServiceAuthenticationException {
		Pair<String, String> signPair = doSign(stringStringMap, key);
		if (!sign.equals(signPair.getF())) {
			throw new ApiServiceAuthenticationException("待签名字符串[" + signPair.getS() + "]");
		}
	}

	protected Pair<String, String> doSign(Map<String, String> stringStringMap, Object key) {
		String waitToSignStr = doStringToSign(stringStringMap);
		String signature = doSign(waitToSignStr, (String) key);
		logger.debug("请求报文待签字符串:{},key:{},signature:{}", waitToSignStr, key, signature);
		Pair<String, String> pair = new Pair<>(signature, waitToSignStr);
		return pair;
	}

	protected String doStringToSign(Map<String, String> stringStringMap) {
		String waitToSignStr = null;
		Map<String, String> sortedMap = new TreeMap<>(stringStringMap);
		if (sortedMap.containsKey("sign")) {
			sortedMap.remove("sign");
		}
		StringBuilder stringToSign = new StringBuilder();
		if (sortedMap.size() > 0) {
			for (Map.Entry<String, String> entry : sortedMap.entrySet()) {
				if (entry.getValue() != null) {
					stringToSign.append(entry.getKey()).append("=").append(entry.getValue()).append("&");
				}
			}
			stringToSign.deleteCharAt(stringToSign.length() - 1);
			waitToSignStr = stringToSign.toString();
			logger.debug("代签名字符串:{}", waitToSignStr);
		}
		return waitToSignStr;
	}

	protected abstract String doSign(String waitToSignStr, String key);

}
