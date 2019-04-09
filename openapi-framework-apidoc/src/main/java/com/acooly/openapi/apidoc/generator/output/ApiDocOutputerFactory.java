/*
 * www.yiji.com Inc.
 * Copyright (c) 2015 All Rights Reserved.
 */

package com.acooly.openapi.apidoc.generator.output;

import com.acooly.openapi.apidoc.generator.ApiDocModule;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Set;

/**
 * Created by zhangpu on 2015/2/26.
 */
@Slf4j
@Component
public class ApiDocOutputerFactory implements ApplicationContextAware, InitializingBean {

    private ApplicationContext applicationContext;

    private Map<ApiDocModule, Set<ApiDocOutputer>> registers = Maps.newHashMap();

    public Map<ApiDocModule, Set<ApiDocOutputer>> getOutputers() {
        return registers;
    }

    public Set<ApiDocOutputer> getOutputers(ApiDocModule apiDocModule) {
        return registers.get(apiDocModule);
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
        Map<String, ApiDocOutputer> outputers = applicationContext.getBeansOfType(ApiDocOutputer.class);
        if (outputers == null || outputers.isEmpty()) {
            return;
        }
        for (ApiDocOutputer apiDocOutputer : outputers.values()) {
            if (registers.get(apiDocOutputer.getModule()) == null) {
                registers.put(apiDocOutputer.getModule(), Sets.newHashSet());
            }
            registers.get(apiDocOutputer.getModule()).add(apiDocOutputer);
        }
        log.debug("Registed Ouputers:" + registers);
    }


}
