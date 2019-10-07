/*
* acooly.cn Inc.
* Copyright (c) 2019 All Rights Reserved.
* create by zhike
* date:2019-02-19
*/
package com.acooly.openapi.apidoc.web;

import com.acooly.core.common.web.AbstractJQueryEntityController;
import com.acooly.core.common.web.MappingMethod;
import com.acooly.core.common.web.support.JsonEntityResult;
import com.acooly.openapi.apidoc.persist.entity.ApiDocService;
import com.acooly.openapi.apidoc.persist.enums.ApiDocServiceBusiTypeEnum;
import com.acooly.openapi.apidoc.persist.enums.ApiDocServiceServiceTypeEnum;
import com.acooly.openapi.apidoc.persist.service.ApiDocServiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * 服务信息 管理控制器
 * 
 * @author zhike
 * Date: 2019-02-19 10:24:17
 */
@Controller
@RequestMapping(value = "/manage/apidoc/apiDocService")
public class ApiDocServiceManagerController extends AbstractJQueryEntityController<ApiDocService, ApiDocServiceService> {
	

	{
		allowMapping = "*";
	}

	@SuppressWarnings("unused")
	@Autowired
	private ApiDocServiceService apiDocServiceService;


	@RequestMapping("showServiceInfo")
	public String showServiceInfo(long id, Model model) {
		model.addAttribute("id",id);
		return "/manage/apidoc/apiServiceDocInfoShow";
	}

	@RequestMapping("servcieBaseInfo")
	@ResponseBody
	private JsonEntityResult<ApiDocService> servcieBaseInfo(long id, HttpServletRequest request, HttpServletResponse response) {
		this.allow(request, response, MappingMethod.show);
		JsonEntityResult result = new JsonEntityResult();
		try {
			ApiDocService entity = apiDocServiceService.loadApiDocService(id);
			result.setEntity(entity);
			result.setMessage("查询服务信息成功");
		} catch (Exception var5) {
			this.handleException(result, "查询", var5);
		}

		return result;
	}

	@Override
	protected void referenceData(HttpServletRequest request, Map<String, Object> model) {
		model.put("allServiceTypes", ApiDocServiceServiceTypeEnum.mapping());
		model.put("allBusiTypes", ApiDocServiceBusiTypeEnum.mapping());
	}

}
