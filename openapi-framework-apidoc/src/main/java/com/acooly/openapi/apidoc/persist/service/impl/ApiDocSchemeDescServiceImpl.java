/*
 * acooly.cn Inc.
 * Copyright (c) 2019 All Rights Reserved.
 * create by zhike
 * date:2019-02-20
 */
package com.acooly.openapi.apidoc.persist.service.impl;

import com.acooly.core.common.service.EntityService;
import com.acooly.core.common.service.EntityServiceImpl;
import com.acooly.openapi.apidoc.persist.dao.ApiDocSchemeDescDao;
import com.acooly.openapi.apidoc.persist.entity.ApiDocSchemeDesc;
import com.acooly.openapi.apidoc.persist.service.ApiDocSchemeDescService;
import org.springframework.stereotype.Service;

/**
 * 服务方案描述 Service实现
 *
 * Date: 2019-02-20 15:39:16
 *
 * @author zhike
 *
 */
@Service("apiDocSchemeDescService")
public class ApiDocSchemeDescServiceImpl extends EntityServiceImpl<ApiDocSchemeDesc, ApiDocSchemeDescDao> implements ApiDocSchemeDescService {

    @Override
    public ApiDocSchemeDesc findBySchemeNo(String schemeNo) {
        return this.getEntityDao().findBySchemeNo(schemeNo);
    }
}
