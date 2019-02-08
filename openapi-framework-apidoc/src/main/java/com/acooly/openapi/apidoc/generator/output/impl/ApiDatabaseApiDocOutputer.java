/*
 * www.yiji.com Inc.
 * Copyright (c) 2015 All Rights Reserved.
 */

package com.acooly.openapi.apidoc.generator.output.impl;

import com.acooly.openapi.apidoc.generator.ApiDocModule;
import com.acooly.openapi.apidoc.generator.output.ApiDocOutputer;
import com.acooly.openapi.apidoc.generator.output.ApiOutputerTypeEnum;
import com.acooly.openapi.apidoc.persist.entity.ApiDocService;
import com.acooly.openapi.apidoc.persist.service.ApiDocIntegrateService;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

/**
 * 数据库输出实现
 * <p/>
 *
 * @author zhangpu
 * @date 2015/2/26 create
 * @date 2019/2/8 refactoring
 */
@Component("apiDatabaseApiDocOutputer")
public class ApiDatabaseApiDocOutputer implements ApiDocOutputer<List<ApiDocService>> {

    @Resource
    private ApiDocIntegrateService apiDocIntegrateService;

    @Override
    public void output(List<ApiDocService> apiServiceDocs) {
        try {
            apiDocIntegrateService.merge(apiServiceDocs);
//            apiDocIntegrateService.distributeSchemeToDefault(apiServiceDocs);
        } catch (Exception e) {
            throw new RuntimeException("输出:" + getModule() + "到" + getType() + "失败。原因：" + e.getMessage());
        }
    }

    @Override
    public ApiOutputerTypeEnum getType() {
        return ApiOutputerTypeEnum.database;
    }

    @Override
    public ApiDocModule getModule() {
        return ApiDocModule.api;
    }
}
