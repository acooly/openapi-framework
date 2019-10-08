/*
 * www.acooly.cn Inc.
 * Copyright (c) 2017 All Rights Reserved
 */

/*
 * 修订记录:
 * zhangpu@acooly.cn 2017-08-01 创建
 */
package com.acooly.openapi.apidoc.portal;

import com.acooly.core.common.web.support.JsonResult;
import com.acooly.core.utils.Ids;
import com.acooly.core.utils.Servlets;
import com.acooly.core.utils.Strings;
import com.acooly.module.safety.Safes;
import com.acooly.module.safety.signature.SignTypeEnum;
import com.acooly.openapi.apidoc.generator.ApiDocGenerator;
import com.acooly.openapi.apidoc.persist.service.ApiDocServiceService;
import com.acooly.openapi.framework.common.ApiConstants;
import com.alibaba.fastjson.JSON;
import com.github.kevinsawicki.http.HttpRequest;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * 文档中心 控制器
 *
 * @author zhangpu
 */
@Slf4j
@Controller
@RequestMapping("/docs/apidebug")
public class ApiDebugPortalController extends AbstractPortalController {

    public static final String REQUEST_MESSAGE_FIELD_PREFIX = "request_";
    public static final String REQUEST_OBJECT_FIELD_SPLIT = "_";

    @Autowired
    protected ApiDocServiceService apiDocServiceService;

    @Autowired
    private ApiDocGenerator apiDocBuilder;

    @Override
    public String index(HttpServletRequest request, HttpServletResponse response, Model model) {
        model.addAttribute("service", request.getParameter("service"));
        model.addAttribute("testAccessKey", ApiConstants.TEST_ACCESS_KEY);
        model.addAttribute("testSecretKey", ApiConstants.TEST_SECRET_KEY);
        return "/docs/apidebug/index";
    }

    /**
     * 生成IDs
     *
     * @return
     */
    @RequestMapping(value = "ids", method = RequestMethod.POST)
    @ResponseBody
    public JsonResult genIds(HttpServletRequest request, HttpServletResponse response) {
        JsonResult result = new JsonResult();
        try {
            result.appendData("requestNo", Ids.getDid("R"));
            result.appendData("merchOrderNo", Ids.getDid("O"));
            result.appendData(referenceData(request));
        } catch (Exception e) {
            handleException("生成单号", e, request);
        }
        return result;
    }


    @RequestMapping(value = "request", method = RequestMethod.POST)
    @ResponseBody
    public JsonResult request(HttpServletRequest request, HttpServletResponse response) {
        JsonResult result = new JsonResult();
        try {
            Map<String, String> data = buildRequestData(request);
            String requestMessage = Servlets.buildQueryString(data);
            result.appendData("requestData", data);
            result.appendData("requestMessage", requestMessage);
            HttpRequest httpRequest = HttpRequest.post(apiDocProperties.getTestGateway()).form(data);
            result.appendData("responseMessage", JSON.toJSONString(JSON.parse(httpRequest.body()), true));

            result.appendData(referenceData(request));
        } catch (Exception e) {
            handleException("同步请求", e, request);
        }
        return result;
    }


    @RequestMapping(value = "redirect", method = RequestMethod.POST)
    @ResponseBody
    public JsonResult redirect(HttpServletRequest request, HttpServletResponse response) {
        JsonResult result = new JsonResult();
        try {

        } catch (Exception e) {
            handleException("生成单号", e, request);
        }
        return result;
    }


    protected Map<String, String> buildRequestData(HttpServletRequest request) {
        Map<String, Object> odata = Servlets.getParametersStartingWith(request, REQUEST_MESSAGE_FIELD_PREFIX);
        Map<String, String> data = Maps.newHashMap();
        Map<String, Map<String, String>> objectDatas = Maps.newHashMap();
        String key = null;
        for (Map.Entry<String, Object> entry : odata.entrySet()) {
            if (entry.getValue() == null) {
                continue;
            }
            if (Strings.contains(entry.getKey(), REQUEST_OBJECT_FIELD_SPLIT)) {
                String[] keys = Strings.split(entry.getKey(), REQUEST_OBJECT_FIELD_SPLIT);
                if (objectDatas.get(keys[0]) == null) {
                    objectDatas.put(keys[0], new HashMap<String, String>());
                }
                objectDatas.get(keys[0]).put(keys[1], entry.getValue().toString());
            } else {
                data.put(entry.getKey(), entry.getValue().toString());
            }
        }

        for (Map.Entry<String, Map<String, String>> entry : objectDatas.entrySet()) {
            data.put(entry.getKey(), JSON.toJSONString(entry.getValue()));
        }

        String partnerId = request.getParameter(ApiConstants.PARTNER_ID);
        String secretKey = request.getParameter("secretKey");
        data.put(ApiConstants.PARTNER_ID, partnerId);
        String sign = Safes.getSigner(SignTypeEnum.MD5Hex).sign(data, secretKey);
        data.put(ApiConstants.SIGN, sign);
        return data;
    }


}
