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
     * @param apiDocItems
     */
    void mergeSaves(List<ApiDocItem> apiDocItems);


}
