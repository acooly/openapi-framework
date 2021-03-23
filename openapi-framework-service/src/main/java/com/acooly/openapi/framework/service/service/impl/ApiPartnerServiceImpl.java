/*
 * acooly.cn Inc.
 * Copyright (c) 2018 All Rights Reserved.
 * create by qiubo
 * date:2018-08-21
 */
package com.acooly.openapi.framework.service.service.impl;

import com.acooly.core.common.exception.BusinessException;
import com.acooly.core.common.exception.CommonErrorCodes;
import com.acooly.core.common.service.EntityServiceImpl;
import com.acooly.core.utils.Collections3;
import com.acooly.module.event.EventBus;
import com.acooly.openapi.framework.service.dao.ApiPartnerDao;
import com.acooly.openapi.framework.service.domain.ApiPartner;
import com.acooly.openapi.framework.service.event.ApiUpdateEvent;
import com.acooly.openapi.framework.service.service.ApiAuthService;
import com.acooly.openapi.framework.service.service.ApiPartnerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;

/**
 * 租户管理 Service实现
 * <p>
 * Date: 2018-08-21 14:31:06
 *
 * @author qiubo
 * @author zhangpu
 */
@Service
public class ApiPartnerServiceImpl extends EntityServiceImpl<ApiPartner, ApiPartnerDao> implements ApiPartnerService {

    @Autowired
    private ApiAuthService apiAuthService;

    @Autowired(required = false)
    private EventBus eventBus;

    @Override
    public ApiPartner getPartner(String partnerId) {
        return getEntityDao().findUniqu("EQ_partnerId", partnerId);
    }

    @Override
    public void removeById(Serializable id) throws BusinessException {
        ApiPartner apiPartner = get(id);
        if (Collections3.isNotEmpty(apiAuthService.findByPartnerId(apiPartner.getPartnerId()))) {
            throw new BusinessException(CommonErrorCodes.UNSUPPORTED_ERROR, "接入方下还存在认证对象，不能直接删除");
        }
        super.removeById(id);
        publish(apiPartner);
    }

    @Override
    public void update(ApiPartner o) throws BusinessException {
        super.update(o);
        publish(o);
    }

    protected void publish(ApiPartner o) {
        eventBus.publish(new ApiUpdateEvent(o));
    }
}
