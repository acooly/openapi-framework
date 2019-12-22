/*
* acooly.cn Inc.
* Copyright (c) 2017 All Rights Reserved.
* create by acooly
* date:2017-12-05
*/
package com.acooly.openapi.apidoc.web;

import com.acooly.core.common.web.AbstractJsonEntityController;
import com.acooly.openapi.apidoc.persist.entity.ApiDocMessage;
import com.acooly.openapi.apidoc.persist.enums.ApiDocMessageMessageTypeEnum;
import com.acooly.openapi.apidoc.persist.service.ApiDocMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * 服务报文 管理控制器
 * 
 * @author acooly
 * Date: 2017-12-05 12:34:39
 */
@Controller
@RequestMapping(value = "/manage/openapi/apiDocMessage")
public class ApiDocMessageManagerController extends AbstractJsonEntityController<ApiDocMessage, ApiDocMessageService> {
	

	{
		allowMapping = "*";
	}

	@SuppressWarnings("unused")
	@Autowired
	private ApiDocMessageService apiDocMessageService;

	
	@Override
	protected void referenceData(HttpServletRequest request, Map<String, Object> model) {
		model.put("allMessageTypes", ApiDocMessageMessageTypeEnum.mapping());
	}

}
