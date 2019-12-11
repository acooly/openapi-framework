/*
 * www.acooly.cn Inc.
 * Copyright (c) 2017 All Rights Reserved
 */

/*
 * 修订记录:
 * zhangpu@acooly.cn 2017-10-11 00:00 创建
 */
package com.acooly.openapi.apidoc.portal;

import com.acooly.core.common.web.support.JsonEntityResult;
import com.acooly.core.common.web.support.JsonListResult;
import com.acooly.core.utils.Collections3;
import com.acooly.core.utils.Strings;
import com.acooly.core.utils.mapper.BeanCopier;
import com.acooly.module.cms.domain.Content;
import com.acooly.module.cms.service.ContentService;
import com.acooly.openapi.apidoc.ApiDocProperties;
import com.acooly.openapi.apidoc.enums.SchemeTypeEnum;
import com.acooly.openapi.apidoc.persist.dto.ApiDocSchemeDto;
import com.acooly.openapi.apidoc.persist.entity.ApiDocScheme;
import com.acooly.openapi.apidoc.persist.entity.ApiDocSchemeDesc;
import com.acooly.openapi.apidoc.persist.entity.ApiDocService;
import com.acooly.openapi.apidoc.persist.service.ApiDocSchemeDescService;
import com.acooly.openapi.apidoc.persist.service.ApiDocSchemeService;
import com.acooly.openapi.apidoc.persist.service.ApiDocSchemeServiceService;
import com.acooly.openapi.apidoc.portal.dto.SchemeDto;
import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.util.HtmlUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

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
        List<SchemeDto> schemes = loadSchemeList(request, null);
        List<SchemeDto> showSchemes = Lists.newArrayList();
        if (!apiDocProperties.isDefaultSchemeShow()) {
            for (SchemeDto s : schemes) {
                if (s.getSchemeTypeEnum() != SchemeTypeEnum.common) {
                    showSchemes.add(s);
                }
            }
        } else {
            showSchemes.addAll(schemes);
        }
        model.addAttribute("showSchemes", showSchemes);
    }


    protected List<SchemeDto> loadSchemeList(HttpServletRequest request, SchemeTypeEnum schemeGroupEnum) {
        List<ApiDocScheme> schemes = apiDocSchemeService.findBySchemeType(schemeGroupEnum);
        List<SchemeDto> schemeDtos = Lists.newArrayList();
        if (Collections3.isNotEmpty(schemes)) {
            SchemeDto schemeDto = null;
            List<Content> contents = null;
            for (ApiDocScheme scheme : schemes) {
                schemeDto = new SchemeDto(scheme.getId(), scheme.getTitle(), scheme.getSchemeNo());
                schemeDto.setSchemeTypeEnum(scheme.getSchemeType());
                contents = contentService.topByTypeCode(scheme.getSchemeNo(), 100);
                if (Collections3.isNotEmpty(contents)) {
                    for (Content content : contents) {
                        schemeDto.put(content.getId(), content.getTitle());
                    }
                }
                schemeDtos.add(schemeDto);
            }
        }
        return schemeDtos;
    }


    protected void doSchemeApis(HttpServletRequest request, Model model) {
        long id = Long.parseLong(request.getParameter("schemeId"));
        ApiDocScheme apiScheme = apiDocSchemeService.get(id);
        List<ApiDocService> entities = apiDocSchemeServiceService.findSchemeApiDocServices(apiScheme.getSchemeNo());
        model.addAttribute("apis", entities);
        model.addAttribute("schemeName", apiScheme.getTitle());
        model.addAttribute("schemeId", id);
        model.addAttribute("apiScheme", apiScheme);


        ApiDocSchemeDesc apiDocSchemeDesc = apiDocSchemeDescService.findBySchemeNo(apiScheme.getSchemeNo());
        if (apiDocSchemeDesc != null && Strings.isNotBlank(apiDocSchemeDesc.getSchemeDesc())) {
            apiDocSchemeDesc.setSchemeDesc(HtmlUtils.htmlUnescape(apiDocSchemeDesc.getSchemeDesc()));
            model.addAttribute("apiSchemeDesc", apiDocSchemeDesc);
        }
    }

    /**
     * 目录列表
     */
    @ResponseBody
    @GetMapping(value = {"/{category}/{id}", "/{category}"})
    public JsonListResult<ApiDocSchemeDto> catalogList(@PathVariable(value = "category", required = true) String category,
                                                       @PathVariable(value = "id", required = false) Long id,
                                                       HttpServletRequest request, HttpServletResponse response, Model model) {
        JsonListResult<ApiDocSchemeDto> result = new JsonListResult<ApiDocSchemeDto>();
        List<ApiDocScheme> list = apiDocSchemeService.tree(category, id);
        // 使用json转换entity为dto，解决深copy的问题
        if (Collections3.isNotEmpty(list)) {
            List<ApiDocSchemeDto> resultList = JSON.parseArray(JSON.toJSONString(list), ApiDocSchemeDto.class);
            result.setRows(resultList);
            result.setTotal(Long.valueOf(resultList.size()));
        }
        return result;
    }

    /**
     * 内容详情
     */
    @ResponseBody
    @GetMapping(value = {"/content/{id}"})
    public JsonEntityResult contentDetail(@PathVariable(value = "id", required = true) Long id,
                                          HttpServletRequest request, HttpServletResponse response, Model model) {
        JsonEntityResult<ApiDocSchemeDto> result = new JsonEntityResult<ApiDocSchemeDto>();
        ApiDocScheme apiDocScheme = apiDocSchemeService.get(id);
        ApiDocSchemeDesc apiDocSchemeDesc = apiDocSchemeDescService.get(id);
        if (apiDocScheme == null || apiDocSchemeDesc == null) {
            result.setSuccess(false);
            result.setMessage("内容不存在");
        }
        ApiDocSchemeDto dto = new ApiDocSchemeDto();
        BeanCopier.copy(apiDocScheme, dto);
        dto.setContent(apiDocSchemeDesc.getSchemeDesc());
        result.setEntity(dto);
        return result;
    }
}
