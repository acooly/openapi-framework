/*
 * acooly.cn Inc.
 * Copyright (c) 2019 All Rights Reserved.
 * create by zhike
 * date:2019-02-19
 */
package com.acooly.openapi.apidoc.web;

import com.acooly.core.common.web.AbstractJsonEntityController;
import com.acooly.core.common.web.MappingMethod;
import com.acooly.openapi.apidoc.persist.entity.ApiDocSchemeDesc;
import com.acooly.openapi.apidoc.persist.service.ApiDocSchemeDescService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotNull;

/**
 * 服务方案 管理控制器
 *
 * @author zhike
 * Date: 2019-02-19 10:24:17
 */
@Slf4j
@Controller
@RequestMapping(value = "/manage/apidoc/apiDocSchemeDesc")
public class ApiDocSchemeDescManagerController extends AbstractJsonEntityController<ApiDocSchemeDesc, ApiDocSchemeDescService> {


    {
        allowMapping = "*";
    }

    @Autowired
    private ApiDocSchemeDescService apiDocSchemeDescService;

    /**
     * 文章内容编辑页面
     *
     * @param model
     * @param request
     * @return
     */
    @RequestMapping(value = "/createOrEdit")
    public String createOrEdit(HttpServletRequest request, HttpServletResponse response, Model model, @NotNull Long id) {
        allow(request, response, MappingMethod.update);
        try {
            model.addAllAttributes(referenceData(request));
            ApiDocSchemeDesc entity = loadEntity(request);
            if (entity == null) {
                entity = getEntityClass().newInstance();
                entity.setId(id);
                entity.setSchemeNo(request.getParameter("schemeNo"));
                model.addAttribute("action", ACTION_CREATE);
            }else {
                model.addAttribute("action", ACTION_EDIT);
            }
            model.addAttribute(getEntityName(), entity);
            onEdit(request, response, model, entity);
        } catch (Exception e) {
            log.warn(getExceptionMessage("edit", e), e);
            handleException("编辑", e, request);
        }
        return getEditView();
    }

}
