/*
 * www.yiji.com Inc.
 * Copyright (c) 2014 All Rights Reserved
 */
package com.yiji.framework.openapi.core.security.sign;

import com.yiji.framework.openapi.core.exception.impl.ApiServiceAuthenticationException;

/**
 * 签名接口
 * @author zhangpu
 * @date 2014年6月3日
 * @param <T>
 */
public interface Signer<T> {
	
	String sign(T t, Object key);

    /**
     * 认证失败抛出异常
     * @param sign
     * @param key
     * @param t
     * @throws ApiServiceAuthenticationException
     */
	void verify(String sign, String key, T t) throws ApiServiceAuthenticationException;
	
	SignTypeEnum getSinType();
}
