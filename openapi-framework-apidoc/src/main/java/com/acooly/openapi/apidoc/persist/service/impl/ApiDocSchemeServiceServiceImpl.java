/*
 * acooly.cn Inc.
 * Copyright (c) 2017 All Rights Reserved.
 * create by acooly
 * date:2017-12-05
 */
package com.acooly.openapi.apidoc.persist.service.impl;

import com.acooly.core.common.exception.BusinessException;
import com.acooly.core.common.service.EntityServiceImpl;
import com.acooly.core.utils.Strings;
import com.acooly.openapi.apidoc.persist.dao.ApiDocSchemeServiceDao;
import com.acooly.openapi.apidoc.persist.entity.ApiDocSchemeService;
import com.acooly.openapi.apidoc.persist.entity.ApiDocService;
import com.acooly.openapi.apidoc.persist.service.ApiDocSchemeServiceService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 方案服务列表 Service实现
 * <p>
 * Date: 2017-12-05 12:34:39
 *
 * @author acooly
 */
@Service("apiDocSchemeServiceService")
public class ApiDocSchemeServiceServiceImpl extends EntityServiceImpl<ApiDocSchemeService, ApiDocSchemeServiceDao> implements ApiDocSchemeServiceService {

    @Resource
    private ApiDocSchemeServiceDao apiDocSchemeServiceDao;

    @Override
    public List<ApiDocSchemeService> findSchemeServices(String schemeNo) {
        return getEntityDao().findBySchemeNo(schemeNo);
    }

    @Override
    public List<ApiDocService> findSchemeApiDocServices(String schemeNo) {
        if (Strings.isNotBlank(schemeNo)) {
            return getEntityDao().findSchemeService(schemeNo);
        }
        return getEntityDao().findAllSchemeService();
    }

    @Override
    public void deleteSchemeService(String schemeNo, String serviceNo) {
        List<ApiDocSchemeService> apiSchemeServiceDocList = apiDocSchemeServiceDao.findSchemeServicesBySchemeIdAndServiceNo(schemeNo, serviceNo);
        for (ApiDocSchemeService apiSchemeServiceDoc : apiSchemeServiceDocList) {
            apiDocSchemeServiceDao.removeById(apiSchemeServiceDoc.getId());
        }
    }

    @Override
    public List<ApiDocService> findContentServices(String schemeNo) {
        return this.findContentServices(schemeNo, null);
    }

    @Override
    public List<ApiDocService> findContentServices(String schemeNo, String keywords) {
        return this.getEntityDao().findContentServices(schemeNo, keywords);
    }

    @Override
    public void moveDown(Long id) {

        try {
            ApiDocSchemeService apiDocSchemeService = get(id);
            long current = apiDocSchemeService.getSortTime();
            ApiDocSchemeService beforeSchemeService = getEntityDao().findBeforeOne(current, apiDocSchemeService.getSchemeNo(), id);
            exchangeSortTime(apiDocSchemeService, current, beforeSchemeService);
        } catch (Exception e) {
            throw new BusinessException("下移失败", e);
        }
    }
    @Override
    public void moveUp(Long id) {
        try {
            ApiDocSchemeService apiDocSchemeService = get(id);
            long current = apiDocSchemeService.getSortTime();
            ApiDocSchemeService beforeSchemeService = getEntityDao().findAlfterOne(current, apiDocSchemeService.getSchemeNo(), id);
            if (beforeSchemeService == null) {
                return;
            }
            exchangeSortTime(apiDocSchemeService, current, beforeSchemeService);
        } catch (Exception e) {
            throw new BusinessException("操作失败", e);
        }
    }

    private void exchangeSortTime(ApiDocSchemeService apiDocSchemeService, long current, ApiDocSchemeService beforeSchemeService) {
        apiDocSchemeService.setSortTime(beforeSchemeService.getSortTime());
        beforeSchemeService.setSortTime(current);
        this.update(apiDocSchemeService);
        this.update(beforeSchemeService);
    }
}
