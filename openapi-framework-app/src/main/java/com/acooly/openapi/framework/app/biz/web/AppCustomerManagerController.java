package com.acooly.openapi.framework.app.biz.web;

import com.acooly.core.common.web.AbstractJQueryEntityController;
import com.acooly.core.common.web.support.JsonResult;
import com.acooly.openapi.framework.app.biz.domain.AppCustomer;
import com.acooly.openapi.framework.app.biz.enums.EntityStatus;
import com.acooly.openapi.framework.app.biz.service.AppCustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@Controller
@RequestMapping(value = "/manage/module/app/appCustomer")
public class AppCustomerManagerController
    extends AbstractJQueryEntityController<AppCustomer, AppCustomerService> {

  private static Map<String, String> allStatuss = EntityStatus.mapping();

  @Autowired private AppCustomerService appCustomerService;

  @RequestMapping(value = "disable")
  @ResponseBody
  public JsonResult disable(HttpServletRequest request, HttpServletResponse response) {
    JsonResult result = new JsonResult();
    try {
      AppCustomer appCustomer = loadEntity(request);
      appCustomer.setStatus(EntityStatus.Disable);
      getEntityService().save(appCustomer);
      result.setMessage("禁用成功");
    } catch (Exception e) {
      handleException(result, "禁用", e);
    }
    return result;
  }

  @Override
  protected void referenceData(HttpServletRequest request, Map<String, Object> model) {
    model.put("allStatuss", allStatuss);
  }
}
