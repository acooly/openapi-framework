/*
 * acooly.cn Inc.
 * Copyright (c) 2016 All Rights Reserved.
 * create by acooly
 * date:2016-07-16
 *
 */
package com.yiji.framework.openapi.service.persistent;

import com.yiji.framework.openapi.service.persistent.dao.ApiSchemeDao;
import org.springframework.stereotype.Service;

import com.acooly.core.common.service.EntityServiceImpl;
import com.yiji.framework.openapi.service.ApiSchemeService;
import com.yiji.framework.openapi.domain.ApiScheme;

/**
 * 服务方案 Service实现
 *
 * Date: 2016-07-16 01:57:25
 *
 * @author acooly
 *
 */
@Service("apiSchemeService")
public class ApiSchemeServiceImpl extends EntityServiceImpl<ApiScheme, ApiSchemeDao> implements ApiSchemeService {

}
