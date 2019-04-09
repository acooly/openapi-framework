package com.acooly.openapi.apidoc.generator.parser.impl;

import com.acooly.core.utils.Strings;
import com.acooly.openapi.apidoc.ApiDocProperties;
import com.acooly.openapi.apidoc.enums.SchemeTypeEnum;
import com.acooly.openapi.apidoc.generator.ApiDocModule;
import com.acooly.openapi.apidoc.generator.parser.ApiDocParser;
import com.acooly.openapi.apidoc.generator.parser.OpenApiDocParserSupport;
import com.acooly.openapi.apidoc.persist.entity.ApiDocScheme;
import com.acooly.openapi.apidoc.persist.entity.ApiDocSchemeService;
import com.acooly.openapi.apidoc.utils.ApiDocs;
import com.acooly.openapi.framework.common.annotation.ApiDocType;
import com.acooly.openapi.framework.service.domain.ApiMetaService;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * Apidoc的Scheme自动解析
 *
 * @author zhangpu
 * @date 2019-01-13 19:05
 */
@Slf4j
@Component
public class SchemeApiDocParserImpl extends OpenApiDocParserSupport implements ApiDocParser<List<ApiDocScheme>> {

    @Autowired
    private ApiDocProperties apiDocProperties;

    protected List<ApiDocScheme> doParseApiScheme() {
        Map<String, ApiDocScheme> apiDocSchemeMaps = Maps.newHashMap();
        try {
            List<ApiMetaService> apiMetaServices = doLoadOpenApiMetas();
            ApiDocType apiDocType = null;
            String serviceNo = null;
            ApiDocSchemeService apiDocSchemeService = null;
            for (ApiMetaService apiMetaService : apiMetaServices) {
                Class<?> serviceClass = Class.forName(apiMetaService.getServiceClass());
                serviceNo = ApiDocs.getServiceNo(apiMetaService.getServiceName(), apiMetaService.getVersion());
                apiDocType = serviceClass.getAnnotation(ApiDocType.class);
                if (apiDocType != null && Strings.isNoneBlank(apiDocType.code())) {
                    if (apiDocSchemeMaps.get(apiDocType.code()) == null) {
                        apiDocSchemeMaps.put(apiDocType.code(), new ApiDocScheme(apiDocType.code(), apiDocType.name()));
                    }
                    apiDocSchemeService = new ApiDocSchemeService(apiDocType.code(), serviceNo);
                    apiDocSchemeMaps.get(apiDocType.code()).append(apiDocSchemeService);
                }
            }

            // 开启默认scheme
            if (apiDocProperties.isDefaultSchemeEnable()) {
                ApiDocScheme defaultScheme = new ApiDocScheme(ApiDocProperties.DEF_SCHEME_NO, ApiDocProperties.DEF_SCHEME_TITLE);
                defaultScheme.setSchemeType(SchemeTypeEnum.common);
                List<ApiDocSchemeService> allApis = Lists.newArrayList();
                apiMetaServices.forEach(e -> {
                    allApis.add(new ApiDocSchemeService(defaultScheme.getSchemeNo(), e.getServiceNo()));
                });
                defaultScheme.setApiDocSchemeServices(allApis);
                apiDocSchemeMaps.put(defaultScheme.getSchemeNo(), defaultScheme);
            }

        } catch (Exception e) {
            throw new RuntimeException("解析ApiDocScheme失败: " + e.getMessage());
        }


        return Lists.newArrayList(apiDocSchemeMaps.values());
    }


    @Override
    public List<ApiDocScheme> parse() {
        return doParseApiScheme();
    }

    @Override
    public ApiDocModule getModule() {
        return ApiDocModule.scheme;
    }
}
