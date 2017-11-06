/*
 * acooly.cn Inc.
 * Copyright (c) 2016 All Rights Reserved.
 * create by acooly
 * date:2016-07-16
 *
 */
package com.acooly.openapi.framework.service;

import com.acooly.core.common.service.EntityService;
import com.acooly.openapi.framework.domain.ApiServiceType;

import java.util.List;

/**
 * 服务分类 Service接口
 *
 * <p>Date: 2016-07-16 01:57:05
 *
 * @author acooly
 */
public interface ApiServiceTypeService extends EntityService<ApiServiceType> {

  ApiServiceType create(Long parentId, String name, String comments);

  List<ApiServiceType> loadTree(Long parentId);

  List<ApiServiceType> loadTopTypes();
}
