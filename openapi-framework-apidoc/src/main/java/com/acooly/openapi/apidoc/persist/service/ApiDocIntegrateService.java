/*
 * www.acooly.cn Inc.
 * Copyright (c) 2018 All Rights Reserved
 */

/*
 * 修订记录:
 * zhangpu@acooly.cn 2018-01-13 09:59 创建
 */
package com.acooly.openapi.apidoc.persist.service;

import com.acooly.openapi.apidoc.persist.entity.ApiDocScheme;
import com.acooly.openapi.apidoc.persist.entity.ApiDocService;

import java.util.List;

/**
 * Apidoc整合服务
 *
 * @author zhangpu 2018-01-13 09:59
 */
public interface ApiDocIntegrateService {

    /**
     * 合并全局服务及级联信息
     * <p>
     * 1、级联信息包括：对应的报文信息和字段信息
     * 2、合并逻辑：有变动的更新；数据库无的插入；传入没有的数据库删除
     *
     * @param apiDocServices
     */
    void merge(List<ApiDocService> apiDocServices);


    /**
     * 分配所有数据到默认方案
     */
    void distributeDefaultScheme(List<ApiDocService> apiDocServices);


    /**
     * 分配方案对应的服务列表
     *
     * @param apiDocScheme
     * @param apiDocServices
     */
    void distributeScheme(ApiDocScheme apiDocScheme, List<ApiDocService> apiDocServices);


}
