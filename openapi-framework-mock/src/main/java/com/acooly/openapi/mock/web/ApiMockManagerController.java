/*
* acooly.cn Inc.
* Copyright (c) 2018 All Rights Reserved.
* create by shuijing
* date:2018-08-14
*/
package com.acooly.openapi.mock.web;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.acooly.core.common.web.AbstractJQueryEntityController;
import com.acooly.openapi.mock.entity.ApiMock;
import com.acooly.openapi.mock.service.ApiMockService;

import com.google.common.collect.Maps;

/**
 * 服务类 管理控制器
 * 
 * @author shuijing
 * Date: 2018-08-14 17:35:43
 */
@Controller
@RequestMapping(value = "/manage/module/openapi/mock/apiMock")
public class ApiMockManagerController extends AbstractJQueryEntityController<ApiMock, ApiMockService> {
	

	{
		allowMapping = "*";
	}

	@SuppressWarnings("unused")
	@Autowired
	private ApiMockService apiMockService;

	

}
