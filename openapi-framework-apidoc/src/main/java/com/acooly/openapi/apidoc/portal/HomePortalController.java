/*
 * www.yiji.com Inc.
 * Copyright (c) 2017 All Rights Reserved
 */

/*
 * 修订记录:
 * kuli@yiji.com 2017-08-01 00:00 创建
 */
package com.acooly.openapi.apidoc.portal;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 首页 控制器
 *
 * @author acooly
 */
@Controller
@RequestMapping("/docs")
public class HomePortalController extends AbstractPortalController {


    @Override
    public String index(HttpServletRequest request, HttpServletResponse response, Model model) {
        return "/docs/index";
    }

    @RequestMapping("/")
    public String index1(HttpServletRequest request, HttpServletResponse response, Model model) {
        return "/docs/index";
    }

    @RequestMapping("")
    public String index2(HttpServletRequest request, HttpServletResponse response, Model model) {
        return "/docs/index";
    }

    @RequestMapping("common/header")
    public String header(HttpServletRequest request, HttpServletResponse response, Model model) {
        model.addAttribute("logo", apiDocProperties.getPortal().getLogo());
        return "/docs/common/header";
    }

    @RequestMapping("common/footer")
    public String footer(HttpServletRequest request, HttpServletResponse response, Model model) {
        model.addAttribute("copyright", apiDocProperties.getPortal().getCopyright());
        return "/docs/common/footer";
    }

    @RequestMapping("common/meta")
    public String meta(HttpServletRequest request, HttpServletResponse response, Model model) {
        model.addAttribute("title", apiDocProperties.getPortal().getTitle());
        return "/docs/common/meta";
    }

    @RequestMapping("common/include")
    public String include(HttpServletRequest request, HttpServletResponse response, Model model) {
        return "/docs/common/include";
    }
}
