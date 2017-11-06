/*
 * acooly.cn Inc.
 * Copyright (c) 2016 All Rights Reserved.
 * create by acooly
 * date:2016-07-28
 *
 */
package com.acooly.openapi.framework.manage.web;

import com.acooly.core.common.web.AbstractJQueryEntityController;
import com.acooly.core.common.web.support.JsonResult;
import com.acooly.openapi.framework.common.dto.ApiServiceDto;
import com.acooly.openapi.framework.domain.ApiPartner;
import com.acooly.openapi.framework.domain.ApiPartnerService;
import com.acooly.openapi.framework.service.ApiPartnerServiceService;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * api_partner_service 管理控制器
 *
 * @author acooly Date: 2016-07-28 15:33:42
 */
@Controller
@RequestMapping(value = "/manage/openapi/apiPartnerService")
public class ApiPartnerServiceManagerController
    extends AbstractJQueryEntityController<ApiPartnerService, ApiPartnerServiceService> {

  @SuppressWarnings("unused")
  @Autowired
  private ApiPartnerServiceService apiPartnerServiceService;
  @Autowired private com.acooly.openapi.framework.service.ApiPartnerService apiPartnerService;

  {
    allowMapping = "*";
  }

  @RequestMapping("setting")
  public String setting(HttpServletRequest request, HttpServletResponse response, Model model) {
    try {
      String id = request.getParameter("id");
      ApiPartner apiPartner = apiPartnerService.get(Long.valueOf(id));
      model.addAttribute("apiPartner", apiPartner);
      model.addAllAttributes(referenceData(request));
    } catch (Exception e) {
      handleException("设置权限", e, request);
    }
    return "/manage/openapi/apiPartnerServiceSetting";
  }

  @RequestMapping("settingSave")
  @ResponseBody
  public JsonResult settingSave(
      HttpServletRequest request, HttpServletResponse response, Model model) {
    JsonResult result = new JsonResult();
    try {
      String[] requestIds = request.getParameterValues("manage_partner_api");
      String apiPartnerId = request.getParameter("apiPartnerId");
      List<Long> serviceIds = Lists.newArrayList();
      if (requestIds != null && requestIds.length > 0) {
        for (String requestId : requestIds) {
          serviceIds.add(Long.valueOf(requestId));
        }
      }
      apiPartnerServiceService.batchSave(Long.valueOf(apiPartnerId), serviceIds);
      model.addAllAttributes(referenceData(request));
      result.setMessage("设置保存成功");
    } catch (Exception e) {
      handleException(result, "保存权限", e);
    }
    return result;
  }

  @RequestMapping(value = {"search"})
  @ResponseBody
  public JsonResult search(HttpServletRequest request, HttpServletResponse response) {
    JsonResult result = new JsonResult();
    try {
      String apiPartnerId = request.getParameter("apiPartnerId");
      Map<String, List<ApiServiceDto>> data =
          apiPartnerServiceService.searchServiceByPartner(Long.valueOf(apiPartnerId));
      result.appendData("apis", data);
    } catch (Exception e) {
      handleException(result, "查询", e);
    }
    return result;
  }
}
