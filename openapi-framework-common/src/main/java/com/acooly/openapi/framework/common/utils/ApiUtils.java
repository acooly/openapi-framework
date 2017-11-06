/*
 * acooly.com Inc.
 * Copyright (c) 2016 All Rights Reserved
 */

/*
 * 修订记录:
 * kuli@yiji.com 2016-06-06 17:55 创建
 */
package com.acooly.openapi.framework.common.utils;

import com.acooly.core.utils.Strings;
import com.acooly.openapi.framework.common.ApiConstants;
import com.acooly.openapi.framework.common.enums.ApiServiceResultCode;
import com.acooly.openapi.framework.common.exception.ApiServiceException;
import org.apache.commons.validator.routines.UrlValidator;

import java.util.Map;

/**
 * openApi 框架专用工具类
 *
 * @author acooly
 */
public final class ApiUtils {

  private static UrlValidator httpUrlValidator = null;

  static {
    String[] schemes = {"http", "https"};
    httpUrlValidator = new UrlValidator(schemes);
  }

  private ApiUtils() {}

  public static String getRequestNo(Map<String, String> requestData) {
    String requestNo = getParameter(requestData, ApiConstants.REQUEST_NO);
    if (Strings.isBlank(requestNo)) {
      requestNo = getParameter(requestData, ApiConstants.ORDER_NO);
    }
    return requestNo;
  }

  public static boolean isHttpUrl(String str) {

    return httpUrlValidator.isValid(str);
  }

  public static void checkOpenAPIUrl(String str, String name) {
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

  public static String getParameter(Map<String, String> requestData, String key) {
    return requestData.get(key);
  }
}
