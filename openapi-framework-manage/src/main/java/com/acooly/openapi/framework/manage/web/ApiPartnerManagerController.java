/*
 * acooly.cn Inc.
 * Copyright (c) 2016 All Rights Reserved.
 * create by acooly
 * date:2016-07-16
 *
 */
package com.acooly.openapi.framework.manage.web;

import com.acooly.core.common.web.AbstractJQueryEntityController;
import com.acooly.core.common.web.support.JsonResult;
import com.acooly.openapi.framework.common.enums.SecretType;
import com.acooly.openapi.framework.common.enums.SignType;
import com.acooly.openapi.framework.domain.ApiPartner;
import com.acooly.openapi.framework.service.ApiPartnerService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * 合作方管理 管理控制器
 *
 * @author acooly Date: 2016-07-16 02:05:01
 */
@Controller
@RequestMapping(value = "/manage/openapi/apiPartner")
public class ApiPartnerManagerController
    extends AbstractJQueryEntityController<ApiPartner, ApiPartnerService> {

  @Resource private ApiPartnerService apiPartnerService;

  {
    allowMapping = "list,delete,show,create,update,export";
  }

  @Override
  protected void referenceData(HttpServletRequest request, Map<String, Object> model) {
    model.put("allSecretTypes", SecretType.mapping());
    model.put("allSignTypes", SignType.mapping());
  }

  /**
   * 生成商户号
   *
   * @param request
   * @param response
   * @return
   */
  @RequestMapping(value = "generatePartnerId")
  @ResponseBody
  public JsonResult generatePartnerId(HttpServletRequest request, HttpServletResponse response) {
    JsonResult result = new JsonResult();
    result.appendData("partnerId", apiPartnerService.generatePartnerid());
    return result;
  }

  /**
   * 生成秘钥
   *
   * @param request
   * @param response
   * @return
   */
  @RequestMapping(value = "generateSecretKey")
  @ResponseBody
  public JsonResult generateSecretKey(HttpServletRequest request, HttpServletResponse response) {
    JsonResult result = new JsonResult();
    result.appendData("secretKey", apiPartnerService.generateDigestSecurityKey(null));
    return result;
  }
}
