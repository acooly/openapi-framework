/*
 * acooly.cn Inc.
 * Copyright (c) 2016 All Rights Reserved.
 * create by zhangpu 
 * date:2016年3月17日
 *
 */
package com.acooly.openapi.framework.common.convert;

import java.util.Map;

import javax.annotation.PostConstruct;

import com.acooly.openapi.framework.common.convert.converter.DateToStringConverter;
import com.acooly.openapi.framework.common.convert.converter.MoneyToStringConverter;
import com.acooly.openapi.framework.common.convert.converter.StringToDateConverter;
import com.acooly.openapi.framework.common.convert.converter.StringToMoneyConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.convert.support.DefaultConversionService;

import org.springframework.stereotype.Component;

/**
 * 数据类型转换框架
 * 
 * @author zhangpu
 */
@Component
public class ApiServiceConversionService extends DefaultConversionService implements InitializingBean {

    private static Logger logger = LoggerFactory.getLogger(ApiServiceConversionService.class);

    // 静态变量初始化为默认实现,方便测试用例编写,服务器端则通过@PostConstruct重新初始化.
    public static ApiServiceConversionService INSTANCE = new ApiServiceConversionService();

    @Autowired
    protected ApplicationContext applicationContext;

    public ApiServiceConversionService() {
        super();
        addCustomConverters();
    }

    private void addCustomConverters() {
		addConverter(new DateToStringConverter());
		addConverter(new MoneyToStringConverter());
		addConverter(new StringToDateConverter());
		addConverter(new StringToMoneyConverter());
    }


    @PostConstruct
    public void init() {
        INSTANCE = this;
    }


    @SuppressWarnings("rawtypes")
	@Override
    public void afterPropertiesSet() throws Exception {

        Map<String, ApiServiceConverter> apiServiceConverterMap = applicationContext.getBeansOfType(ApiServiceConverter.class);
        if (apiServiceConverterMap == null || apiServiceConverterMap.isEmpty()) {
            return;
        }

        for (ApiServiceConverter converter : apiServiceConverterMap.values()) {
            addConverter(converter);
            logger.info("注册扩展converter: {}",converter.getClass().getName());
        }


    }
}
