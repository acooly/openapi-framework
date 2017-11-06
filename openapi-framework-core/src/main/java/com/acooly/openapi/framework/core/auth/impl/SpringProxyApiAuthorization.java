/*
 * acooly.cn Inc.
 * Copyright (c) 2016 All Rights Reserved.
 * create by zhangpu
 * date:2016年3月17日
 */
package com.acooly.openapi.framework.core.auth.impl;

import com.acooly.integration.bean.AbstractSpringProxyBean;
import com.acooly.openapi.framework.common.message.ApiRequest;
import com.acooly.openapi.framework.core.auth.ApiAuthorization;
import org.springframework.stereotype.Service;

/**
 * 认证授权 spring代理实现.
 *
 * <p>自动判断优先使用目标集成系统的认证实现,如果没有,则使用默认实现(DafaultApiAuthorizer)
 *
 * @author acooly
 * @date 2016-02-29
 */
@Service("apiAuthorization")
public class SpringProxyApiAuthorization
    extends AbstractSpringProxyBean<ApiAuthorization, DefaultApiAuthorization>
    implements ApiAuthorization {

  @Override
  public void authorize(ApiRequest apiRequest) {
    getTarget().authorize(apiRequest);
  }
}
