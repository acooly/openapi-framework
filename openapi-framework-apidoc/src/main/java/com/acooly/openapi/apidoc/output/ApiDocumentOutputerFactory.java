/*
 * www.yiji.com Inc.
 * Copyright (c) 2015 All Rights Reserved.
 */

package com.acooly.openapi.apidoc.output;

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
 * Created by zhangpu on 2015/2/26.
 */
@Slf4j
@Component
public class ApiDocumentOutputerFactory implements ApplicationContextAware, InitializingBean {

    private ApplicationContext applicationContext;

    private Map<String, ApiDocOutputer> registers = Maps.newHashMap();

    public Collection<ApiDocOutputer> getOutputers() {
        return registers.values();
    }

    public ApiDocOutputer getOutputer(ApiOutputerTypeEnum apiOutputerEnum) {
        ApiDocOutputer outpuer = registers.get(apiOutputerEnum.name());
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
        Map<String, ApiDocOutputer> outputers = applicationContext.getBeansOfType(ApiDocOutputer.class);
        if (outputers == null || outputers.isEmpty()) {
            return;
        }
        for (Map.Entry<String, ApiDocOutputer> entry : outputers.entrySet()) {
            registers.put(entry.getValue().getType().name(), entry.getValue());
        }
        log.debug("Registed Ouputers:" + registers);
    }

}
