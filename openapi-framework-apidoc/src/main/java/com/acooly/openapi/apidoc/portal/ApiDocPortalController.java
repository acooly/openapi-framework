/*
 * www.acooly.cn Inc.
 * Copyright (c) 2017 All Rights Reserved
 */

/*
 * 修订记录:
 * zhangpu@acooly.cn 2017-08-01 创建
 */
package com.acooly.openapi.apidoc.portal;

import com.acooly.core.common.web.support.JsonEntityResult;
import com.acooly.core.common.web.support.JsonListResult;
import com.acooly.core.common.web.support.JsonResult;
import com.acooly.core.utils.Strings;
import com.acooly.openapi.apidoc.enums.FieldStatus;
import com.acooly.openapi.apidoc.enums.MessageTypeEnum;
import com.acooly.openapi.apidoc.persist.entity.ApiDocScheme;
import com.acooly.openapi.apidoc.persist.entity.ApiDocService;
import com.acooly.openapi.apidoc.persist.service.ApiDocSchemeService;
import com.acooly.openapi.apidoc.persist.service.ApiDocSchemeServiceService;
import com.acooly.openapi.apidoc.persist.service.ApiDocServiceService;
import com.acooly.openapi.framework.common.enums.ResponseType;
import com.google.common.collect.Maps;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * apidoc的实例报文 控制器
 *
 * @author zhangpu
 */
@Controller
@RequestMapping("/docs/apidoc")
public class ApiDocPortalController extends AbstractPortalController {


    @Autowired
    protected ApiDocServiceService apiDocServiceService;

    @Autowired
    private ApiDocSchemeService apiDocSchemeService;
    @Autowired
    private ApiDocSchemeServiceService apiDocSchemeServiceService;


    /**
     * ApiDoc 首页
     *
     * @param request
     * @param response
     * @param model
     * @return
     */
    @Override
    public String index(HttpServletRequest request, HttpServletResponse response, Model model) {
        try {
            long schemeId = Long.parseLong(request.getParameter("schemeId"));
            String apidocId = request.getParameter("apidocId");
            ApiDocScheme apiScheme = apiDocSchemeService.get(schemeId);
            model.addAttribute("apiScheme", apiScheme);
            model.addAttribute("apidocId", apidocId);
            //doLoadServiceMenus(request, response, model);
            model.addAllAttributes(referenceData(request));
        } catch (Exception e) {
            handleException("", e, request);
        }
        return "/docs/apidoc/apidoc";
    }


    /**
     * apidoc 菜单
     */
    @RequestMapping("menu")
    public String menu(HttpServletRequest request, HttpServletResponse response, Model model) {
        try {
            String id = request.getParameter("schemeId");
            ApiDocScheme apiDocScheme = apiDocSchemeService.get(Long.valueOf(id));
            doApidocList(request, response, model);
            model.addAttribute("page", request.getParameter("page"));
            model.addAllAttributes(referenceData(request));
        } catch (Exception e) {
            handleException("", e, request);
        }
        return "/docs/apidoc/apimenu";
    }


    /**
     * 分类服务列表
     *
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("apiDocList")
    @ResponseBody
    public JsonListResult<ApiDocService> apiServiceList(HttpServletRequest request, HttpServletResponse response) {
        JsonListResult<ApiDocService> result = new JsonListResult<ApiDocService>();
        long id = Long.parseLong(request.getParameter("id"));
        try {
            ApiDocScheme apiScheme = apiDocSchemeService.get(id);
            List<ApiDocService> entities = apiDocSchemeServiceService.findSchemeApiDocServices(apiScheme.getSchemeNo());
            result.setTotal((long) entities.size());
            result.setRows(entities);
            result.appendData("schemeName", apiScheme.getTitle());
            result.appendData("schemeId", id);
            result.appendData(referenceData(request));
        } catch (Exception e) {
            handleException("解决方案服务列表查询", e, request);
        }
        return result;
    }


    @RequestMapping(value = "apidoc", method = RequestMethod.POST)
    @ResponseBody
    public JsonEntityResult<ApiDocService> apidocInfo(HttpServletRequest request, HttpServletResponse response) {
        JsonEntityResult<ApiDocService> result = new JsonEntityResult<ApiDocService>();
        try {
            ApiDocService apiServiceDoc = doLoadApidoc(request);
            result.setEntity(apiServiceDoc);
            result.appendData(referenceData(request));
        } catch (Exception e) {
            handleException(result, "apidoc详情", e);
        }
        return result;
    }


    @RequestMapping(value = "apidoc")
    @ResponseBody
    public Object apidocMetadata(HttpServletRequest request, HttpServletResponse response) {
        JsonResult result = new JsonResult();
        try {
            return doLoadApidoc(request);
        } catch (Exception e) {
            handleException(result, "apidoc元数据", e);
        }
        return result;
    }


    protected ApiDocService doLoadApidoc(HttpServletRequest request) {
        String id = request.getParameter("id");
        String serviceNo = request.getParameter("serviceNo");
        ApiDocService apiServiceDoc = null;
        if (Strings.isNotBlank(id)) {
            apiServiceDoc = apiDocServiceService.loadApiDocService(Long.valueOf(id));
        }
        if (apiServiceDoc == null && Strings.isNotBlank(serviceNo)) {
            apiServiceDoc = apiDocServiceService.loadApiDocServiceByNo(serviceNo);
        }
        return apiServiceDoc;
    }

    protected void doApidocList(HttpServletRequest request, HttpServletResponse response, Model model) {
        String schemeNo = request.getParameter("schemeNo");
        List<ApiDocService> apidocs = apiDocSchemeServiceService.findSchemeApiDocServices(schemeNo);
        model.addAttribute("apidocs", apidocs);
    }


    protected void doLoadServiceMenus(HttpServletRequest request, HttpServletResponse response, Model model) {
        String schemeId = request.getParameter("schemeId");
        List<ApiDocService> apiServiceDocs = null; //apiDocSchemeService.getSchemeService(Long.parseLong(schemeId));
        model.addAttribute("apiDocs", apiServiceDocs);
    }


    @Override
    protected void referenceData(HttpServletRequest request, Map model) {
        model.put("allServiceTypes", serviceTypeMapping);
        model.put("allMessageTypes", MessageTypeEnum.mapping());
        model.put("allStatuss", FieldStatus.mapping());
    }

    public static Map<String, String> serviceTypeMapping = Maps.newHashMap();

    static {
        serviceTypeMapping.put(ResponseType.SYN.name(), "同步服务");
        serviceTypeMapping.put(ResponseType.ASNY.name(), "异步服务");
        serviceTypeMapping.put(ResponseType.REDIRECT.name(), "跳转服务");
    }

}
