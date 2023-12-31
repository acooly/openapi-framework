/*
 * www.acooly.cn Inc.
 * Copyright (c) 2017 All Rights Reserved
 */

/*
 * 修订记录:
 * zhangpu@acooly.cn 2017-08-17 16:33 创建
 */
package com.acooly.openapi.apidoc.builder;


import com.acooly.openapi.apidoc.persist.entity.ApiDocService;

import java.util.List;

/**
 * @author zhangpu 2017-08-17 16:33
 */
public interface ApiDocMessageBuilder {

    List<ApiDocMessageContext> build(ApiDocService apiDocService, String signType);

}
