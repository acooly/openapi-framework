/*
 * www.yiji.com Inc.
 * Copyright (c) 2015 All Rights Reserved.
 */

package com.acooly.openapi.apidoc.output.impl;

import java.util.List;

import com.acooly.openapi.apidoc.output.ApiDocumentOutputer;
import com.acooly.openapi.apidoc.ApiDocContext;
import com.acooly.openapi.apidoc.output.ApiOutputerEnum;
import com.acooly.openapi.apidoc.persist.entity.ApiDocService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;

/**
 * JDBC输出实现
 * <p/>
 * Created by zhangpu on 2015/2/26.
 */
public class ApiDocumentConsoleOutputer implements ApiDocumentOutputer<String> {
	private static final Logger logger = LoggerFactory.getLogger(ApiDocumentConsoleOutputer.class);

	@Override
	public String output(List<ApiDocService> apiServiceDocs, ApiDocContext apidocContext) {
		String console = JSON.toJSONString(apiServiceDocs, true);
		logger.info(console);
		return console;
	}

	@Override
	public String getName() {
		return ApiOutputerEnum.Console.name();
	}
}
