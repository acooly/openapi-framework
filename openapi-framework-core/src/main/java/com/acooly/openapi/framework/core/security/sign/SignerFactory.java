/*
 * acooly.com Inc.
 * Copyright (c) 2014 All Rights Reserved
 */
package com.acooly.openapi.framework.core.security.sign;

import com.acooly.openapi.framework.common.enums.SignTypeEnum;

/**
 * 签名对象工厂
 *
 * @author zhangpu
 * @date 2014年6月3日
 * @param <T>
 */
public interface SignerFactory<T> {

  Signer<T> getSigner(SignTypeEnum signType);
}
