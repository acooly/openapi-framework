/*
 * www.acooly.cn Inc.
 * Copyright (c) 2017 All Rights Reserved
 */

/*
 * 修订记录:
 * zhangpu@acooly.cn 2017-08-01 创建
 */
package com.acooly.openapi.apidoc.portal;

import com.acooly.core.common.exception.BusinessException;
import com.acooly.core.common.web.support.JsonListResult;
import com.acooly.core.utils.Strings;
import com.acooly.module.safety.signature.SignTypeEnum;
import com.acooly.openapi.apidoc.builder.ApiDocMessageBuilder;
import com.acooly.openapi.apidoc.builder.ApiDocMessageContext;
import com.acooly.openapi.apidoc.persist.entity.ApiDocService;
import com.acooly.openapi.apidoc.persist.service.ApiDocServiceService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
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
    @RequestMapping("/message")
    @ResponseBody
    @ApiOperation("api-apiDemo报文消息")
    @ApiImplicitParams({@ApiImplicitParam(name = "id", value = "api服务id", required = false, paramType = "query"),
            @ApiImplicitParam(name = "serviceNo", value = "api服务编码", required = false, paramType = "query")})
    public JsonListResult<ApiDocMessageContext> message(String id, String serviceNo, HttpServletRequest request, HttpServletResponse response) {
        JsonListResult<ApiDocMessageContext> result = new JsonListResult<ApiDocMessageContext>();
        try {
            ApiDocService apiServiceDoc = null;
            if (Strings.isNotBlank(id)) {
                apiServiceDoc = apiDocServiceService.loadApiDocService(Long.valueOf(id));
            } else {
                apiServiceDoc = apiDocServiceService.loadApiDocServiceByNo(serviceNo);
            }
            if (apiServiceDoc == null) {
                throw new BusinessException("系统异常，数据不存在!");
            }
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
