/*
 * www.yiji.com Inc.
 * Copyright (c) 2015 All Rights Reserved.
 */

package com.acooly.openapi.apidoc.generator;

import com.acooly.core.utils.Collections3;
import com.acooly.openapi.apidoc.ApiDocProperties;
import com.acooly.openapi.apidoc.generator.output.ApiDocOutputer;
import com.acooly.openapi.apidoc.generator.output.ApiDocOutputerFactory;
import com.acooly.openapi.apidoc.generator.parser.ApiDocParser;
import com.acooly.openapi.apidoc.generator.parser.ApiDocParserFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Set;

/**
 * Apidoc生成器实现
 *
 * @author zhangpu on 2015/1/29.
 */
@Service("apiDocBuilder")
@Slf4j
public class ApiDocGeneratorImpl implements ApiDocGenerator {

    @Autowired
    private ApiDocParserFactory apiDocParserFactory;

    @Autowired
    private ApiDocOutputerFactory apiDocOutputerFactory;

    @Autowired
    private ApiDocProperties apiDocProperties;

    @Override
    public void build() {

        Collection<ApiDocParser> apiDocParsers = apiDocParserFactory.getApiDocParsers();
        if (Collections3.isEmpty(apiDocParsers)) {
            log.info("没有注册的ApiDocParser存在，退出自动生成文档");
            return;
        }

        Set<ApiDocOutputer> apiDocOutputers = null;
        for (ApiDocParser apiDocParser : apiDocParsers) {
            apiDocOutputers = apiDocOutputerFactory.getOutputers(apiDocParser.getModule());
            if (Collections3.isEmpty(apiDocOutputers)) {
                log.warn("模块:{} 没有对应的输出实现。", apiDocParser.getModule());
                continue;
            }
            Object object = apiDocParser.parse();
            apiDocOutputers.forEach(e -> {
                if (apiDocProperties.getOutputTypes().contains(e.getType().code())) {
                    e.output(object);
                }
            });
        }
    }


}
