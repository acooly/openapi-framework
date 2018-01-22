/*
 * acooly.cn Inc.
 * Copyright (c) 2017 All Rights Reserved.
 * create by acooly
 * date:2017-12-05
 */
package com.acooly.openapi.apidoc.persist.service.impl;

import com.acooly.core.common.service.EntityServiceImpl;
import com.acooly.core.utils.Strings;
import com.acooly.openapi.apidoc.persist.dao.ApiDocServiceDao;
import com.acooly.openapi.apidoc.persist.entity.ApiDocService;
import com.acooly.openapi.apidoc.persist.service.ApiDocItemService;
import com.acooly.openapi.apidoc.persist.service.ApiDocMessageService;
import com.acooly.openapi.apidoc.persist.service.ApiDocServiceService;
import com.acooly.openapi.apidoc.utils.ApiDocs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 服务 Service实现
 * <p>
 * Date: 2017-12-05 12:34:39
 *
 * @author acooly
 */
@Service("apiDocServiceService")
public class ApiDocServiceServiceImpl extends EntityServiceImpl<ApiDocService, ApiDocServiceDao> implements ApiDocServiceService {

    @Autowired
    private ApiDocMessageService apiDocMessageService;

    @Autowired
    private ApiDocItemService apiDocItemService;

    @Override
    public void mergeSave(ApiDocService apiDocService) {

        String serviceNo = getServiceNo(apiDocService);
        ApiDocService entity = getEntityDao().findByServiceNo(serviceNo);
        // 暂时不处理已删除
        if (entity != null) {
            // update
            if (!ApiDocs.equelsApiDocService(entity, apiDocService)) {
                // 数据有变更
                apiDocService.setId(entity.getId());
                update(apiDocService);
            }
        } else {
            // insert
            save(apiDocService);
        }

    }


    protected String getServiceNo(ApiDocService entity) {
        String serviceNo = entity.getServiceNo();
        if (Strings.isBlank(serviceNo)) {
            serviceNo = ApiDocs.getServiceNo(entity.getName(), entity.getVersion());
        }
        return serviceNo;
    }

    @Override
    public ApiDocService findByServiceNo(String serviceNo) {
        return getEntityDao().findByServiceNo(serviceNo);
    }


    @Override
    public ApiDocService loadApiDocService(Long id) {
        ApiDocService apiDocService = get(id);
        if (apiDocService == null) {
            return null;
        }
        apiDocService.setApiDocMessages(apiDocMessageService.loadApiDocMessages(apiDocService.getServiceNo()));
        return apiDocService;
    }

    @Override
    public ApiDocService loadApiDocServiceByNo(String serviceNo) {
        ApiDocService apiDocService = getEntityDao().findByServiceNo(serviceNo);
        if (apiDocService == null) {
            return null;
        }
        apiDocService.setApiDocMessages(apiDocMessageService.loadApiDocMessages(apiDocService.getServiceNo()));
        return apiDocService;
    }
}
