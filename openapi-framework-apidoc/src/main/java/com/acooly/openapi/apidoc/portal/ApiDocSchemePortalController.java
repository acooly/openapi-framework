/*
 * www.acooly.cn Inc.
 * Copyright (c) 2017 All Rights Reserved
 */

/*
 * 修订记录:
 * zhangpu@acooly.cn 2017-10-11 00:00 创建
 */
package com.acooly.openapi.apidoc.portal;

import com.acooly.core.utils.Collections3;
import com.acooly.core.utils.Servlets;
import com.acooly.core.utils.Strings;
import com.acooly.module.cms.domain.Content;
import com.acooly.module.cms.service.ContentService;
import com.acooly.openapi.apidoc.ApiDocProperties;
import com.acooly.openapi.apidoc.enums.SchemeTypeEnum;
import com.acooly.openapi.apidoc.persist.entity.ApiDocScheme;
import com.acooly.openapi.apidoc.persist.entity.ApiDocSchemeDesc;
import com.acooly.openapi.apidoc.persist.entity.ApiDocService;
import com.acooly.openapi.apidoc.persist.service.ApiDocSchemeDescService;
import com.acooly.openapi.apidoc.persist.service.ApiDocSchemeService;
import com.acooly.openapi.apidoc.persist.service.ApiDocSchemeServiceService;
import com.acooly.openapi.apidoc.portal.dto.ApiDocSchemeDto;
import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.util.HtmlUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author zhangpu 2017-10-11 00:00
 */
@Controller
@RequestMapping("/docs/scheme")
public class ApiDocSchemePortalController extends AbstractPortalController {

    @Autowired
    private ApiDocSchemeService apiDocSchemeService;

    @Autowired
    private ApiDocSchemeServiceService apiDocSchemeServiceService;

    @Autowired
    private ContentService contentService;

    @Autowired
    private ApiDocProperties apiDocProperties;

    @Autowired
    private ApiDocSchemeDescService apiDocSchemeDescService;

    /**
     * scheme首页（文档中心首页）
     *
     * @param request
     * @param response
     * @param model
     * @return
     */
    @Override
    public String index(HttpServletRequest request, HttpServletResponse response, Model model) {
        model.addAllAttributes(referenceData(request));
        model.addAttribute("gateway", apiDocProperties.getGateway());
        model.addAttribute("testGateway", apiDocProperties.getTestGateway());
        return "/docs/apidoc/index";
    }

    /**
     * apidoc 菜单
     */
    @RequestMapping("menu")
    public String menu(HttpServletRequest request, HttpServletResponse response, Model model) {
        try {
            doLoadSchemeMenus(request, response, model);
            model.addAttribute("page", request.getParameter("page"));
            model.addAllAttributes(referenceData(request));
        } catch (Exception e) {
            handleException("", e, request);
        }
        return "/docs/apidoc/menu";
    }

    /**
     * ApiDoc 解决方案列表
     *
     * @param request
     * @param response
     * @param model
     * @return
     */
    @RequestMapping("scheme")
    public String scheme(HttpServletRequest request, HttpServletResponse response, Model model) {
        try {
            doSchemeApis(request, model);
            model.addAllAttributes(referenceData(request));
        } catch (Exception e) {
            handleException("", e, request);
        }
        return "/docs/apidoc/scheme";
    }

    @RequestMapping("content")
    public String content(HttpServletRequest request, HttpServletResponse response, Model model) {
        try {
            long id = Long.parseLong(request.getParameter("schemeId"));
            ApiDocScheme apiScheme = apiDocSchemeService.get(id);
            Content content = contentService.get(Long.parseLong(request.getParameter("id")));
            model.addAttribute("apiScheme", apiScheme);
            model.addAttribute("content", content);
            model.addAllAttributes(referenceData(request));
        } catch (Exception e) {
            handleException("", e, request);
        }
        return "/docs/apidoc/apischemeContent";
    }

    /**
     * openapi开发规范
     */
    @RequestMapping("spec")
    public String spec(HttpServletRequest request, HttpServletResponse response, Model model) {
        model.addAllAttributes(referenceData(request));
        return "/docs/apidoc/spec";
    }


    protected void doLoadSchemeMenus(HttpServletRequest request, HttpServletResponse response, Model model) {
        String schemeType = Servlets.getParameter("schemeType");
        String category = Servlets.getParameter("category");
        List<ApiDocSchemeDto> schemes = loadSchemeList(request, schemeType, category);
        List<ApiDocSchemeDto> showSchemes = Lists.newArrayList();
        if (!apiDocProperties.isDefaultSchemeShow()) {
            for (ApiDocSchemeDto s : schemes) {
                if (s.getSchemeTypeEnum() != SchemeTypeEnum.common) {
                    showSchemes.add(s);
                }
            }
        } else {
            showSchemes.addAll(schemes);
        }
        model.addAttribute("showSchemes", showSchemes);
    }


    protected List<ApiDocSchemeDto> loadSchemeList(HttpServletRequest request, String schemeType, String category) {
        List<ApiDocScheme> schemes = apiDocSchemeService.findBySchemeTypeAndCategory(schemeType, category);
        List<ApiDocSchemeDto> apiDocSchemeDtos = Lists.newArrayList();
        if (Collections3.isNotEmpty(schemes)) {
            apiDocSchemeDtos = JSON.parseArray(JSON.toJSONString(schemes), ApiDocSchemeDto.class);
            // 过滤所有父节点
            apiDocSchemeDtos = apiDocSchemeDtos.stream().filter(item ->
                    item.getSubCount() == null || item.getSubCount().intValue() == 0).collect(Collectors.toList());
        }
        return apiDocSchemeDtos;
    }


    protected void doSchemeApis(HttpServletRequest request, Model model) {
        Long id = Long.parseLong(request.getParameter("schemeId"));
        String key = Servlets.getParameter(request, "key");
        ApiDocScheme apiScheme = null;
        if (Strings.isNotBlank(key)) {
            // 设置为SYSTEM全局SCHEME
            apiScheme = apiDocSchemeService.findBySchemeNo("SYSTEM");
        }else{
            apiScheme = apiDocSchemeService.get(id);
        }
        model.addAttribute("schemeName", apiScheme.getTitle());
        model.addAttribute("schemeId", apiScheme.getId());
        model.addAttribute("apiScheme", apiScheme);
        List<ApiDocService> entities = apiDocSchemeServiceService.searchApiDocServices(apiScheme.getSchemeNo(), key);
        model.addAttribute("apis", entities);
        model.addAttribute("key", key);

        ApiDocSchemeDesc apiDocSchemeDesc = apiDocSchemeDescService.findBySchemeNo(apiScheme.getSchemeNo());
        if (apiDocSchemeDesc != null && Strings.isNotBlank(apiDocSchemeDesc.getSchemeDesc())) {
            apiDocSchemeDesc.setSchemeDesc(HtmlUtils.htmlUnescape(apiDocSchemeDesc.getSchemeDesc()));
            model.addAttribute("apiSchemeDesc", apiDocSchemeDesc);
        }
    }
}
