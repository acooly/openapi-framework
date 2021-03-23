/*
 * acooly.cn Inc.
 * Copyright (c) 2018 All Rights Reserved.
 * create by qiubo
 * date:2018-08-21
 */
package com.acooly.openapi.framework.service.service.impl;

import com.acooly.core.common.service.EntityServiceImpl;
import com.acooly.openapi.framework.service.dao.ApiPartnerDao;
import com.acooly.openapi.framework.service.domain.ApiPartner;
import com.acooly.openapi.framework.service.service.ApiPartnerService;
import org.springframework.stereotype.Service;

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

    @Override
    public ApiPartner getPartner(String partnerId) {
        return getEntityDao().findUniqu("EQ_partnerId", partnerId);
    }
}
