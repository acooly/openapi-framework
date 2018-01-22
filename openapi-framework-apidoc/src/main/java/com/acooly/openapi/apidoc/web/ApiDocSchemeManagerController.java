/*
* acooly.cn Inc.
* Copyright (c) 2017 All Rights Reserved.
* create by acooly
* date:2017-12-05
*/
package com.acooly.openapi.apidoc.web;

import com.acooly.core.common.web.AbstractJQueryEntityController;
import com.acooly.openapi.apidoc.persist.entity.ApiDocScheme;
import com.acooly.openapi.apidoc.persist.enums.ApiDocSchemeBusiTypeEnum;
import com.acooly.openapi.apidoc.persist.enums.ApiDocSchemeSchemeTypeEnum;
import com.acooly.openapi.apidoc.persist.service.ApiDocSchemeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * 服务方案 管理控制器
 * 
 * @author acooly
 * Date: 2017-12-05 12:34:38
 */
@Controller
@RequestMapping(value = "/manage/openapi/apiDocScheme")
public class ApiDocSchemeManagerController extends AbstractJQueryEntityController<ApiDocScheme, ApiDocSchemeService> {
	

	{
		allowMapping = "*";
	}

	@SuppressWarnings("unused")
	@Autowired
	private ApiDocSchemeService apiDocSchemeService;

	
	@Override
	protected void referenceData(HttpServletRequest request, Map<String, Object> model) {
		model.put("allSchemeTypes", ApiDocSchemeSchemeTypeEnum.mapping());
		model.put("allBusiTypes", ApiDocSchemeBusiTypeEnum.mapping());
	}

}
