/*
* acooly.cn Inc.
* Copyright (c) 2019 All Rights Reserved.
* create by zhike
* date:2019-02-20
*/
package com.acooly.openapi.apidoc.web;

import com.acooly.core.common.web.AbstractJQueryEntityController;
import com.acooly.core.common.web.support.JsonResult;
import com.acooly.openapi.apidoc.persist.entity.ApiQaClassify;
import com.acooly.openapi.apidoc.persist.entity.ApiQaQuestion;
import com.acooly.openapi.apidoc.persist.service.ApiQaClassifyService;
import com.acooly.openapi.apidoc.persist.service.ApiQaQuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.util.HtmlUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * 问题记录表 管理控制器
 * 
 * @author zhike
 * Date: 2019-02-20 17:09:59
 */
@Controller
@RequestMapping(value = "/manage/apidoc/apiQaQuestion")
public class ApiQaQuestionManagerController extends AbstractJQueryEntityController<ApiQaQuestion, ApiQaQuestionService> {
	

	{
		allowMapping = "*";
	}

	@Autowired
	private ApiQaQuestionService apiQaQuestionService;

	@Autowired
	private ApiQaClassifyService qaClassifyService;

	@Override
	protected void onCreate(HttpServletRequest request, HttpServletResponse response, Model model) {
		String typeId = request.getParameter("typeId");
		ApiQaClassify qaClassify = qaClassifyService.get(Long.valueOf(typeId));
		model.addAttribute("typeId", typeId);
		model.addAttribute("typeName", qaClassify.getName());
		super.onCreate(request, response, model);
	}

	@Override
	protected void onEdit(HttpServletRequest request, HttpServletResponse response, Model model, ApiQaQuestion entity) {
		ApiQaClassify qaClassify = qaClassifyService.get(Long.valueOf(entity.getClassifyId()));
		model.addAttribute("typeId", qaClassify.getId());
		model.addAttribute("typeName", qaClassify.getName());
		super.onEdit(request, response, model, entity);
	}

	@Override
	protected void onShow(HttpServletRequest request, HttpServletResponse response, Model model, ApiQaQuestion entity)
			throws Exception {
		super.onShow(request, response, model, entity);
	}

	@Override
	protected ApiQaQuestion onSave(HttpServletRequest request, HttpServletResponse response, Model model, ApiQaQuestion entity,
								boolean isCreate) throws Exception {
		entity.setSolution(HtmlUtils.htmlUnescape(entity.getSolution()));
		return super.onSave(request, response, model, entity, isCreate);
	}

	@RequestMapping(value = {"search"})
	@ResponseBody
	public JsonResult search(HttpServletRequest request, HttpServletResponse response) {
		JsonResult result = new JsonResult();
		try {
			String key = request.getParameter("key");
			Map<String, List<ApiQaQuestion>> data = apiQaQuestionService.searchQaQuestion(key);
			result.appendData("apis", data);
		} catch (Exception e) {
			handleException(result, "查询", e);
		}
		return result;
	}


	@Override
	protected void referenceData(HttpServletRequest request, Map<String, Object> model) {
		model.put("qaClassifyId", request.getParameter("qaClassifyId"));
	}




}
