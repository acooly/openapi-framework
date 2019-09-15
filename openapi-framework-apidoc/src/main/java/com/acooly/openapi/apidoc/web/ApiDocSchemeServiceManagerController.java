/*
* acooly.cn Inc.
* Copyright (c) 2017 All Rights Reserved.
* create by acooly
* date:2017-12-05
*/
package com.acooly.openapi.apidoc.web;

import com.acooly.core.common.web.AbstractJsonEntityController;
import com.acooly.openapi.apidoc.persist.entity.ApiDocSchemeService;
import com.acooly.openapi.apidoc.persist.service.ApiDocSchemeServiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 方案服务列表 管理控制器
 * 
 * @author acooly
 * Date: 2017-12-05 12:34:38
 */
@Controller
@RequestMapping(value = "/manage/openapi/apiDocSchemeService")
public class ApiDocSchemeServiceManagerController extends AbstractJsonEntityController<ApiDocSchemeService, ApiDocSchemeServiceService> {
	

	{
		allowMapping = "*";
	}

	@SuppressWarnings("unused")
	@Autowired
	private ApiDocSchemeServiceService apiDocSchemeServiceService;

	

}
