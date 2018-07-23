/*
 * www.acooly.cn Inc.
 * Copyright (c) 2018 All Rights Reserved
 */

/*
 * 修订记录:
 * zhangpu@acooly.cn 2018-01-13 10:00 创建
 */
package com.acooly.openapi.apidoc.persist.service.impl;

import com.acooly.core.common.exception.BusinessException;
import com.acooly.core.utils.Collections3;
import com.acooly.openapi.apidoc.persist.entity.ApiDocMessage;
import com.acooly.openapi.apidoc.persist.entity.ApiDocScheme;
import com.acooly.openapi.apidoc.persist.entity.ApiDocService;
import com.acooly.openapi.apidoc.persist.service.*;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author zhangpu 2018-01-13 10:00
 */
@Slf4j
@Component
public class ApiDocIntegrateServiceImpl implements ApiDocIntegrateService {

    @Autowired
    private ApiDocServiceService apiDocServiceService;

    @Autowired
    private ApiDocMessageService apiDocMessageService;

    @Autowired
    private ApiDocItemService apiDocItemService;

    @Autowired
    private ApiDocSchemeService apiDocSchemeService;

    @Autowired
    private ApiDocSchemeServiceService apiDocSchemeServiceService;


    @Override
    public void merge(List<ApiDocService> apiDocServices) {
        List<ApiDocService> persists = apiDocServiceService.getAll();
        List<Long> needRemoves = Lists.newArrayList();
        for (ApiDocService persist : persists) {
            // 生成没有，数据库存在，合并删除
            if (!apiDocServices.contains(persist)) {
                needRemoves.add(persist.getId());
                continue;
            }
        }
        log.info("合并删除的服务：{}", needRemoves.toString());
        for (ApiDocService entity : apiDocServices) {
            mergeOne(entity);
        }

        // do remove
    }


    @Override
    public void distributeDefaultScheme(List<ApiDocService> apiDocServices) {
        ApiDocScheme apiDocScheme = apiDocSchemeService.createDefault();
        distributeScheme(apiDocScheme, apiDocServices);
    }

    @Override
    @Transactional
    public void distributeScheme(ApiDocScheme apiDocScheme, List<ApiDocService> apiDocServices) {

        try {
            List<com.acooly.openapi.apidoc.persist.entity.ApiDocSchemeService> schemeServices =
                    apiDocSchemeServiceService.findSchemeServices(apiDocScheme.getSchemeNo());
            if (schemeServices == null) {
                schemeServices = Lists.newArrayList();
            }

            // 合并处理：删除数据库有，新生成无的关系
            List<Long> removeEntities = Lists.newArrayList();
            for (com.acooly.openapi.apidoc.persist.entity.ApiDocSchemeService schemeService : schemeServices) {
                if (!apiDocServices.contains(new ApiDocService(schemeService.getServiceNo()))) {
                    removeEntities.add(schemeService.getId());
                }
            }

            if (Collections3.isNotEmpty(removeEntities)) {
                apiDocSchemeServiceService.removes(removeEntities.toArray(new Long[]{}));
            }

            // 合并处理：保存新增
            List<com.acooly.openapi.apidoc.persist.entity.ApiDocSchemeService> unsaveEntities = Lists.newArrayList();
            com.acooly.openapi.apidoc.persist.entity.ApiDocSchemeService apiDocSchemeService = null;
            for (ApiDocService apiDocService : apiDocServices) {
                apiDocSchemeService = new com.acooly.openapi.apidoc.persist.entity.ApiDocSchemeService
                        (apiDocScheme.getSchemeNo(), apiDocService.getServiceNo());
                if (!schemeServices.contains(apiDocSchemeService)) {
                    unsaveEntities.add(apiDocSchemeService);
                }
            }
            if (Collections3.isNotEmpty(unsaveEntities)) {
                apiDocSchemeServiceService.saves(unsaveEntities);
            }
            log.info("合并构建方案服务列表 成功。新增: {}， 删除冗余:{}", unsaveEntities.size(), removeEntities.size());
        } catch (Exception e) {
            log.warn("合并构建方案服务列表 失败. scheme:{}. error:{}", apiDocScheme, e.getMessage());
            throw new RuntimeException("合并构建方案服务列表 失败", e);
        }
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
    public void mergeOne(ApiDocService apiDocService) {
        try {
            apiDocServiceService.mergeSave(apiDocService);

            List<ApiDocMessage> apiDocMessages = apiDocService.getApiDocMessages();
            if (Collections3.isEmpty(apiDocMessages)) {
                return;
            }

            for (ApiDocMessage apiDocMessage : apiDocMessages) {
                apiDocMessageService.mergeSave(apiDocMessage);

                if (Collections3.isEmpty(apiDocMessage.getApiDocItems())) {
                    continue;
                }

                apiDocItemService.mergeSaves(apiDocMessage.getApiDocItems());
            }
        } catch (Exception e) {
            throw new BusinessException("合并Apidoc失败：" + apiDocService.getServiceNo(), e);
        }
    }
}
