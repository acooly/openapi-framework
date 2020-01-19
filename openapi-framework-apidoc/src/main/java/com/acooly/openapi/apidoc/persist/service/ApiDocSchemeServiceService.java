/*
 * acooly.cn Inc.
 * Copyright (c) 2017 All Rights Reserved.
 * create by acooly
 * date:2017-12-05
 *
 */
package com.acooly.openapi.apidoc.persist.service;

import com.acooly.core.common.service.EntityService;
import com.acooly.openapi.apidoc.persist.entity.ApiDocSchemeService;
import com.acooly.openapi.apidoc.persist.entity.ApiDocService;

import java.util.List;

/**
 * 方案服务列表 Service接口
 * <p>
 * Date: 2017-12-05 12:34:38
 *
 * @author acooly
 */
public interface ApiDocSchemeServiceService extends EntityService<ApiDocSchemeService> {


    List<ApiDocSchemeService> findSchemeServices(String schemeNo);

    List<ApiDocService> findSchemeApiDocServices(String schemeNo);

    void deleteSchemeService(String schemeNo, String serviceNo);

    /**
     * 传入产品的schemeNo，查询得到产品相关的service，返回的服务列表的schemeNo为在api列表分类中的schemeNo
     * @param schemeNo
     * @return
     */
    List<ApiDocService> findContentServices(String schemeNo);
}
