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
import com.acooly.core.utils.Collections3;
import com.acooly.core.utils.ObjectId;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.List;

/**
 * @author qiubo@yiji.com
 */
@Slf4j
public class OpenAPIComponentInitializer implements ComponentInitializer {
    @Override
    public void initialize(ConfigurableApplicationContext applicationContext) {
        System.setProperty("acooly.jpa.entityPackagesToScan.api", "com.acooly.openapi.framework.domain");
        if (Apps.buildProperties(OpenAPIProperties.class).getQueryLogSeparationEnable()
                || Apps.buildProperties(OpenAPIProperties.class).getLog().getMultFileEnable()) {
            System.setProperty("openapi.queryLogSeparationEnable", "true");
        }

        List<String> gateways = Apps.buildProperties(OpenAPIProperties.class).getGateways();
        if (Collections3.isNotEmpty(gateways)) {
            for (int i = 0; i < gateways.size(); i++) {
                System.setProperty("acooly.security.csrf.exclusions.gateway[" + i + "]", gateways.get(i));
            }
        }

        System.setProperty("acooly.mybatis.daoScanPackages.api", "com.acooly.openapi.framework.serviceimpl.manage");

        // 启动时新线程同步初始化获取机器码静态变量,加快首次访问速度
        Thread thread = new Thread(() -> {
            log.info("ObjectId Initialized: " + ObjectId.get().toHexString());
        });
        thread.setName("ObjectId-init-thread");
        thread.start();

    }
}
