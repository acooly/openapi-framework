/*
 * www.yiji.com Inc.
 * Copyright (c) 2016 All Rights Reserved
 */

/*
 * 修订记录:
 * kuli@yiji.com 2016-12-18 19:24 创建
 */
package com.acooly.openapi.framework.trafficlimiter;

import com.acooly.core.utils.enums.Messageable;
import com.acooly.openapi.framework.common.message.ApiRequest;
import com.acooly.openapi.framework.trafficlimiter.enums.TrafficLimiterType;
import com.acooly.openapi.framework.trafficlimiter.exception.TrafficLimiterException;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * 采用filter链方式整合可能存在的多种流控实现
 *
 * @author acooly
 */
@Service
public class OpenApiTrafficLimiter implements TrafficLimiterFilter, TrafficLimiterFilterChain, InitializingBean {

    private static final Logger logger = LoggerFactory.getLogger(OpenApiTrafficLimiter.class);

    private Map<Messageable, TrafficLimiterFilter> filterChain = Maps.newLinkedHashMap();


    @Autowired
    private ApplicationContext context;

    @Override
    public void evaluate(ApiRequest request) throws TrafficLimiterException {
        for (Map.Entry<Messageable, TrafficLimiterFilter> entry : mapFilters().entrySet()) {
            entry.getValue().evaluate(request);
        }
    }


    @Override
    public TrafficLimiterType getTrafficLimiterType() {
        return null;
    }


    @Override
    public boolean enable() {
        return false;
    }

    @Override
    public void addFilter(TrafficLimiterFilter filter) {
        filterChain.put(filter.getTrafficLimiterType(), filter);
        logger.info("成功注册流控策略: {}", filter);
    }

    @Override
    public List<TrafficLimiterFilter> listFilters() {
        return Lists.newArrayList(filterChain.values());
    }

    @Override
    public Map<Messageable, TrafficLimiterFilter> mapFilters() {
        return filterChain;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        Map<String, TrafficLimiterFilter> beans = context.getBeansOfType(TrafficLimiterFilter.class);
        for (TrafficLimiterFilter trafficLimiterFilter : beans.values()) {
            if (trafficLimiterFilter.enable()) {
                addFilter(trafficLimiterFilter);
            }
        }
    }

}
