/*
 * www.yiji.com Inc.
 * Copyright (c) 2016 All Rights Reserved
 */

/*
 * 修订记录:
 * kuli@yiji.com 2016-12-21 16:10 创建
 */
package com.acooly.openapi.framework.trafficlimiter.filter;

import com.acooly.core.utils.enums.Messageable;
import com.acooly.openapi.framework.common.message.ApiRequest;
import com.acooly.openapi.framework.trafficlimiter.enums.TrafficLimiterType;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * @author acooly
 */
@Service
public class PartnerAndServiceRedisTrafficLimiterFilter extends AbstractRedisTrafficLimiterFilter {

    @Value("${traficlimiter.partner.service.detault}")
    private int maxCounterPerSecond;

    @Override
    protected String getKey(ApiRequest request) {
        return request.getPartnerId() + request.getService();
    }

    @Override
    protected int getMaxCounterPerSecond() {
        return maxCounterPerSecond;
    }

    @Override
    public Messageable getTrafficLimiterType() {
        return TrafficLimiterType.Partner_Service_Traffic_limit;
    }



}
