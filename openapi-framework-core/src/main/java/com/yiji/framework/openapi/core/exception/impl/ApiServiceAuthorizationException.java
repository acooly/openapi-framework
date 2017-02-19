/*
 * acooly.cn Inc.
 * Copyright (c) 2016 All Rights Reserved.
 * create by zhangpu 
 * date:2016年3月17日
 *
 */
package com.yiji.framework.openapi.core.exception.impl;

import com.yiji.framework.openapi.common.enums.ApiServiceResultCode;
import com.yiji.framework.openapi.common.exception.ApiServiceException;

/**
 * 服务授权异常
 * 
 * @author zhangpu
 */
public class ApiServiceAuthorizationException extends ApiServiceException {
	/** serialVersionUID */
	private static final long serialVersionUID = -2608417216952576356L;

	public ApiServiceAuthorizationException(String detail) {
		super(ApiServiceResultCode.UN_AUTHORIZED_ERROR.getCode(), ApiServiceResultCode.UN_AUTHORIZED_ERROR.getMessage(),
				detail);
	}

}
