/*
 * acooly.cn Inc.
 * Copyright (c) 2017 All Rights Reserved.
 * create by acooly
 * date:2017-12-05
 *
 */
package com.acooly.openapi.apidoc.persist.service;

import com.acooly.core.common.service.EntityService;
import com.acooly.openapi.apidoc.enums.SchemeTypeEnum;
import com.acooly.openapi.apidoc.persist.entity.ApiDocScheme;
import com.alibaba.fastjson.JSONObject;

import java.util.List;

/**
 * 服务方案 Service接口
 * <p>
 * Date: 2017-12-05 12:34:38
 *
 * @author acooly
 */
public interface ApiDocSchemeService extends EntityService<ApiDocScheme> {

    /**
     * 创建默认scheme
     *
     * @return
     */
    ApiDocScheme createDefault();

    /**
     * 按方案类型查询
     *
     * @param schemeType
     * @return
     */
    List<ApiDocScheme> findBySchemeType(SchemeTypeEnum schemeType);


    /**
     * 合并
     *
     * @param apiDocSchemes
     */
    void merge(List<ApiDocScheme> apiDocSchemes);

    JSONObject getSelectSchemeList(String schemeNo);

}
