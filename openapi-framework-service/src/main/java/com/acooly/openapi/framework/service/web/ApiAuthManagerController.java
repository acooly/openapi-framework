/*
 * acooly.cn Inc.
 * Copyright (c) 2018 All Rights Reserved.
 * create by qiubo
 * date:2018-08-21
 */
package com.acooly.openapi.framework.service.web;

import com.acooly.core.common.view.ViewResult;
import com.acooly.core.common.web.AbstractJsonEntityController;
import com.acooly.openapi.framework.common.enums.SignType;
import com.acooly.openapi.framework.common.utils.AccessKeys;
import com.acooly.openapi.framework.service.domain.ApiAuth;
import com.acooly.openapi.framework.service.service.ApiAuthService;
import com.acooly.openapi.framework.service.service.ApiMetaServiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 认证授权信息管理 管理控制器
 *
 * @author qiubo
 * Date: 2018-08-21 14:31:06
 */
@Controller
@RequestMapping(value = "/manage/module/openapi/apiAuth")
public class ApiAuthManagerController extends AbstractJsonEntityController<ApiAuth, ApiAuthService> {


    {
        allowMapping = "*";
    }

    @SuppressWarnings("unused")
    @Autowired
    private ApiAuthService apiAuthService;
    @Autowired
    private ApiMetaServiceService apiMetaServiceService;

    @RequestMapping(value = "generateAccessKey")
    @ResponseBody
    public ViewResult generatePartnerId(HttpServletRequest request, HttpServletResponse response) {
        return ViewResult.success(AccessKeys.newAccessKey());
    }

    @RequestMapping(value = "generateSecretKey")
    @ResponseBody
    public ViewResult generateSecretKey(HttpServletRequest request, HttpServletResponse response, SignType signType) {
        if (signType == null) {
            signType = SignType.MD5;
        }
        if (signType == SignType.MD5) {
            return ViewResult.success(AccessKeys.newSecretKey());
        } else {
            throw new UnsupportedOperationException("不支持的signType:" + signType);
        }
    }

    @RequestMapping("setting")
    public String setting(HttpServletRequest request, HttpServletResponse response, Model model, Long id) {
        try {
            ApiAuth apiAuth = this.getEntityService().get(id);
            model.addAttribute("apiAuth", apiAuth);
            model.addAllAttributes(referenceData(request));
        } catch (Exception e) {
            handleException("设置权限", e, request);
        }
        return "/manage/module/openapi/apiAuthSetting";
    }

    @RequestMapping(value = "getAllService")
    @ResponseBody
    public ViewResult getAllService(HttpServletRequest request, HttpServletResponse response) {
        return ViewResult.success(apiMetaServiceService.getAll());
    }


    @RequestMapping(value = "setPerm")
    @ResponseBody
    public ViewResult setPerm(String accessKey, String perm) {
        ApiAuth apiAuth = apiAuthService.findByAccesskey(accessKey);
        apiAuth.setPermissions(perm);
        apiAuthService.update(apiAuth);
        return ViewResult.success(null);
    }


}
