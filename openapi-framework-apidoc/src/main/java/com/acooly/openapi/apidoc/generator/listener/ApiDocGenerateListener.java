/*
 * www.acooly.cn Inc.
 * Copyright (c) 2015 All Rights Reserved.
 */

package com.acooly.openapi.apidoc.generator.listener;

import com.acooly.openapi.apidoc.ApiDocProperties;
import com.acooly.openapi.apidoc.generator.ApiDocGenerator;
import com.acooly.openapi.framework.common.event.ApiMetaParseFinish;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

/**
 * ApiDoc生成事件集成
 *
 * @author zhangpu 2018-1-2
 */
@Component
@Slf4j
public class ApiDocGenerateListener implements ApplicationListener<ApiMetaParseFinish> {

    @Autowired
    private ApiDocGenerator apiDocBuilder;

    @Override
    public void onApplicationEvent(ApiMetaParseFinish event) {
        log.debug("ApiDoc生成事件 [启动]");
        apiDocBuilder.build();
        log.debug("ApiDoc生成事件 [完成]");
    }

}
