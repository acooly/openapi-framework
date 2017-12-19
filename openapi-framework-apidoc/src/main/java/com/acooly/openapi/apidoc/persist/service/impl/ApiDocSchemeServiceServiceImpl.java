/*
 * acooly.cn Inc.
 * Copyright (c) 2017 All Rights Reserved.
 * create by acooly
 * date:2017-12-05
 */
package com.acooly.openapi.apidoc.persist.service.impl;

import org.springframework.stereotype.Service;

import com.acooly.core.common.service.EntityServiceImpl;
import com.acooly.openapi.apidoc.persist.service.ApiDocSchemeServiceService;
import com.acooly.openapi.apidoc.persist.dao.ApiDocSchemeServiceDao;
import com.acooly.openapi.apidoc.persist.entity.ApiDocSchemeService;

/**
 * 方案服务列表 Service实现
 *
 * Date: 2017-12-05 12:34:39
 *
 * @author acooly
 *
 */
@Service("apiDocSchemeServiceService")
public class ApiDocSchemeServiceServiceImpl extends EntityServiceImpl<ApiDocSchemeService, ApiDocSchemeServiceDao> implements ApiDocSchemeServiceService {

}
