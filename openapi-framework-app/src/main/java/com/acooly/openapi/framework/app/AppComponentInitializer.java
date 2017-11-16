/*
 * www.yiji.com Inc.
 * Copyright (c) 2017 All Rights Reserved
 */

/*
 * 修订记录:
 * qiubo@yiji.com 2017-02-25 22:18 创建
 */
package com.acooly.openapi.framework.app;

import com.acooly.core.common.boot.component.ComponentInitializer;
import org.springframework.context.ConfigurableApplicationContext;

import static com.acooly.openapi.framework.core.OpenApiConstants.APP_CLIENT_ENABLE;


/** @author qiubo@yiji.com */
public class AppComponentInitializer implements ComponentInitializer {
  @Override
  public void initialize(ConfigurableApplicationContext applicationContext) {
    System.setProperty(APP_CLIENT_ENABLE, Boolean.TRUE.toString());
  }
}
