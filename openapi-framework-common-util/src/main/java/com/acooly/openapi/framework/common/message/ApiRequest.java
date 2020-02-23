/**
 * openapi-framework
 * <p>
 * Copyright 2014 Acooly.cn, Inc. All rights reserved.
 *
 * @author zhangpu
 * @date 2019-09-16 09:31
 */
package com.acooly.openapi.framework.common.message;

/**
 * 请求报文基类
 *
 * @author zhangpu@acooly.cn
 * @date 2019-09-16
 */
public class ApiRequest extends ApiMessage {


    /**
     * 参数校验,校验失败请抛出RuntimeException
     */
    @Override
    public void check() throws RuntimeException {
    }

}
