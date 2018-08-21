/*
 * acooly.cn Inc.
 * Copyright (c) 2018 All Rights Reserved.
 * create by qiubo
 * date:2018-08-21
 */
package com.acooly.openapi.framework.serviceimpl.manage.service.impl;

import org.springframework.stereotype.Service;

import com.acooly.core.common.service.EntityServiceImpl;
import com.acooly.openapi.framework.serviceimpl.manage.service.ApiAuthService;
import com.acooly.openapi.framework.serviceimpl.manage.dao.ApiAuthDao;
import com.acooly.openapi.framework.serviceimpl.manage.entity.ApiAuth;

/**
 * 认证授权信息管理 Service实现
 *
 * Date: 2018-08-21 14:31:06
 *
 * @author qiubo
 *
 */
@Service("apiAuthService")
public class ApiAuthServiceImpl extends EntityServiceImpl<ApiAuth, ApiAuthDao> implements ApiAuthService {

    @Override
    public ApiAuth findByAccesskey(String accesskey) {
        return this.getEntityDao().findByAccesskey(accesskey);
    }
}
