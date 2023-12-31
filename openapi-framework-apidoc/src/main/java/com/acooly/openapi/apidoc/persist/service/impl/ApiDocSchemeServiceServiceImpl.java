/*
 * acooly.cn Inc.
 * Copyright (c) 2017 All Rights Reserved.
 * create by acooly
 * date:2017-12-05
 */
package com.acooly.openapi.apidoc.persist.service.impl;

import com.acooly.core.common.dao.support.PageInfo;
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
    public List<ApiDocService> searchApiDocServices(String schemeNo, String key) {
        if (Strings.isNotBlank(key)) {
            return getEntityDao().searchSchemeService("SYSTEM", key);
        } else {
            return findSchemeApiDocServices(schemeNo);
        }
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
        return this.getEntityDao().findContentServices(schemeNo);
    }

    @Override
    public PageInfo<ApiDocService> findContentServicesByKey(PageInfo<ApiDocService> pageInfo, String keywords) {
        int totalCount = this.getEntityDao().countServicesByKey(keywords);
        List<ApiDocService> contentServicesByKey = this.getEntityDao().findContentServicesByKey(keywords, (pageInfo.getCurrentPage() - 1) * pageInfo.getCountOfCurrentPage(), pageInfo.getCountOfCurrentPage());
        pageInfo.setTotalCount(totalCount);
        pageInfo.setPageResults(contentServicesByKey);
        pageInfo.setTotalPage(pageInfo.getTotalPage());
        return pageInfo;

    }

    @Override
    public void moveDown(Long id) {

        try {
            ApiDocSchemeService apiDocSchemeService = get(id);
            long current = apiDocSchemeService.getSortTime();
            ApiDocSchemeService beforeSchemeService = getEntityDao().findAlfterOne(current, apiDocSchemeService.getSchemeNo(), id);
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
            ApiDocSchemeService beforeSchemeService = getEntityDao().findBeforeOne(current, apiDocSchemeService.getSchemeNo(), id);
            if (beforeSchemeService == null) {
                return;
            }
            exchangeSortTime(apiDocSchemeService, current, beforeSchemeService);
        } catch (Exception e) {
            throw new BusinessException("操作失败", e);
        }
    }

    @Override
    public void moveTop(Long id) {
        try {
            ApiDocSchemeService apiDocSchemeService = get(id);
            ApiDocSchemeService topSchemeService = getEntityDao().findTopOne(apiDocSchemeService.getSchemeNo());
            //如果当前已经是第一条则不做任何处理
            if (apiDocSchemeService.getId().equals(topSchemeService.getId())) {
                return;
            }
            //sort_time减1
            apiDocSchemeService.setSortTime(topSchemeService.getSortTime() - 1);
            getEntityDao().update(apiDocSchemeService);
        } catch (Exception e) {
            throw new BusinessException("操作失败", e);
        }
    }

    @Override
    public void remove(Long id) {
        try {
            this.getEntityDao().removeById(id);
        } catch (Exception e) {
            throw new BusinessException("操作失败", e);
        }
    }

    private void exchangeSortTime(ApiDocSchemeService apiDocSchemeService, long current, ApiDocSchemeService targetSchemeService) {
        if (targetSchemeService == null) {
            return;
        }
        apiDocSchemeService.setSortTime(targetSchemeService.getSortTime());
        targetSchemeService.setSortTime(current);
        this.update(apiDocSchemeService);
        this.update(targetSchemeService);
    }
}
