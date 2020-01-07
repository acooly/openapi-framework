/*
 * acooly.cn Inc.
 * Copyright (c) 2017 All Rights Reserved.
 * create by acooly
 * date:2017-12-05
 */
package com.acooly.openapi.apidoc.persist.service.impl;

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
}
