/*
 * acooly.cn Inc.
 * Copyright (c) 2018 All Rights Reserved.
 * create by qiubo
 * date:2018-08-21
 *
 */
package com.acooly.openapi.framework.service.service;

import com.acooly.core.common.service.EntityService;
import com.acooly.openapi.framework.service.domain.ApiAuth;

/**
 * 认证授权信息管理 Service接口
 *
 * Date: 2018-08-21 14:31:06
 * @author qiubo
 *
 */
public interface ApiAuthService extends EntityService<ApiAuth> {
    ApiAuth findByAccesskey( String accesskey);
}
