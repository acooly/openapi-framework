/*
 * www.yiji.com Inc.
 * Copyright (c) 2014 All Rights Reserved
 */
package com.yiji.framework.openapi.core.security.sign;

/**
 * 签名对象工厂
 * @author zhangpu
 * @date 2014年6月3日
 * @param <T>
 */
public interface SignerFactory<T> {

	Signer<T> getSigner(String signType);

}
