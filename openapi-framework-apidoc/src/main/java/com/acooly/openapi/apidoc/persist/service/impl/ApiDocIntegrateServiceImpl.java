/*
 * www.acooly.cn Inc.
 * Copyright (c) 2018 All Rights Reserved
 */

/*
 * 修订记录:
 * zhangpu@acooly.cn 2018-01-13 10:00 创建
 */
package com.acooly.openapi.apidoc.persist.service.impl;

import com.acooly.core.utils.Collections3;
import com.acooly.openapi.apidoc.persist.entity.ApiDocScheme;
import com.acooly.openapi.apidoc.persist.entity.ApiDocService;
import com.acooly.openapi.apidoc.persist.service.ApiDocIntegrateService;
import com.acooly.openapi.apidoc.persist.service.ApiDocSchemeService;
import com.acooly.openapi.apidoc.persist.service.ApiDocSchemeServiceService;
import com.acooly.openapi.apidoc.persist.service.ApiDocServiceService;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.Serializable;
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
    private ApiDocSchemeService apiDocSchemeService;

    @Autowired
    private ApiDocSchemeServiceService apiDocSchemeServiceService;


    @Override
    public void merge(List<ApiDocService> apiDocServices) {
        apiDocServiceService.merge(apiDocServices);
    }


    @Override
    public void mergeScheme(List<ApiDocScheme> apiDocSchemes) {
        apiDocSchemeService.merge(apiDocSchemes);
    }

    @Override
    public void distributeSchemeToDefault(List<ApiDocService> apiDocServices) {
        ApiDocScheme apiDocScheme = apiDocSchemeService.createDefault();
        distributeScheme(apiDocScheme, apiDocServices);
    }

    @Override
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
                apiDocSchemeServiceService.removes(removeEntities.toArray(new Serializable[]{}));
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
            log.info("构建方案：{} [成功], 新增: {}， 删除冗余:{}", apiDocScheme.getTitle(), unsaveEntities.size(), removeEntities.size());
        } catch (Exception e) {
            log.warn("构建方案：{}  [失败]. scheme:{}. error:{}", apiDocScheme.getTitle(), e.getMessage());
            throw new RuntimeException("构建方案 失败", e);
        }
    }


}
