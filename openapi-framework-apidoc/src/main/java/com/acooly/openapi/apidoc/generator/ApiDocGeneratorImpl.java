/*
 * www.yiji.com Inc.
 * Copyright (c) 2015 All Rights Reserved.
 */

package com.acooly.openapi.apidoc.generator;

import com.acooly.core.utils.Collections3;
import com.acooly.openapi.apidoc.ApiDocContext;
import com.acooly.openapi.apidoc.ApiDocProperties;
import com.acooly.openapi.apidoc.generator.output.ApiDocOutputer;
import com.acooly.openapi.apidoc.generator.output.ApiDocOutputerFactory;
import com.acooly.openapi.apidoc.generator.output.ApiOutputerTypeEnum;
import com.acooly.openapi.apidoc.generator.parser.ApiDocParser;
import com.acooly.openapi.apidoc.persist.entity.ApiDocService;
import com.acooly.openapi.apidoc.persist.service.ApiDocSchemeService;
import com.acooly.openapi.apidoc.persist.service.ApiDocSchemeServiceService;
import com.acooly.openapi.apidoc.persist.service.ApiDocServiceService;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Apidoc生成器实现
 * @author zhangpu on 2015/1/29.
 */
@Service("apiDocBuilder")
@Slf4j
public class ApiDocGeneratorImpl implements ApiDocGenerator {

    private static final Logger logger = LoggerFactory.getLogger(ApiDocGeneratorImpl.class);
    List<String> packageList = Lists.newArrayList();

    @Autowired
    private ApiDocParser apiDocParser;

    @Autowired
    private ApiDocOutputerFactory apiDocOutputerFactory;

    @Autowired
    private ApiDocServiceService apiDocServiceService;

    @Autowired
    private ApiDocSchemeServiceService apiDocSchemeServiceService;

    @Autowired
    private ApiDocSchemeService apiDocSchemeService;

    @Autowired
    private ApiDocProperties apiDocProperties;

    @Override
    public void build() {
        List<ApiDocService> docs = doParse();

        List<String> outputerTypes =  apiDocProperties.getOutputTypes();
        if(Collections3.isEmpty(outputerTypes)){
            outputerTypes = Lists.newArrayList(ApiOutputerTypeEnum.console.code());
        }
        ApiOutputerTypeEnum apiOutputerTypeEnum = null;
        for(String outputTypeCode : outputerTypes){
            apiOutputerTypeEnum = ApiOutputerTypeEnum.find(outputTypeCode);
            if(apiOutputerTypeEnum == null){
                continue;
            }
            doOutput(apiDocOutputerFactory.getOutputer(apiOutputerTypeEnum), docs, null);
        }
    }


    protected List<ApiDocService> doParse() {
        return apiDocParser.parse();
    }


    protected void doOutput(ApiDocOutputer outputer, List<ApiDocService> docs, ApiDocContext apidocContext) {
        try {
            outputer.output(docs, apidocContext);
        } catch (Exception e) {
            logger.error("ApiOutpuer处理异常: outpuer:{},message:{}", outputer.getType(), e.getMessage());
        }
    }


}
