/*
 * acooly.cn Inc.
 * Copyright (c) 2018 All Rights Reserved.
 * create by qiubo
 * date:2018-08-21
 */
package com.acooly.openapi.framework.service.web;

import com.acooly.core.common.web.AbstractJsonEntityController;
import com.acooly.openapi.framework.service.domain.ApiPartner;
import com.acooly.openapi.framework.service.service.ApiPartnerService;
import com.acooly.openapi.framework.service.service.tenant.ApiTenantLoaderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * 接入方管理 管理控制器
 *
 * @author zhangpu
 * @date: 2018-08-21 14:31:05
 */
@Controller
@RequestMapping(value = "/manage/openapi/apiPartner")
public class ApiPartnerManagerController extends AbstractJsonEntityController<ApiPartner, ApiPartnerService> {


    {
        allowMapping = "*";
    }

    @Autowired
    private ApiPartnerService apiPartnerService;

    @Autowired
    private ApiTenantLoaderService apiTenantLoaderService;


    @Override
    protected void referenceData(HttpServletRequest request, Map<String, Object> model) {
        model.put("tenants", apiTenantLoaderService.load());
    }
}
