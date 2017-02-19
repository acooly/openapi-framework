/*
 * acooly.cn Inc.
 * Copyright (c) 2016 All Rights Reserved.
 * create by acooly
 * date:2016-07-16
 *
 */
package com.yiji.framework.openapi.service;

import com.acooly.core.common.service.EntityService;
import com.yiji.framework.openapi.domain.ApiService;

import java.util.List;
import java.util.Map;

/**
 * 服务分类 Service接口
 * <p>
 * Date: 2016-07-16 01:57:14
 *
 * @author acooly
 */
public interface ApiServiceService extends EntityService<ApiService> {


    Map<String, List<ApiService>> searchApiService(String key);


}
