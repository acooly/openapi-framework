/*
 * www.yiji.com Inc.
 * Copyright (c) 2015 All Rights Reserved.
 */

package com.acooly.openapi.apidoc.generator.parser;

import com.acooly.openapi.apidoc.generator.output.ApiDocOutputer;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Map;

/**
 * ApiDoc解析器工厂
 *
 * @author zhangpu
 * @Date 2015/2/26. Created by zhangpu
 * @Date 2019/2/8. refactoring
 */
@Slf4j
@Component
public class ApiDocParserFactory implements ApplicationContextAware, InitializingBean {

    private ApplicationContext applicationContext;

    private Map<String, ApiDocParser> registers = Maps.newHashMap();

    public Collection<ApiDocParser> getApiDocParsers() {
        return registers.values();
    }

    public ApiDocParser getApiDocParser(String parserModule) {
        ApiDocParser parser = registers.get(parserModule);
        if (parser == null) {
            throw new RuntimeException("不支持的parser实现:" + parserModule);
        }
        return parser;
    }


    protected void register() {
        Map<String, ApiDocParser> outputers = applicationContext.getBeansOfType(ApiDocParser.class);
        if (outputers == null || outputers.isEmpty()) {
            return;
        }
        for (Map.Entry<String, ApiDocParser> entry : outputers.entrySet()) {
            registers.put(entry.getValue().getModule().code(), entry.getValue());
        }
        log.debug("Registed ApiDocParser:" + registers);
    }

    /**
     * 获取outputer的key
     *
     * @param apiDocOutputer
     * @return
     */
    private String getApiDocOutputerKey(ApiDocOutputer apiDocOutputer) {
        return apiDocOutputer.getType().code() + "_" + apiDocOutputer.getModule().code();
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        register();
    }

}
