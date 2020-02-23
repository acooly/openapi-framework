/**
 * openapi-framework
 * <p>
 * Copyright 2014 Acooly.cn, Inc. All rights reserved.
 *
 * @author zhangpu
 * @date 2020-02-21 14:08
 */
package com.acooly.openapi.framework.common.utils;

import com.acooly.core.utils.Strings;
import com.acooly.openapi.framework.common.enums.ApiServiceResultCode;
import com.acooly.openapi.framework.common.exception.ApiServiceException;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.validator.routines.UrlValidator;

import java.util.Map;

/**
 * @author zhangpu
 * @date 2020-02-21 14:08
 */
@Slf4j
public class ApiServerUtils {

    private static UrlValidator httpUrlValidator = null;

    static {
        String[] schemes = {"http", "https"};
        httpUrlValidator = new UrlValidator(schemes);
    }

    public static boolean isHttpUrl(String str) {
        return httpUrlValidator.isValid(str);
    }


    public static void checkOpenAPIUrl(String str, String name) {
        if (Strings.isBlank(str)) {
            throw new ApiServiceException(ApiServiceResultCode.PARAMETER_ERROR, name + "不能为空");
        }
        if (isHttpUrl(str)) {
            if (str.contains("?")) {
                throw new ApiServiceException(
                        ApiServiceResultCode.PARAMETER_ERROR, "必须传入格式正确的" + name + "参数,请求参数不能包含?");
            }
        } else {
            throw new ApiServiceException(
                    ApiServiceResultCode.PARAMETER_ERROR, "必须传入格式正确的" + name + "参数");
        }
    }

    public static Map parseJsonBody(String jsonBody) {
        try {
            return (JSONObject) JSON.parse(jsonBody);
        } catch (Exception e) {
            throw new ApiServiceException(ApiServiceResultCode.JSON_BODY_PARSING_FAILED);
        }
    }
}
