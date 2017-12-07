/*
 * www.acooly.cn Inc.
 * Copyright (c) 2017 All Rights Reserved
 */

/*
 * 修订记录:
 * zhangpu@acooly.cn 2017-12-08 02:55 创建
 */
package com.acooly.openapi.framework.serviceimpl.persistent;

import com.acooly.core.common.service.EntityServiceImpl;
import com.acooly.openapi.framework.domain.ApiMetaService;
import com.acooly.openapi.framework.service.ApiMetaServiceService;
import com.acooly.openapi.framework.serviceimpl.persistent.dao.ApiMetaServiceDao;
import org.springframework.stereotype.Service;

/**
 * @author zhangpu 2017-12-08 02:55
 */
@Service("apiMetaServiceService")
public class ApiMetaServiceServiceImpl extends EntityServiceImpl<ApiMetaService, ApiMetaServiceDao>
        implements ApiMetaServiceService {

    @Override
    public ApiMetaService findByServiceNameAndVersion(String serviceName, String version) {
        return getEntityDao().findByServiceNameAndVersion(serviceName, version);
    }

    @Override
    public ApiMetaService mergeSave(ApiMetaService apiMetaService) {

        ApiMetaService old = findByServiceNameAndVersion(apiMetaService.getServiceName(), apiMetaService.getVersion());
        if (old == null) {
            save(apiMetaService);
        } else {
            apiMetaService.setId(old.getId());
            update(apiMetaService);
        }
        return apiMetaService;
    }
}
