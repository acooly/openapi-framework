/*
 * www.yiji.com Inc.
 * Copyright (c) 2017 All Rights Reserved
 */

/*
 * 修订记录:
 * qiubo@yiji.com 2017-02-19 21:46 创建
 */
package com.acooly.openapi.framework.core;

import com.acooly.core.common.boot.Apps;
import com.acooly.core.common.boot.component.ComponentInitializer;
import com.acooly.core.utils.ObjectId;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * @author qiubo@yiji.com
 */
@Slf4j
public class OpenAPIComponentInitializer implements ComponentInitializer {
    @Override
    public void initialize(ConfigurableApplicationContext applicationContext) {
        System.setProperty(
                "acooly.jpa.entityPackagesToScan.api", "com.acooly.openapi.framework.domain");
        if (Apps.buildProperties(OpenAPIProperties.class).getQueryLogSeparationEnable()) {
            System.setProperty("openapi.queryLogSeparationEnable", "true");
        }
        System.setProperty("acooly.mybatis.daoScanPackages.api", "com.acooly.openapi.framework.serviceimpl.manage");

        // 启动时新线程同步初始化获取机器码静态变量
        new Thread(() -> {
            log.info("ObjectId init start..");
            ObjectId.get();
            log.info("ObjectId init end");
        }, "acooly-init").run();
    }
}
