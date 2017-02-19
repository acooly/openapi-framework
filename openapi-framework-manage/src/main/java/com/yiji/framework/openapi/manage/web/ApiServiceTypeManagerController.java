/*
 * acooly.cn Inc.
 * Copyright (c) 2016 All Rights Reserved.
 * create by acooly
 * date:2016-07-16
 *
 */
package com.yiji.framework.openapi.manage.web;

import com.acooly.core.common.web.AbstractJQueryEntityController;
import com.acooly.core.common.web.support.JsonListResult;
import com.acooly.core.common.web.support.JsonResult;
import com.yiji.framework.openapi.domain.ApiServiceType;
import com.yiji.framework.openapi.service.ApiServiceTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 服务分类 管理控制器
 *
 * @author acooly
 *         Date: 2016-07-16 01:57:05
 */
@Controller
@RequestMapping(value = "/manage/openapi/apiServiceType")
public class ApiServiceTypeManagerController extends AbstractJQueryEntityController<ApiServiceType, ApiServiceTypeService> {


    {
        allowMapping = "*";
    }

    @SuppressWarnings("unused")
    @Autowired
    private ApiServiceTypeService apiServiceTypeService;


    @Override
    protected void onCreate(HttpServletRequest request, HttpServletResponse response, Model model) {
        model.addAttribute("parentId", request.getParameter("parentId"));
        super.onCreate(request, response, model);
    }

    @Override
    protected void onEdit(HttpServletRequest request, HttpServletResponse response, Model model, ApiServiceType entity) {
        model.addAttribute("parentId", entity.getParentId());
        super.onEdit(request, response, model, entity);
    }

    @Override
    protected ApiServiceType doSave(HttpServletRequest request, HttpServletResponse response, Model model,
                                    boolean isCreate) throws Exception {
        ApiServiceType entity = loadEntity(request);
        if (entity == null) {
            entity = getEntityClass().newInstance();
        }
        doDataBinding(request, entity);
        if (isCreate) {
            entity = getEntityService().create(entity.getParentId(), entity.getName(), entity.getComments());
        } else {
            getEntityService().save(entity);
        }
        return entity;
    }

    @RequestMapping("loadTree")
    @ResponseBody
    public JsonListResult<ApiServiceType> loadTree(HttpServletRequest request, HttpServletResponse response, Model model) {
        JsonListResult<ApiServiceType> result = new JsonListResult<ApiServiceType>();
        try {
            result.setRows(apiServiceTypeService.loadTree(null));
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
            ApiServiceType source = apiServiceTypeService.get(Long.valueOf(sourceId));
            ApiServiceType target = apiServiceTypeService.get(Long.valueOf(targetId));
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
