/*
 * acooly.com Inc.
 * Copyright (c) 2014 All Rights Reserved
 */
package com.acooly.openapi.framework.core.service.base;

import com.acooly.openapi.framework.common.message.ApiRequest;
import com.acooly.openapi.framework.common.message.ApiResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Api服务基类
 *
 * <p>提供转换工具和帮助支持
 *
 * @author zhangpu
 * @date 2014年5月16日
 */
public abstract class BaseApiService<O extends ApiRequest, R extends ApiResponse>
    extends AbstractApiService<O, R> {

  protected static Logger logger = LoggerFactory.getLogger(BaseApiService.class);
}
