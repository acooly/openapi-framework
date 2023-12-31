/*
 * acooly.cn Inc.
 * Copyright (c) 2017 All Rights Reserved.
 * create by acooly
 * date:2017-12-05
 *
 */
package com.acooly.openapi.apidoc.persist.service;

import com.acooly.core.common.dao.support.PageInfo;
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

    List<ApiDocService> searchApiDocServices(String schemeNo, String key);

    List<ApiDocService> findSchemeApiDocServices(String schemeNo);

    void deleteSchemeService(String schemeNo, String serviceNo);

    /**
     * 传入产品的schemeNo，查询得到产品相关的service，返回的服务列表的schemeNo为在api列表分类中的schemeNo
     *
     * @param schemeNo
     * @return
     */
    List<ApiDocService> findContentServices(String schemeNo);

    /**
     * 关键词查询查询服务
     *
     * @param keywords
     * @return
     */
    PageInfo<ApiDocService> findContentServicesByKey(PageInfo<ApiDocService> pageInfo, String keywords);

    /**
     * 下移
     *
     * @param id
     */
    void moveDown(Long id);

    /**
     * 下移
     *
     * @param id
     */
    void moveUp(Long id);

    /**
     * 置顶
     *
     * @param id
     */
    void moveTop(Long id);

    /**
     * 删除关联关系
     *
     * @param id
     */
    void remove(Long id);
}
