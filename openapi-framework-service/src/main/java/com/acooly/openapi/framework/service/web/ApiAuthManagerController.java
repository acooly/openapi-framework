/*
 * acooly.cn Inc.
 * Copyright (c) 2018 All Rights Reserved.
 * create by qiubo
 * date:2018-08-21
 */
package com.acooly.openapi.framework.service.web;

import com.acooly.core.common.dao.support.PageInfo;
import com.acooly.core.common.view.ViewResult;
import com.acooly.core.common.web.AbstractJsonEntityController;
import com.acooly.core.common.web.support.JsonListResult;
import com.acooly.core.common.web.support.JsonResult;
import com.acooly.core.utils.Collections3;
import com.acooly.core.utils.Servlets;
import com.acooly.core.utils.Strings;
import com.acooly.core.utils.enums.SimpleStatus;
import com.acooly.core.utils.enums.WhetherStatus;
import com.acooly.openapi.framework.common.enums.SecretType;
import com.acooly.openapi.framework.common.enums.SignType;
import com.acooly.openapi.framework.common.utils.AccessKeys;
import com.acooly.openapi.framework.service.domain.ApiAuth;
import com.acooly.openapi.framework.service.domain.ApiAuthAcl;
import com.acooly.openapi.framework.service.domain.ApiMetaService;
import com.acooly.openapi.framework.service.domain.ApiPartner;
import com.acooly.openapi.framework.service.service.ApiAuthAclService;
import com.acooly.openapi.framework.service.service.ApiAuthService;
import com.acooly.openapi.framework.service.service.ApiMetaServiceService;
import com.acooly.openapi.framework.service.service.ApiPartnerService;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 认证授权信息管理 管理控制器
 *
 * @author zhangpu
 * @date 2020-2-8
 */
@Controller
@RequestMapping(value = "/manage/openapi/apiAuth")
public class ApiAuthManagerController extends AbstractJsonEntityController<ApiAuth, ApiAuthService> {

    @Autowired
    private ApiAuthService apiAuthService;
    @Autowired
    private ApiMetaServiceService apiMetaServiceService;
    @Autowired
    private ApiAuthAclService apiAuthAclService;
    @Autowired
    private ApiPartnerService apiPartnerService;

    @RequestMapping(value = "loadLevel")
    @ResponseBody
    public JsonListResult<ApiAuth> loadLevel(HttpServletRequest request) {
        JsonListResult<ApiAuth> result = new JsonListResult();
        try {
            result.appendData(referenceData(request));
            Long parentId = Servlets.getLongParameter("id");
            String serviceCode = Servlets.getParameter("serviceCode");
            Map<String, Object> map = getSearchParams(request);
            if (parentId == null) {
                map.put("NULL_parentId", 0L);
            } else {
                map.put("EQ_parentId", parentId);
            }
            PageInfo<ApiAuth> pageInfo = apiAuthService.query(getPageInfo(request), map, getSortMap(request), serviceCode);
            result.setTotal(pageInfo.getTotalCount());
            result.setRows(pageInfo.getPageResults());
            result.setHasNext(pageInfo.hasNext());
            result.setPageNo(pageInfo.getCurrentPage());
            result.setPageSize(pageInfo.getCountOfCurrentPage());
        } catch (Exception e) {
            handleException(result, "列表查询", e);
        }
        return result;
    }

    @RequestMapping(value = "loadAcls")
    @ResponseBody
    public JsonListResult<ApiAuthAcl> loadAcls(HttpServletRequest request, HttpServletResponse response) {
        JsonListResult<ApiAuthAcl> result = new JsonListResult<>();
        try {
            String authNo = Servlets.getParameter(request, "authNo");
            List<ApiAuthAcl> acls = apiAuthAclService.loadAcls(authNo);
            result.setRows(acls);
        } catch (Exception e) {
            handleException(result, "加载ACL", e);
        }
        return result;
    }

    @RequestMapping(value = "loadMetaServices")
    @ResponseBody
    public JsonListResult<ApiMetaService> loadMetaServices(HttpServletRequest request, HttpServletResponse response) {
        JsonListResult<ApiMetaService> result = new JsonListResult<>();
        try {
            String authNo = Servlets.getParameter(request, "authNo");
            List<ApiMetaService> acls = apiAuthAclService.loadMetaServices(authNo);
            result.setRows(acls);
        } catch (Exception e) {
            handleException(result, "加载MetaServices", e);
        }
        return result;
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
        return "/manage/openapi/apiAuthSetting";
    }

    @RequestMapping(value = "settingSave")
    @ResponseBody
    public JsonResult settingSave(HttpServletRequest request, @Valid String authNo) {
        JsonResult result = new JsonResult();
        try {
            ApiAuth apiAuth = apiAuthService.findByAuthNo(authNo);
            String[] serviceNos = Strings.split(Servlets.getParameter("serviceNo"), ",");
            List<ApiAuthAcl> acls = Lists.newArrayList();
            ApiAuthAcl apiAuthAcl = null;
            String[] services = null;
            for (String serviceNo : serviceNos) {
                apiAuthAcl = new ApiAuthAcl();
                services = Strings.split(serviceNo, "_");
                apiAuthAcl.setAccessKey(apiAuth.getAccessKey());
                apiAuthAcl.setAuthNo(authNo);
                apiAuthAcl.setServiceNo(serviceNo);
                apiAuthAcl.setName(services[0]);
                apiAuthAcl.setVersion(services[1]);
                acls.add(apiAuthAcl);
            }
            apiAuthAclService.merge(acls);
            result.setMessage("设置ACL权限成功");
        } catch (Exception e) {
            handleException(result, "设置ACL权限", e);
        }
        return result;
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

    @RequestMapping(value = "getSignTypes")
    @ResponseBody
    public JsonResult getSignTypes(HttpServletRequest request) {
        JsonResult result = new JsonResult();
        String secretType = Servlets.getParameter(request, "secretType");
        if (Strings.isBlank(secretType)) {
            secretType = SecretType.digest.code();
        }
        List<SignType> signTypes = SecretType.find(secretType).getSignTypes();
        result.appendData(Collections3.extractToMap(signTypes, "code", "message"));
        return result;
    }


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

    @Override
    protected void referenceData(HttpServletRequest request, Map<String, Object> model) {
        model.put("allSecretTypes", SecretType.mapping());
        model.put("allSignTypes", SignType.mapping());
        model.put("allPartners", getAllPartner());
        model.put("allStatuss", SimpleStatus.mapping());
        model.put("allWhitelistChecks", WhetherStatus.mapping());
    }

    /**
     * 获取所有的接入方，便于在前端展示接入方名称
     *
     * @return
     */
    private Map<String, String> getAllPartner() {
        List<ApiPartner> partnerList = apiPartnerService.getAll();
        if (partnerList == null || partnerList.size() == 0) {
            return null;
        }

        Map<String, String> partnerMap = new HashMap<>(partnerList.size());

        partnerList.forEach(apiPartner -> {
            partnerMap.put(apiPartner.getPartnerId(), apiPartner.getPartnerName());
        });

        return partnerMap;
    }
}
