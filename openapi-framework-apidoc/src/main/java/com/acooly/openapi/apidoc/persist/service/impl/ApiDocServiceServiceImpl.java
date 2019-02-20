/*
 * acooly.cn Inc.
 * Copyright (c) 2017 All Rights Reserved.
 * create by acooly
 * date:2017-12-05
 */
package com.acooly.openapi.apidoc.persist.service.impl;

import com.acooly.core.common.service.EntityServiceImpl;
import com.acooly.core.utils.Collections3;
import com.acooly.core.utils.Strings;
import com.acooly.openapi.apidoc.persist.dao.ApiDocServiceDao;
import com.acooly.openapi.apidoc.persist.dto.MergeResult;
import com.acooly.openapi.apidoc.persist.entity.ApiDocService;
import com.acooly.openapi.apidoc.persist.enums.ApiDocMergeType;
import com.acooly.openapi.apidoc.persist.service.ApiDocMessageService;
import com.acooly.openapi.apidoc.persist.service.ApiDocServiceService;
import com.acooly.openapi.apidoc.utils.ApiDocs;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 服务 Service实现
 * <p>
 * Date: 2017-12-05 12:34:39
 *
 * @author acooly
 */
@Slf4j
@Service("apiDocServiceService")
public class ApiDocServiceServiceImpl extends EntityServiceImpl<ApiDocService, ApiDocServiceDao> implements ApiDocServiceService {

    @Autowired
    private ApiDocMessageService apiDocMessageService;

    @Override
    public void merge(List<ApiDocService> apiDocServices) {
        if (Collections3.isEmpty(apiDocServices)) {
            return;
        }
        List<MergeResult> mergeResults = Lists.newArrayList();
        mergeResults.addAll(doMergeDelete(apiDocServices));
        mergeResults.addAll(doMergeSave(apiDocServices));
        if (Collections3.isNotEmpty(mergeResults)) {
            log.info("Service合并 [成功] size: {}", mergeResults.size());
            for (MergeResult result : mergeResults) {
                log.info("Service合并 {}", result.toString());
            }
        }


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


    protected List<MergeResult> doMergeSave(List<ApiDocService> apiDocServices) {
        List<MergeResult> mergeResults = Lists.newArrayList();
        ApiDocService entity = null;
        String serviceNo = null;
        for (ApiDocService apiDocService : apiDocServices) {
            serviceNo = apiDocService.getServiceNo();
            entity = getEntityDao().findByServiceNo(serviceNo);
            if (entity != null) {
                // update
                if (!ApiDocs.equelsApiDocService(entity, apiDocService)) {
                    // 数据有变更
                    apiDocService.setId(entity.getId());
                    update(apiDocService);
                    mergeResults.add(MergeResult.serviceOf(serviceNo, ApiDocMergeType.UPDATE));
                }
            } else {
                // insert
                save(apiDocService);
                mergeResults.add(MergeResult.serviceOf(serviceNo, ApiDocMergeType.CREATE));
            }
            apiDocMessageService.merge(serviceNo, apiDocService.getApiDocMessages());
        }
        return mergeResults;
    }


    protected List<MergeResult> doMergeDelete(List<ApiDocService> apiDocServices) {
        List<MergeResult> mergeResults = Lists.newArrayList();
        List<ApiDocService> persists = getAll();
        if (Collections3.isEmpty(persists)) {
            return mergeResults;
        }
        List<ApiDocService> needRemoves = Lists.newArrayList();
        for (ApiDocService persist : persists) {
            if (!apiDocServices.contains(persist)) {
                needRemoves.add(persist);
                continue;
            }
        }
        if (needRemoves.size() > 0) {
            mergeResults.addAll(doCascadeDelete(needRemoves));
        }
        return mergeResults;
    }


    protected List<MergeResult> doCascadeDelete(List<ApiDocService> apiDocServices) {
        List<MergeResult> mergeResults = Lists.newArrayList();
        for (ApiDocService apiDocService : apiDocServices) {
            getEntityDao().deleteByServiceNo(apiDocService.getServiceNo());
            apiDocMessageService.cascadeDelete(apiDocService.getServiceNo());
            mergeResults.add(MergeResult.serviceOf(apiDocService.getServiceNo(), ApiDocMergeType.DELETE));
        }
        return mergeResults;
    }


    protected String getServiceNo(ApiDocService entity) {
        String serviceNo = entity.getServiceNo();
        if (Strings.isBlank(serviceNo)) {
            serviceNo = ApiDocs.getServiceNo(entity.getName(), entity.getVersion());
        }
        return serviceNo;
    }

    @Override
    public List<ApiDocService> findServicesBySchemeNo(String schemeNo) {
        return this.getEntityDao().findServicesBySchemeNo(schemeNo);
    }
}
