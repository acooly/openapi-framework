/*
 * acooly.cn Inc.
 * Copyright (c) 2016 All Rights Reserved.
 * create by acooly
 * date:2016-07-16
 *
 */
package com.acooly.openapi.framework.service.persistent;

import com.acooly.core.common.service.EntityServiceImpl;
import com.acooly.openapi.framework.domain.ApiScheme;
import com.acooly.openapi.framework.service.ApiSchemeService;
import com.acooly.openapi.framework.service.persistent.dao.ApiSchemeDao;
import org.springframework.stereotype.Service;

/**
 * 服务方案 Service实现
 *
 * <p>Date: 2016-07-16 01:57:25
 *
 * @author acooly
 */
@Service("apiSchemeService")
public class ApiSchemeServiceImpl extends EntityServiceImpl<ApiScheme, ApiSchemeDao>
    implements ApiSchemeService {}
