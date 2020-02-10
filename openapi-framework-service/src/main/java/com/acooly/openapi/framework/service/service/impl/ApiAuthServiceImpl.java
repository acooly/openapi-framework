/*
 * acooly.cn Inc.
 * Copyright (c) 2018 All Rights Reserved.
 * create by qiubo
 * date:2018-08-21
 */
package com.acooly.openapi.framework.service.service.impl;

import com.acooly.core.common.exception.BusinessException;
import com.acooly.core.common.service.EntityServiceImpl;
import com.acooly.core.utils.Ids;
import com.acooly.core.utils.Strings;
import com.acooly.module.event.EventBus;
import com.acooly.openapi.framework.service.dao.ApiAuthDao;
import com.acooly.openapi.framework.service.domain.ApiAuth;
import com.acooly.openapi.framework.service.event.ApiAuthUpdateEvent;
import com.acooly.openapi.framework.service.service.ApiAuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 认证授权信息管理 Service实现
 * <p>
 * Date: 2018-08-21 14:31:06
 *
 * @author qiubo
 * @author zhangpu : 调整缓存更新为事件模式
 */
@Service("apiAuthService")
public class ApiAuthServiceImpl extends EntityServiceImpl<ApiAuth, ApiAuthDao> implements ApiAuthService {

    @Autowired(required = false)
    private EventBus eventBus;

    @Override
    public ApiAuth findByAccesskey(String accesskey) {
        return this.getEntityDao().findByAccesskey(accesskey);
    }

    @Override
    public void save(ApiAuth o) throws BusinessException {
        if (Strings.isBlank(o.getAuthNo())) {
            o.setAuthNo(Ids.did());
        }
        super.save(o);
    }

    @Override
    public void update(ApiAuth o) throws BusinessException {
        ApiAuth oldApiAuth = this.get(o.getId());
        if (Strings.isBlank(o.getAuthNo())) {
            o.setAuthNo(Ids.did());
        }
        super.update(o);
        eventBus.publish(new ApiAuthUpdateEvent(oldApiAuth));
    }


    @Override
    public void remove(ApiAuth o) throws BusinessException {
        super.remove(o);
        eventBus.publish(new ApiAuthUpdateEvent(o));
    }
}
