/*
 * www.yiji.com Inc.
 * Copyright (c) 2015 All Rights Reserved.
 */

package com.acooly.openapi.apidoc.generator.output.impl;

import com.acooly.openapi.apidoc.generator.ApiDocModule;
import com.acooly.openapi.apidoc.generator.output.ApiDocOutputer;
import com.acooly.openapi.apidoc.generator.output.ApiOutputerTypeEnum;
import com.acooly.openapi.apidoc.persist.entity.ApiDocService;
import com.alibaba.fastjson.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 控制台输出实现
 * <p/>
 * Created by zhangpu on 2015/2/26.
 */
@Component("consoleApiDocOutputer")
public class ConsoleApiDocOutputer implements ApiDocOutputer<List<ApiDocService>> {
    private static final Logger logger = LoggerFactory.getLogger(ConsoleApiDocOutputer.class);

    @Override
    public void output(List<ApiDocService> apiServiceDocs) {
        String console = JSON.toJSONString(apiServiceDocs, true);
        logger.info(console);
    }


    @Override
    public ApiOutputerTypeEnum getType() {
        return ApiOutputerTypeEnum.console;
    }

    @Override
    public ApiDocModule getModule() {
        return ApiDocModule.api;
    }
}
