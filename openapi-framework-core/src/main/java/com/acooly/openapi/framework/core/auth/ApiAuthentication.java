/*
 * acooly.com Inc.
 * Copyright (c) 2014 All Rights Reserved
 */
package com.acooly.openapi.framework.core.auth;

import com.acooly.openapi.framework.common.context.ApiContext;
import com.acooly.openapi.framework.common.enums.SignTypeEnum;

import java.util.Map;

/**
 * Api Service 认证接口
 *
 * @author zhangpu
 * @date 2014年5月17日
 */
public interface ApiAuthentication {

    void authenticate(ApiContext apiContext);

    /**
     * 对Map报文签名
     *
     * @param response
     * @param accessKey
     * @param signType
     * @return
     */
    String signature(Map<String, String> response, String accessKey, String signType);

    /**
     * 对字符串报文签名
     *
     * @param body
     * @param accessKey
     * @param signType
     * @return
     */
    String signature(String body, String accessKey, String signType);

    /**
     * 对字符串报文验签
     *
     * @param body
     * @param accessKey
     * @param signType
     * @param verifySign
     */
    void verify(String body, String accessKey, SignTypeEnum signType, String verifySign);

}
