/*
 * www.yiji.com Inc.
 * Copyright (c) 2015 All Rights Reserved.
 */

package com.acooly.openapi.apidoc.generator.output.impl;

import java.util.List;

import com.acooly.openapi.apidoc.generator.output.ApiDocOutputer;
import com.acooly.openapi.apidoc.generator.output.ApiOutputerTypeEnum;
import com.acooly.openapi.apidoc.ApiDocContext;
import com.acooly.openapi.apidoc.persist.entity.ApiDocService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import org.springframework.stereotype.Component;

/**
 * 控制台输出实现
 * <p/>
 * Created by zhangpu on 2015/2/26.
 */
@Component("consoleApiDocOutputer")
public class ConsoleApiDocOutputer implements ApiDocOutputer<String> {
	private static final Logger logger = LoggerFactory.getLogger(ConsoleApiDocOutputer.class);

	@Override
	public String output(List<ApiDocService> apiServiceDocs, ApiDocContext apidocContext) {
		String console = JSON.toJSONString(apiServiceDocs, true);
		logger.info(console);
		return console;
	}


	@Override
	public ApiOutputerTypeEnum getType() {
		return ApiOutputerTypeEnum.console;
	}
}
