/*
 * acooly.cn Inc.
 * Copyright (c) 2016 All Rights Reserved.
 * create by acooly
 * date:2016-07-16
 *
 */
package com.acooly.openapi.framework.serviceimpl.manage.web;

import com.acooly.core.common.web.AbstractJQueryEntityController;
import com.acooly.core.common.web.support.JsonResult;
import com.acooly.openapi.framework.domain.ApiService;
import com.acooly.openapi.framework.domain.ApiServiceType;
import com.acooly.openapi.framework.service.ApiPartnerServiceService;
import com.acooly.openapi.framework.service.ApiServiceService;
import com.acooly.openapi.framework.service.ApiServiceTypeService;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * 服务分类 管理控制器
 *
 * @author acooly Date: 2016-07-16 01:57:14
 */
@Controller
@RequestMapping(value = "/manage/openapi/apiService")
public class ApiServiceManagerController
    extends AbstractJQueryEntityController<ApiService, ApiServiceService> {

  @Autowired private ApiServiceService apiServiceService;
  @Autowired private ApiServiceTypeService aiServiceTypeService;
  @Autowired private ApiPartnerServiceService apiPartnerServiceService;

  {
    allowMapping = "*";
  }

  @Override
  protected void onCreate(HttpServletRequest request, HttpServletResponse response, Model model) {
    String typeId = request.getParameter("typeId");
    ApiServiceType apiServiceType = aiServiceTypeService.get(Long.valueOf(typeId));
    model.addAttribute("typeId", typeId);
    model.addAttribute("typeName", apiServiceType.getName());
    super.onCreate(request, response, model);
  }

  @Override
  protected void onEdit(
      HttpServletRequest request, HttpServletResponse response, Model model, ApiService entity) {
    model.addAttribute("typeId", entity.getApiServiceType().getId());
    model.addAttribute("typeName", entity.getApiServiceType().getName());
    super.onEdit(request, response, model, entity);
  }

  @Override
  protected void onShow(
      HttpServletRequest request, HttpServletResponse response, Model model, ApiService entity)
      throws Exception {
    super.onShow(request, response, model, entity);
  }

  @Override
  protected ApiService onSave(
      HttpServletRequest request,
      HttpServletResponse response,
      Model model,
      ApiService entity,
      boolean isCreate)
      throws Exception {
    entity.setCode(entity.getName() + "_" + entity.getVersion());
    return super.onSave(request, response, model, entity, isCreate);
  }

  @Override
  protected void onRemove(
      HttpServletRequest request, HttpServletResponse response, Model model, Serializable... ids)
      throws Exception {
    List<Long> serviceIds = Lists.newArrayList();
    for (Serializable id : ids) {
      serviceIds.add((Long) id);
    }
    boolean exist = apiPartnerServiceService.exsitService(serviceIds);
    if (exist) {
      throw new RuntimeException("删除的服务已配置给商户使用,不能直接删除");
    }
  }

  @RequestMapping(value = {"search"})
  @ResponseBody
  public JsonResult search(HttpServletRequest request, HttpServletResponse response) {
    JsonResult result = new JsonResult();
    try {
      String key = request.getParameter("key");
      Map<String, List<ApiService>> data = apiServiceService.searchApiService(key);
      result.appendData("apis", data);
    } catch (Exception e) {
      handleException(result, "查询", e);
    }
    return result;
  }

  @Override
  protected void referenceData(HttpServletRequest request, Map<String, Object> model) {
    model.put("apiServiceTypeId", request.getParameter("apiServiceTypeId"));
  }
}
