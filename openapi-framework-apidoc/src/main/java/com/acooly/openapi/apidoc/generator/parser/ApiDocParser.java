/*
 * www.yiji.com Inc.
 * Copyright (c) 2015 All Rights Reserved.
 */

package com.acooly.openapi.apidoc.generator.parser;



import com.acooly.openapi.apidoc.persist.entity.ApiDocService;

import java.util.List;

/**
 * API文档自动生成 接口定义
 *
 * @author zhangpu
 */
public interface ApiDocParser {

    List<ApiDocService> parse();

}
