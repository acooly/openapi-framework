package com.acooly.openapi.apidoc.generator.parser;

import com.acooly.openapi.framework.service.domain.ApiMetaService;
import com.acooly.openapi.framework.service.service.ApiMetaServiceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * @author zhangpu
 * @date 2019-01-13 19:18
 */
@Slf4j
public class AbstractApiDocParser {

    @Autowired
    private ApiMetaServiceService apiMetaServiceService;

    /**
     * 获取所有的OpoenApi元数据，用于解析文档
     *
     * @return
     */
    protected List<ApiMetaService> doLoadOpenApiMetas() {
        return apiMetaServiceService.getAll();
    }
}
