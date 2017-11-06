/*
 * acooly.cn Inc.
 * Copyright (c) 2016 All Rights Reserved.
 * create by acooly
 * date:2016-07-16
 *
 */
package com.acooly.openapi.framework.manage.web;

import com.acooly.core.common.web.AbstractJQueryEntityController;
import com.acooly.openapi.framework.domain.ApiScheme;
import com.acooly.openapi.framework.service.ApiSchemeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 服务方案 管理控制器
 *
 * @author acooly Date: 2016-07-16 01:57:25
 */
// @Controller
@RequestMapping(value = "/manage/openapi/apiScheme")
public class ApiSchemeManagerController
    extends AbstractJQueryEntityController<ApiScheme, ApiSchemeService> {

  @SuppressWarnings("unused")
  @Autowired
  private ApiSchemeService apiSchemeService;

  {
    allowMapping = "*";
  }
}
