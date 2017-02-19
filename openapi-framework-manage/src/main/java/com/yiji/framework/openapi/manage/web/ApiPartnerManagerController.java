/*
 * acooly.cn Inc.
 * Copyright (c) 2016 All Rights Reserved.
 * create by acooly
 * date:2016-07-16
 *
 */
package com.yiji.framework.openapi.manage.web;

import com.acooly.core.common.exception.BusinessException;
import com.acooly.core.common.web.AbstractJQueryEntityController;
import com.acooly.core.common.web.MappingMethod;
import com.acooly.core.common.web.support.JsonEntityResult;
import com.acooly.core.common.web.support.JsonResult;
import com.yiji.framework.openapi.common.enums.SecretType;
import com.yiji.framework.openapi.common.enums.SignType;
import com.yiji.framework.openapi.domain.ApiPartner;
import com.yiji.framework.openapi.service.ApiPartnerService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 合作方管理 管理控制器
 *
 * @author acooly
 *         Date: 2016-07-16 02:05:01
 */
@Controller
@RequestMapping(value = "/manage/openapi/apiPartner")
public class ApiPartnerManagerController extends AbstractJQueryEntityController<ApiPartner, ApiPartnerService> {


    {
        allowMapping = "list,delete,show,create,update,export";

    }

    @Resource
    private ApiPartnerService apiPartnerService;


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
