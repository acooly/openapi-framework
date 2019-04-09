/*
 * acooly.cn Inc.
 * Copyright (c) 2019 All Rights Reserved.
 * create by zhike
 * date:2019-02-20
 *
 */
package com.acooly.openapi.apidoc.persist.service;

import com.acooly.core.common.service.EntityService;
import com.acooly.openapi.apidoc.persist.entity.ApiQaClassify;

import java.util.List;

/**
 * 问题分类表 Service接口
 *
 * Date: 2019-02-20 17:09:59
 * @author zhike
 *
 */
public interface ApiQaClassifyService extends EntityService<ApiQaClassify> {

    ApiQaClassify create(Long parentId, String name, String comments);

    List<ApiQaClassify> loadTree(Long parentId);

    List<ApiQaClassify> loadTopTypes();
}
