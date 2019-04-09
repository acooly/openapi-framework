/*
 * acooly.com Inc.
 * Copyright (c) 2014 All Rights Reserved
 */
package com.acooly.openapi.framework.service.test.api;

import com.acooly.openapi.framework.common.annotation.OpenApiService;
import com.acooly.openapi.framework.common.enums.ResponseType;
import com.acooly.openapi.framework.core.service.base.BaseApiService;
import com.acooly.openapi.framework.service.test.request.WithdrawRequest;
import com.acooly.openapi.framework.service.test.response.WithdrawResponse;
import org.apache.commons.codec.digest.DigestUtils;

/**
 * @author zhangpu
 * @date 2014年7月29日
 */
@OpenApiService(name = "withdraw", desc = "提现", responseType = ResponseType.REDIRECT)
public class WithdrawApiService extends BaseApiService<WithdrawRequest, WithdrawResponse> {

  @Override
  protected void doService(WithdrawRequest request, WithdrawResponse response) {
    response.setTradeNo(DigestUtils.md5Hex(request.getRequestNo()));
  }
}
