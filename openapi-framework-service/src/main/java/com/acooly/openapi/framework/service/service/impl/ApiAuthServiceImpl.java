/*
 * acooly.cn Inc.
 * Copyright (c) 2018 All Rights Reserved.
 * create by qiubo
 * date:2018-08-21
 */
package com.acooly.openapi.framework.service.service.impl;

import com.acooly.core.common.exception.BusinessException;
import com.acooly.core.common.service.EntityServiceImpl;
import com.acooly.openapi.framework.service.dao.ApiAuthDao;
import com.acooly.openapi.framework.service.domain.ApiAuth;
import com.acooly.openapi.framework.service.service.ApiAuthService;
import org.springframework.stereotype.Service;

/**
 * 认证授权信息管理 Service实现
 * <p>
 * Date: 2018-08-21 14:31:06
 *
 * @author qiubo
 */
@Service("apiAuthService")
public class ApiAuthServiceImpl extends EntityServiceImpl<ApiAuth, ApiAuthDao> implements ApiAuthService {
//    @Autowired(required = false)
//    private AuthInfoRealm authInfoRealm;

    @Override
    public ApiAuth findByAccesskey(String accesskey) {
        return this.getEntityDao().findByAccesskey(accesskey);
    }

    @Override
    public void update(ApiAuth o) throws BusinessException {
//        if (authInfoRealm instanceof CacheableAuthInfoRealm) {
//            ((CacheableAuthInfoRealm) authInfoRealm).removeCache(o.getAccessKey());
//        }
        super.update(o);
    }
}
