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
import org.springframework.context.ConfigurableApplicationContext;

/** @author qiubo@yiji.com */
public class OpenAPIComponentInitializer implements ComponentInitializer {
  @Override
  public void initialize(ConfigurableApplicationContext applicationContext) {
    System.setProperty(
        "acooly.jpa.entityPackagesToScan.openapi", "com.acooly.openapi.framework.domain");
    if (Apps.buildProperties(OpenAPIProperties.class).getQueryLogSeparationEnable()) {
      System.setProperty("openapi.queryLogSeparationEnable", "true");
    }
  }
}
