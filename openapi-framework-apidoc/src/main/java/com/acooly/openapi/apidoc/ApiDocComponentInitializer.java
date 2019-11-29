/*
 * www.yiji.com Inc.
 * Copyright (c) 2017 All Rights Reserved
 */

/*
 * 修订记录:
 * qiubo@yiji.com 2017-03-14 00:56 创建
 */
package com.acooly.openapi.apidoc;

import com.acooly.core.common.boot.component.ComponentInitializer;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * @author qiubo@yiji.com
 */
public class ApiDocComponentInitializer implements ComponentInitializer {
    @Override
    public void initialize(ConfigurableApplicationContext applicationContext) {
        setPropertyIfMissing("acooly.mybatis.dao-scan-packages.apidoc", "com.acooly.openapi.apidoc.persist.dao");
        setPropertyIfMissing("acooly.mybatis.type-aliases-package.apidoc", "com.acooly.openapi.apidoc.persist.entity");
    }
}
