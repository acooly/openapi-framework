/*
 * www.acooly.cn Inc.
 * Copyright (c) 2015 All Rights Reserved.
 */

package com.acooly.openapi.apidoc.listener;

import com.acooly.openapi.apidoc.ApiDocProperties;
import com.acooly.openapi.apidoc.builder.ApiDocBuilder;
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
    private ApiDocProperties apiDocProperties;
    @Autowired
    private ApiDocBuilder apiDocBuilder;

    @Override
    public void onApplicationEvent(ApiMetaParseFinish event) {
        log.info("启动ApiDoc生成...");
        apiDocBuilder.build();
        log.info("完成ApiDoc生成");
    }

}
