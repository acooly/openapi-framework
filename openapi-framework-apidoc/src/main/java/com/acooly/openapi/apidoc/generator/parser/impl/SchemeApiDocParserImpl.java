package com.acooly.openapi.apidoc.generator.parser.impl;

import com.acooly.core.utils.Collections3;
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
import com.google.common.collect.Sets;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Set;

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
            Map<String, List<ApiMetaService>> schemeServices = Maps.newHashMap();
            Set<String> errorCodes = Sets.newHashSet();
            ApiDocType apiDocType = null;
            String serviceNo = null;
            for (ApiMetaService apiMetaService : apiMetaServices) {
                Class<?> serviceClass = Class.forName(apiMetaService.getServiceClass());
                serviceNo = ApiDocs.getServiceNo(apiMetaService.getServiceName(), apiMetaService.getVersion());
                apiDocType = serviceClass.getAnnotation(ApiDocType.class);
                if (apiDocType != null && Strings.isNotBlank(apiDocType.code())) {
                    // 对注解生成的服务根据编码进行分组
                    doGroupServices(apiMetaService, apiDocType, schemeServices);
                    // 将服务方案放入map
                    doSchemeMap(apiDocSchemeMaps, apiDocType, serviceNo, errorCodes);
                }
            }
            if (Collections3.isNotEmpty(errorCodes)) {
                for (String errorCode : errorCodes) {
                    log.error("ApiDocType定义错误，code={}", errorCode);
                    log.error("包含错误的api列表为:");
                    for (ApiMetaService apiMetaService : schemeServices.get(errorCode)) {
                        log.error("serviceName={},serviceDesc={}", apiMetaService.getServiceName(), apiMetaService.getServiceDesc());
                    }
                }
                throw new RuntimeException("ApiDocType定义不符合规范,code与parentCode在不同服务定义不一致");
            }

            // 开启默认scheme
            if (apiDocProperties.isDefaultSchemeEnable()) {
                ApiDocScheme defaultScheme = new ApiDocScheme(ApiDocProperties.DEF_SCHEME_NO, ApiDocProperties.DEF_SCHEME_TITLE, null);
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

    private void doSchemeMap(Map<String, ApiDocScheme> apiDocSchemeMaps, ApiDocType apiDocType, String serviceNo, Set<String> errorCodes) {
        String parentCode = null;
        if (Strings.isNotBlank(apiDocType.parentCode())) {
            parentCode = apiDocType.parentCode();
        }
        ApiDocScheme apiDocScheme = apiDocSchemeMaps.get(apiDocType.code());
        if (apiDocScheme == null) {
            // 为空则创建新服务方案
            apiDocSchemeMaps.put(apiDocType.code(), new ApiDocScheme(apiDocType.code(), apiDocType.name(), parentCode));
        } else if (!Strings.equals(parentCode, apiDocScheme.getParentSchemeNo())) {
            // 不为空需检测是否和已有服务方案父级编码相同
            errorCodes.add(apiDocType.code());
        }
        if (Strings.isNotBlank(parentCode)) {
            ApiDocScheme parentScheme = apiDocSchemeMaps.get(parentCode);
            if (parentScheme == null) {
                parentScheme = new ApiDocScheme(parentCode, apiDocType.parentName(), null);
                parentScheme.setSubCount(1);
                apiDocSchemeMaps.put(parentCode, parentScheme);
            } else if (apiDocScheme == null) {
                // 当前方案在schemeMap中不存在时，才增加父节点的子节点数量
                parentScheme.setSubCount(parentScheme.getSubCount() + 1);
                apiDocSchemeMaps.put(parentCode, parentScheme);
            }

        }
        ApiDocSchemeService apiDocSchemeService = new ApiDocSchemeService(apiDocType.code(), serviceNo);
        apiDocSchemeMaps.get(apiDocType.code()).append(apiDocSchemeService);
    }

    private void doGroupServices(ApiMetaService apiMetaService, ApiDocType apiDocType, Map<String, List<ApiMetaService>> schemeServices) {
        if (schemeServices.get(apiDocType.code()) == null) {
            List<ApiMetaService> list = Lists.newArrayList();
            list.add(apiMetaService);
            schemeServices.put(apiDocType.code(), list);
        } else {
            schemeServices.get(apiDocType.code()).add(apiMetaService);
        }
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
