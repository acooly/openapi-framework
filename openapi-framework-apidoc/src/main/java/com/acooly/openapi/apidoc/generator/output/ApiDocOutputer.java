/*
 * www.yiji.com Inc.
 * Copyright (c) 2015 All Rights Reserved.
 */

package com.acooly.openapi.apidoc.generator.output;

import com.acooly.openapi.apidoc.ApiDocContext;
import com.acooly.openapi.apidoc.persist.entity.ApiDocService;

import java.util.List;

/**
 * API文档输出接口 Created by zhangpu on 2015/1/27.
 */
public interface ApiDocOutputer<T> {

    T output(List<ApiDocService> apiServiceDocs, ApiDocContext apidocContext);

    ApiOutputerTypeEnum getType();

}
