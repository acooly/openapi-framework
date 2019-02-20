/*
 * acooly.cn Inc.
 * Copyright (c) 2019 All Rights Reserved.
 * create by zhike
 * date:2019-02-20
 *
 */
package com.acooly.openapi.apidoc.persist.service;

import com.acooly.core.common.service.EntityService;
import com.acooly.openapi.apidoc.persist.entity.ApiDocSchemeDesc;

/**
 * 服务方案描述 Service接口
 *
 * Date: 2019-02-20 15:39:15
 * @author zhike
 *
 */
public interface ApiDocSchemeDescService extends EntityService<ApiDocSchemeDesc> {

    ApiDocSchemeDesc findBySchemeNo(String schemeNo);
}
