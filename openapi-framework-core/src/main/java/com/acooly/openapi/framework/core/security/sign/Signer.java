/*
 * acooly.com Inc.
 * Copyright (c) 2014 All Rights Reserved
 */
package com.acooly.openapi.framework.core.security.sign;

import com.acooly.openapi.framework.common.enums.SignTypeEnum;
import com.acooly.openapi.framework.core.exception.impl.ApiServiceAuthenticationException;

/**
 * 签名接口
 *
 * @param <T>
 * @author zhangpu
 * @date 2014年6月3日
 */
public interface Signer<T> {

    String sign(String t, String key);

    /**
     * 认证失败抛出异常
     *
     * @param sign
     * @param key
     * @throws ApiServiceAuthenticationException
     */
    void verify(String sign, String key, String body) throws ApiServiceAuthenticationException;

    /**
     * 签名类型
     *
     * @return
     */
    SignTypeEnum getSinType();
}
