/*
 * acooly.com Inc.
 * Copyright (c) 2016 All Rights Reserved
 */

/*
 * 修订记录:
 * kuli@yiji.com 2016-11-10 01:25 创建
 */
package com.acooly.openapi.framework.facade.common;

import com.acooly.core.common.dao.support.PageInfo;
import com.acooly.core.common.facade.OrderBase;
import com.google.common.collect.Maps;

import java.util.Map;

/**
 * @author acooly
 */
public class QueryOrderBase<T> extends OrderBase {

    /**
     * 分页对象
     */
    private PageInfo<T> pageInfo = new PageInfo<>(1, 10);

    /**
     * 查询条件
     */
    private Map<String, Object> searchMap = Maps.newHashMap();

    /**
     * 排序条件
     */
    private Map<String, Boolean> sortMap;

    public PageInfo<T> getPageInfo() {
        return pageInfo;
    }

    public void setPageInfo(PageInfo<T> pageInfo) {
        this.pageInfo = pageInfo;
    }

    public Map<String, Object> getSearchMap() {
        return searchMap;
    }

    public void setSearchMap(Map<String, Object> searchMap) {
        this.searchMap = searchMap;
    }

    public Map<String, Boolean> getSortMap() {
        return sortMap;
    }

    public void setSortMap(Map<String, Boolean> sortMap) {
        this.sortMap = sortMap;
    }
}
