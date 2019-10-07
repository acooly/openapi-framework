/*
* acooly.cn Inc.
* Copyright (c) 2019 All Rights Reserved.
* create by zhike
* date:2019-02-20
*/
package com.acooly.openapi.apidoc.web;

import com.acooly.core.common.web.AbstractJQueryEntityController;
import com.acooly.core.common.web.support.JsonListResult;
import com.acooly.core.common.web.support.JsonResult;
import com.acooly.openapi.apidoc.persist.entity.ApiQaClassify;
import com.acooly.openapi.apidoc.persist.service.ApiQaClassifyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 问题分类表 管理控制器
 *
 * @author zhike
 * Date: 2019-02-20 17:09:59
 */
@Controller
@RequestMapping(value = "/manage/apidoc/apiQaClassify")
public class ApiQaClassifyManagerController extends AbstractJQueryEntityController<ApiQaClassify, ApiQaClassifyService> {


	{
		allowMapping = "*";
	}

	@SuppressWarnings("unused")
	@Autowired
	private ApiQaClassifyService apiQaClassifyService;

	@Override
	protected void onCreate(HttpServletRequest request, HttpServletResponse response, Model model) {
		model.addAttribute("parentId", request.getParameter("parentId"));
		super.onCreate(request, response, model);
	}

	@Override
	protected void onEdit(HttpServletRequest request, HttpServletResponse response, Model model, ApiQaClassify entity) {
		model.addAttribute("parentId", entity.getParentId());
		super.onEdit(request, response, model, entity);
	}

	@Override
	protected ApiQaClassify doSave(HttpServletRequest request, HttpServletResponse response, Model model,
								boolean isCreate) throws Exception {
		ApiQaClassify entity = loadEntity(request);
		if (entity == null) {
			entity = getEntityClass().newInstance();
		}
		doDataBinding(request, entity);
		if (isCreate) {
			entity = this.getEntityService().create(entity.getParentId(), entity.getName(), entity.getComments());
		} else {
			getEntityService().update(entity);
		}
		return entity;
	}

	@RequestMapping("loadTree")
	@ResponseBody
	public JsonListResult<ApiQaClassify> loadTree(HttpServletRequest request, HttpServletResponse response, Model model) {
		JsonListResult<ApiQaClassify> result = new JsonListResult<ApiQaClassify>();
		try {
			result.setRows(apiQaClassifyService.loadTree(null));
			result.setTotal((long) result.getRows().size());
		} catch (Exception e) {
			handleException(result, "loadTree", e);
		}
		return result;
	}

	@RequestMapping("move")
	@ResponseBody
	public JsonResult move(HttpServletRequest request, HttpServletResponse response, Model model) {
		JsonResult result = new JsonResult();
		String moveType = request.getParameter("moveType");
		String sourceId = request.getParameter("sourceId");
		String targetId = request.getParameter("targetId");
		try {
			ApiQaClassify source = apiQaClassifyService.get(Long.valueOf(sourceId));
			ApiQaClassify target = apiQaClassifyService.get(Long.valueOf(targetId));
			if ("inner".equals(moveType)) {
				source.setParentId(target.getId());
			} else if ("prev".equals(moveType)) {
				source.setSortTime(target.getSortTime() + 1);
				// 不同级
				if (source.getParentId() != null && target.getParentId() != null
						&& !source.getParentId().equals(target.getParentId())) {
					source.setParentId(target.getParentId());
				}
			} else if ("next".equals(moveType)) {
				source.setSortTime(target.getSortTime() - 1);
				// 不同级
				if (source.getParentId() != null && target.getParentId() != null
						&& !source.getParentId().equals(target.getParentId())) {
					source.setParentId(target.getParentId());
				}
			}
			getEntityService().save(source);
		} catch (Exception e) {
			handleException(result, "移动[" + moveType + "]", e);
		}
		return result;
	}

}
