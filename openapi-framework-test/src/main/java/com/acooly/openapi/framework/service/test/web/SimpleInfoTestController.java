/**
 * openapi-framework
 * <p>
 * Copyright 2014 Acooly.cn, Inc. All rights reserved.
 *
 * @author zhangpu
 * @date 2019-04-14 15:04
 */
package com.acooly.openapi.framework.service.test.web;

import com.acooly.core.common.web.support.JsonEntityResult;
import com.acooly.core.common.web.support.JsonResult;
import com.acooly.core.utils.Servlets;
import com.acooly.openapi.framework.client.OpenApiClient;
import com.acooly.openapi.framework.common.message.ApiRequest;
import com.acooly.openapi.framework.common.message.ApiResponse;
import com.acooly.openapi.framework.common.utils.Ids;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

/**
 * @author zhangpu
 * @date 2019-04-14 15:04
 */
@Slf4j
@Controller
@RequestMapping("/openapi/test/simpleInfo")
public class SimpleInfoTestController {

    @Autowired
    private OpenApiClient openApiClient;

    @RequestMapping("index")
    public String index(HttpServletRequest request) {
        return "/openapi/test/simpleInfo";
    }

    @RequestMapping("send")
    @ResponseBody
    public JsonResult send(HttpServletRequest request) {
        JsonEntityResult result = new JsonEntityResult();
        try {
            ApiRequest apiRequest = new ApiRequest();
            apiRequest.setService("simpleInfo");
            apiRequest.setRequestNo(Ids.getDid());
            apiRequest.setContext(Servlets.getParameter(request, "context"));
            ApiResponse apiResponse = openApiClient.send(apiRequest, ApiResponse.class);
            result.setEntity(apiResponse);
        } catch (Exception e) {
            result.setSuccess(false);
            result.setMessage(e.getMessage());
        }
        return result;
    }

}
