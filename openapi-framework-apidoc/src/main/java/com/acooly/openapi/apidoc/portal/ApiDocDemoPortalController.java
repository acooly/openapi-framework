/*
 * www.acooly.cn Inc.
 * Copyright (c) 2017 All Rights Reserved
 */

/*
 * 修订记录:
 * zhangpu@acooly.cn 2017-08-01 创建
 */
package com.acooly.openapi.apidoc.portal;

import com.acooly.core.common.web.support.JsonListResult;
import com.acooly.core.utils.Strings;
import com.acooly.module.safety.signature.SignTypeEnum;
import com.acooly.openapi.apidoc.builder.ApiDocMessageBuilder;
import com.acooly.openapi.apidoc.builder.ApiDocMessageContext;
import com.acooly.openapi.apidoc.persist.entity.ApiDocService;
import com.acooly.openapi.apidoc.persist.service.ApiDocServiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 文档中心 控制器
 *
 * @author zhangpu
 */
@Controller
@RequestMapping("/docs/apidoc/demo")
public class ApiDocDemoPortalController extends AbstractPortalController {

    @Autowired
    private ApiDocServiceService apiDocServiceService;
    @Autowired
    private ApiDocMessageBuilder apiDocMessageBuilder;


    /**
     * 获取Demo报文
     */
    @RequestMapping("message")
    @ResponseBody
    public JsonListResult<ApiDocMessageContext> message(HttpServletRequest request, HttpServletResponse response) {
        JsonListResult<ApiDocMessageContext> result = new JsonListResult<ApiDocMessageContext>();
        try {
            String id = request.getParameter("id");
            ApiDocService apiServiceDoc = apiDocServiceService.loadApiDocService(Long.valueOf(id));
            String signType = Strings.isBlankDefault(request.getParameter("signType"), SignTypeEnum.MD5Hex.name());
            List<ApiDocMessageContext> messages = apiDocMessageBuilder.build(apiServiceDoc, signType);
            result.setRows(messages);
            result.appendData(referenceData(request));
        } catch (Exception e) {
            handleException("获取服务示例报文", e, request);
        }
        return result;
    }


}
