/*
 * www.yiji.com Inc.
 * Copyright (c) 2016 All Rights Reserved
 */

/*
 * 修订记录:
 * kuli@yiji.com 2016-12-18 19:17 创建
 */
package com.acooly.openapi.framework.trafficlimiter;

import com.acooly.core.utils.enums.Messageable;
import com.acooly.openapi.framework.common.message.ApiRequest;
import com.acooly.openapi.framework.trafficlimiter.exception.TrafficLimiterException;

/**
 * 流控接口
 *
 * @author acooly
 */
public interface TrafficLimiterFilter {

  void evaluate(ApiRequest request) throws TrafficLimiterException;

  boolean enable();

  Messageable getTrafficLimiterType();
}
