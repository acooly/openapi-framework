/*
 * www.acooly.cn Inc.
 * Copyright (c) 2017 All Rights Reserved
 */

/*
 * 修订记录:
 * zhangpu@acooly.cn 2017-12-11 00:07 创建
 */
package com.acooly.openapi.apidoc.utils;

/**
 * @author zhangpu 2017-12-11 00:07
 */
public class ApiDocs {

    private static final String API_DOC_COMMON_SPLIT_CHAR = "_";

    public static String getServiceNo(String serviceName, String serviceVersion) {
        return serviceName + API_DOC_COMMON_SPLIT_CHAR + serviceVersion;
    }


}
