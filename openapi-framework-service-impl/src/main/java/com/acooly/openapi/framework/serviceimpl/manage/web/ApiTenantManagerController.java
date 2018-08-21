/*
* acooly.cn Inc.
* Copyright (c) 2018 All Rights Reserved.
* create by qiubo
* date:2018-08-21
*/
package com.acooly.openapi.framework.serviceimpl.manage.web;

import com.acooly.core.common.web.AbstractJQueryEntityController;
import com.acooly.openapi.framework.serviceimpl.manage.entity.ApiTenant;
import com.acooly.openapi.framework.serviceimpl.manage.enums.SecretTypeEnum;
import com.acooly.openapi.framework.serviceimpl.manage.enums.SignTypeEnum;
import com.acooly.openapi.framework.serviceimpl.manage.service.ApiTenantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * 租户管理 管理控制器
 * 
 * @author qiubo
 * Date: 2018-08-21 14:31:05
 */
@Controller
@RequestMapping(value = "/manage/module/openapi/apiTenant")
public class ApiTenantManagerController extends AbstractJQueryEntityController<ApiTenant, ApiTenantService> {
	

	{
		allowMapping = "*";
	}

	@SuppressWarnings("unused")
	@Autowired
	private ApiTenantService apiTenantService;

	
	@Override
	protected void referenceData(HttpServletRequest request, Map<String, Object> model) {
		model.put("allSecretTypes", SecretTypeEnum.mapping());
		model.put("allSignTypes", SignTypeEnum.mapping());
	}

}
