/*
 * acooly.cn Inc.
 * Copyright (c) 2017 All Rights Reserved.
 * create by acooly
 * date:2017-12-05
 *
 */
package com.acooly.openapi.apidoc.persist.service;

import com.acooly.core.common.service.EntityService;
import com.acooly.openapi.apidoc.persist.entity.ApiDocService;

import java.util.List;

/**
 * 服务 Service接口
 * <p>
 * Date: 2017-12-05 12:34:39
 *
 * @author acooly
 */
public interface ApiDocServiceService extends EntityService<ApiDocService> {


    /**
     * 合并保存服务信息
     *
     * @param apiDocServices
     */
    void merge(List<ApiDocService> apiDocServices);

    ApiDocService loadApiDocService(Long id);

    ApiDocService loadApiDocServiceByNo(String serviceNo);

}
