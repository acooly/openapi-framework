/*
 * www.yiji.com Inc.
 * Copyright (c) 2015 All Rights Reserved.
 */

package com.acooly.openapi.apidoc.output;

import java.util.Collection;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

import com.google.common.collect.Maps;

/**
 * Created by zhangpu on 2015/2/26.
 */
@Service("apiDocumentOutputerFactory")
public class ApiDocumentOutputerFactory implements ApplicationContextAware, InitializingBean {

	private static final Logger logger = LoggerFactory.getLogger(ApiDocumentOutputerFactory.class);

	private ApplicationContext applicationContext;

	private Map<String, ApiDocumentOutputer> registers = Maps.newHashMap();

	public Collection<ApiDocumentOutputer> getOutputers() {
		return registers.values();
	}

	public ApiDocumentOutputer getOutputer(ApiOutputerEnum apiOutputerEnum) {
		ApiDocumentOutputer outpuer = registers.get(apiOutputerEnum.name());
		if (outpuer == null) {
			throw new RuntimeException("不支持的outpuer实现:" + apiOutputerEnum);
		}
		return outpuer;
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		register();
	}

	protected void register() {
		Map<String, ApiDocumentOutputer> outputers = applicationContext.getBeansOfType(ApiDocumentOutputer.class);
		if (outputers == null || outputers.isEmpty()) {
			return;
		}
		for (Map.Entry<String,ApiDocumentOutputer> entry : outputers.entrySet()) {
			registers.put(entry.getValue().getName(),entry.getValue());
		}
		logger.debug("Registed Ouputers:" + registers);
	}

}
