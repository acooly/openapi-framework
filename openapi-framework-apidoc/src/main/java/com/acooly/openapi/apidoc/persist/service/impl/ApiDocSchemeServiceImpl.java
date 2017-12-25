/*
 * acooly.cn Inc.
 * Copyright (c) 2017 All Rights Reserved.
 * create by acooly
 * date:2017-12-05
 */
package com.acooly.openapi.apidoc.persist.service.impl;

import org.springframework.stereotype.Service;

import com.acooly.core.common.service.EntityServiceImpl;
import com.acooly.openapi.apidoc.persist.service.ApiDocSchemeService;
import com.acooly.openapi.apidoc.persist.dao.ApiDocSchemeDao;
import com.acooly.openapi.apidoc.persist.entity.ApiDocScheme;

/**
 * 服务方案 Service实现
 *
 * Date: 2017-12-05 12:34:38
 *
 * @author acooly
 *
 */
@Service("apiDocSchemeService")
public class ApiDocSchemeServiceImpl extends EntityServiceImpl<ApiDocScheme, ApiDocSchemeDao> implements ApiDocSchemeService {

}
