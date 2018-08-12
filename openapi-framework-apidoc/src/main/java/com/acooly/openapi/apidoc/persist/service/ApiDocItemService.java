/*
 * acooly.cn Inc.
 * Copyright (c) 2017 All Rights Reserved.
 * create by acooly
 * date:2017-12-05
 *
 */
package com.acooly.openapi.apidoc.persist.service;

import com.acooly.core.common.service.EntityService;
import com.acooly.openapi.apidoc.persist.entity.ApiDocItem;

import java.util.List;

/**
 * 报文字段 Service接口
 * <p>
 * Date: 2017-12-05 12:34:39
 *
 * @author acooly
 */
public interface ApiDocItemService extends EntityService<ApiDocItem> {

    /**
     * 合并保存报文字段列表
     *
     * @param messageNo
     * @param apiDocItems
     */
    void merge(String messageNo, List<ApiDocItem> apiDocItems);


    /**
     * 根据报文编码查询字段
     *
     * @param messageNo
     * @return
     */
    List<ApiDocItem> loadByMessageNo(String messageNo);


    /**
     * 根据消息编码删除字段数据
     *
     * @param messageNo
     */
    void deleteByMessageNo(String messageNo);

}
