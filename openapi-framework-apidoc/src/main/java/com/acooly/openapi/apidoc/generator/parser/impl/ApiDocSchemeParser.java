package com.acooly.openapi.apidoc.generator.parser.impl;

import com.acooly.openapi.apidoc.generator.parser.AbstractApiDocParser;
import com.acooly.openapi.apidoc.persist.entity.ApiDocScheme;
import com.acooly.openapi.framework.common.annotation.ApiDocType;
import com.acooly.openapi.framework.service.domain.ApiMetaService;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

/**
 * @author zhangpu
 * @date 2019-01-13 19:05
 */
@Slf4j
public class ApiDocSchemeParser extends AbstractApiDocParser {


    protected Map<ApiDocScheme, ApiMetaService> doParseApiScheme(ApiMetaService apiMetaService) {
        try {
            Class<?> serviceClass = Class.forName(apiMetaService.getServiceClass());
            ApiDocType apiDocType = serviceClass.getAnnotation(ApiDocType.class);
            if (apiDocType == null) {

            }
        } catch (Exception e) {
            throw new RuntimeException();
        }
        return null;
    }

}
