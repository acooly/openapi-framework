/*
 * acooly.cn Inc.
 * Copyright (c) 2018 All Rights Reserved.
 * create by qiubo
 * date:2018-08-21
 */
package com.acooly.openapi.framework.serviceimpl.manage.service.impl;

import org.springframework.stereotype.Service;

import com.acooly.core.common.service.EntityServiceImpl;
import com.acooly.openapi.framework.serviceimpl.manage.service.ApiTenantService;
import com.acooly.openapi.framework.serviceimpl.manage.dao.ApiTenantDao;
import com.acooly.openapi.framework.serviceimpl.manage.entity.ApiTenant;

/**
 * 租户管理 Service实现
 *
 * Date: 2018-08-21 14:31:06
 *
 * @author qiubo
 *
 */
@Service("apiTenantService")
public class ApiTenantServiceImpl extends EntityServiceImpl<ApiTenant, ApiTenantDao> implements ApiTenantService {

}
