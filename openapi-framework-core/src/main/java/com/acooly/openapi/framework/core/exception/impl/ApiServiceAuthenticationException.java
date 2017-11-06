package com.acooly.openapi.framework.core.exception.impl;

import com.acooly.openapi.framework.common.enums.ApiServiceResultCode;
import com.acooly.openapi.framework.common.exception.ApiServiceException;

/**
 * Api Servie 认证异常
 *
 * @author zhangpu
 * @date 2014年5月17日
 */
public class ApiServiceAuthenticationException extends ApiServiceException {

  /** UID */
  private static final long serialVersionUID = 2193413258894579969L;

  public ApiServiceAuthenticationException() {
    this(null);
  }

  public ApiServiceAuthenticationException(String detail) {
    super(
        ApiServiceResultCode.UN_AUTHENTICATED_ERROR.getCode(),
        ApiServiceResultCode.UN_AUTHENTICATED_ERROR.getMessage(),
        detail);
  }
}
