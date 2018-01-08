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
     * @param apiDocService
     */
    void mergeSave(ApiDocService apiDocService);


    /**
     * 合并全局服务及级联信息
     * <p>
     * 1、级联信息包括：对应的报文信息和字段信息
     * 2、合并逻辑：有变动的更新；数据库无的插入；传入没有的数据库删除
     *
     * @param apiDocServices
     */
    void merge(List<ApiDocService> apiDocServices);


}
