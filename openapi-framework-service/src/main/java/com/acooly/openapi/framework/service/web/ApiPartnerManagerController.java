/*
 * acooly.cn Inc.
 * Copyright (c) 2018 All Rights Reserved.
 * create by qiubo
 * date:2018-08-21
 */
package com.acooly.openapi.framework.service.web;

import com.acooly.core.common.web.AbstractJsonEntityController;
import com.acooly.openapi.framework.common.enums.ResponseType;
import com.acooly.openapi.framework.service.domain.ApiMetaService;
import com.acooly.openapi.framework.service.domain.ApiPartner;
import com.acooly.openapi.framework.service.service.ApiMetaServiceService;
import com.acooly.openapi.framework.service.service.ApiPartnerService;
import com.acooly.openapi.framework.service.service.tenant.ApiTenantLoaderService;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
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

    @Autowired
    private ApiMetaServiceService apiMetaServiceService;

    @RequestMapping("testdata")
    @ResponseBody
    public Object testdata(HttpServletRequest request, HttpServletResponse response) {

        List<ApiMetaService> asms = Lists.newArrayList();
        for (int i = 1; i <= 100; i++) {
            ApiMetaService ams = new ApiMetaService();

            ams.setServiceName(RandomStringUtils.random(15, true, false) + i);
            ams.setServiceDesc(RandomStringUtils.random(15, true, false) + i);
            ams.setVersion("1.0");
            ams.setOwner("zhangpu");
            ams.setResponseType(i % 2 == 0 ? ResponseType.SYN : ResponseType.ASNY);
            asms.add(ams);
        }
        apiMetaServiceService.saves(asms);

        return "OK";
    }


    @Override
    protected void referenceData(HttpServletRequest request, Map<String, Object> model) {
        model.put("tenants", apiTenantLoaderService.load());
    }
}
