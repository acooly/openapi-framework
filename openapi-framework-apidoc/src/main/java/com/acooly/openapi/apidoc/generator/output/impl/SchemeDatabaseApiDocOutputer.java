package com.acooly.openapi.apidoc.generator.output.impl;

import com.acooly.core.utils.Collections3;
import com.acooly.openapi.apidoc.generator.ApiDocModule;
import com.acooly.openapi.apidoc.generator.output.ApiDocOutputer;
import com.acooly.openapi.apidoc.persist.entity.ApiDocScheme;
import com.acooly.openapi.apidoc.persist.entity.ApiDocSchemeService;
import com.acooly.openapi.apidoc.persist.entity.ApiDocService;
import com.acooly.openapi.apidoc.persist.service.ApiDocIntegrateService;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 文档方案模块的数据库输出实现
 * <p>
 * 实现基于代码注释的自动方案的合并和输出
 * 1、与现有的scheme方案合并。排序规则：orderTime desc,schemeCode asc
 * 2、scheme方案内的api文档每次重构。排序规则：orderTime desc,serviceNo asc
 *
 * @author zhangpu
 * @date 2019-02-08 17:28
 */
@Slf4j
@Component
public class SchemeDatabaseApiDocOutputer implements ApiDocOutputer<List<ApiDocScheme>> {

    @Autowired
    private ApiDocIntegrateService apiDocIntegrateService;

    @Override
    public void output(List<ApiDocScheme> apiDocSchemes) {
        // 合并方案scheme
        apiDocIntegrateService.mergeScheme(apiDocSchemes);

        // 合并方案下的API文档
        List<ApiDocService> apiDocServices = null;
        for (ApiDocScheme apiDocScheme : apiDocSchemes) {
            apiDocServices = Lists.newArrayList();
            if (Collections3.isNotEmpty(apiDocScheme.getApiDocSchemeServices())) {
                for (ApiDocSchemeService apiDocSchemeService : apiDocScheme.getApiDocSchemeServices()) {
                    apiDocServices.add(new ApiDocService(apiDocSchemeService.getServiceNo()));
                }
            }
            apiDocIntegrateService.distributeScheme(apiDocScheme, apiDocServices);
        }
    }

    @Override
    public ApiDocModule getModule() {
        return ApiDocModule.scheme;
    }


}
