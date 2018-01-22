/*
* acooly.cn Inc.
* Copyright (c) 2017 All Rights Reserved.
* create by acooly
* date:2017-12-05
*/
package com.acooly.openapi.apidoc.web;

import com.acooly.core.common.web.AbstractJQueryEntityController;
import com.acooly.openapi.apidoc.persist.entity.ApiDocService;
import com.acooly.openapi.apidoc.persist.enums.ApiDocServiceBusiTypeEnum;
import com.acooly.openapi.apidoc.persist.enums.ApiDocServiceServiceTypeEnum;
import com.acooly.openapi.apidoc.persist.service.ApiDocServiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * 服务 管理控制器
 * 
 * @author acooly
 * Date: 2017-12-05 12:34:39
 */
@Controller
@RequestMapping(value = "/manage/openapi/apiDocService")
public class ApiDocServiceManagerController extends AbstractJQueryEntityController<ApiDocService, ApiDocServiceService> {
	

	{
		allowMapping = "*";
	}

	@SuppressWarnings("unused")
	@Autowired
	private ApiDocServiceService apiDocServiceService;

	
	@Override
	protected void referenceData(HttpServletRequest request, Map<String, Object> model) {
		model.put("allServiceTypes", ApiDocServiceServiceTypeEnum.mapping());
		model.put("allBusiTypes", ApiDocServiceBusiTypeEnum.mapping());
	}

}
