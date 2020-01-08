/*
 * acooly.cn Inc.
 * Copyright (c) 2017 All Rights Reserved.
 * create by acooly
 * date:2017-12-05
 *
 */
package com.acooly.openapi.apidoc.persist.service;

import com.acooly.core.common.service.EntityService;
import com.acooly.openapi.apidoc.enums.DocStatusEnum;
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
     * 按方案类型查询
     *
     * @param schemeType
     * @return
     */
    List<ApiDocScheme> findBySchemeTypeAndCategory(String schemeType,String category);

    /**
     * 合并
     *
     * @param apiDocSchemes
     */
    void merge(List<ApiDocScheme> apiDocSchemes);

    JSONObject getSelectSchemeList(String schemeNo);

    /**
     * 置顶
     * @param id
     */
    void moveTop(Long id);

    /**
     * 上移
     * @param id
     */
    void moveUp(Long id);

    /**
     * 树形结构
     * 默认分类为：api
     *
     * @param rootPath 根节点Path（为空，则默认：/）
     * @param status
     * @return
     */
    List<ApiDocScheme> tree(String rootPath, DocStatusEnum status);

    /**
     * 树形结构
     * 默认分类为：api
     *
     * @param rootId 根节点Id（为空，则默认：0）
     * @param status
     * @return
     */
    List<ApiDocScheme> tree(Long rootId, DocStatusEnum status);

    /**
     * 树形结构
     *
     * @param category    分类
     * @param rootPath 根节点Path（为空，则默认：/）
     * @param status
     * @return
     */
    List<ApiDocScheme> tree(String category, String rootPath, DocStatusEnum status);

    /**
     * 树形结构
     *
     * @param category  主题（为空，则默认：api）
     * @param rootId 根节点Id（为空，则默认：0）
     * @param status
     * @return
     */
    List<ApiDocScheme> tree(String category, Long rootId, DocStatusEnum status);

    /**
     * 单层查询
     * （默认分类：api）
     *
     * @param parentId
     * @param status
     * @return
     */
    List<ApiDocScheme> level(Long parentId, DocStatusEnum status);

    /**
     * 单层查询
     *
     * @param parentId 为空：顶层（parentId=TOP_PARENT_ID）
     * @param category    为空则：默认分类（category=DEFAULT_THEME）
     * @param status
     * @return
     */
    List<ApiDocScheme> level(Long parentId, String category, DocStatusEnum status);

    /**
     * 移动节点
     * top-移动至targetId对应层级，排序为targetId减1
     * bottom-移动至targetId对应层级，排序为targetId加1
     * append-移动至targetId父节点下，排序为现有时间
     *
     * @param sourceId 源数据id
     * @param targetId 目标数据id
     * @param point
     */
    void move(String sourceId, String targetId, String point);

    /**
     * 根据schemeNo查询方案
     * @param schemeNo
     * @return
     */
    ApiDocScheme findBySchemeNo(String schemeNo);
}
