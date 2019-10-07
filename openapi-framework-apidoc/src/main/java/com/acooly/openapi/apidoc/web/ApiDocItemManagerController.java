/*
* acooly.cn Inc.
* Copyright (c) 2017 All Rights Reserved.
* create by acooly
* date:2017-12-05
*/
package com.acooly.openapi.apidoc.web;

import com.acooly.core.common.web.AbstractJQueryEntityController;
import com.acooly.openapi.apidoc.enums.ApiEncryptstatusEnum;
import com.acooly.openapi.apidoc.persist.entity.ApiDocItem;
import com.acooly.openapi.apidoc.persist.service.ApiDocItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * 报文字段 管理控制器
 * 
 * @author acooly
 * Date: 2017-12-05 12:34:39
 */
@Controller
@RequestMapping(value = "/manage/openapi/apiDocItem")
public class ApiDocItemManagerController extends AbstractJQueryEntityController<ApiDocItem, ApiDocItemService> {
	

	{
		allowMapping = "*";
	}

	@SuppressWarnings("unused")
	@Autowired
	private ApiDocItemService apiDocItemService;

	
	@Override
	protected void referenceData(HttpServletRequest request, Map<String, Object> model) {
		model.put("allEncryptstatuss", ApiEncryptstatusEnum.mapping());
	}

}
