/*
 * www.acooly.cn Inc.
 * Copyright (c) 2017 All Rights Reserved
 */

/*
 * 修订记录:
 * zhangpu@acooly.cn 2017-12-08 02:55 创建
 */
package com.acooly.openapi.apidoc.persist.service.impl;

import com.acooly.core.common.service.EntityServiceImpl;
import com.acooly.core.utils.Collections3;
import com.acooly.openapi.apidoc.persist.dao.ApiMetaServiceDao;
import com.acooly.openapi.apidoc.persist.service.ApiMetaServiceService;
import com.acooly.openapi.framework.domain.ApiMetaService;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author zhangpu 2017-12-08 02:55
 */
@Slf4j
@Service("apiMetaServiceService")
public class ApiMetaServiceServiceImpl extends EntityServiceImpl<ApiMetaService, ApiMetaServiceDao>
        implements ApiMetaServiceService {

    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Throwable.class)
    @Override
    public void merge(List<ApiMetaService> apiMetaServices) {
        if (Collections3.isEmpty(apiMetaServices)) {
            return;
        }
        doMergeSave(apiMetaServices);
        doMergeDelete(apiMetaServices);
    }


    @Override
    public ApiMetaService findByServiceNameAndVersion(String serviceName, String version) {
        return getEntityDao().findByServiceNameAndVersion(serviceName, version);
    }


    protected void doMergeSave(List<ApiMetaService> apiMetaServices) {
        ApiMetaService entity = null;
        for (ApiMetaService apiMetaService : apiMetaServices) {
            entity = findByServiceNameAndVersion(apiMetaService.getServiceName(), apiMetaService.getVersion());
            if (entity != null) {
                apiMetaService.setId(entity.getId());
                update(apiMetaService);
            } else {
                save(apiMetaService);
            }
        }
    }


    protected void doMergeDelete(List<ApiMetaService> apiMetaServices) {
        List<ApiMetaService> persists = getAll();
        if (Collections3.isEmpty(persists)) {
            return;
        }
        List<ApiMetaService> needRemoves = Lists.newArrayList();
        for (ApiMetaService persist : persists) {
            if (!apiMetaServices.contains(persist)) {
                needRemoves.add(persist);
                continue;
            }
        }
        if (needRemoves.size() > 0) {
            doDelete(needRemoves);
            log.info("Apidoc元数据合并删除：{}", needRemoves);
        }
    }


    protected void doDelete(List<ApiMetaService> apiMetaServices) {
        for (ApiMetaService apiMetaService : apiMetaServices) {
            getEntityDao().deleteByServiceNameAndVersion(apiMetaService.getServiceName(), apiMetaService.getVersion());
        }
    }


}
