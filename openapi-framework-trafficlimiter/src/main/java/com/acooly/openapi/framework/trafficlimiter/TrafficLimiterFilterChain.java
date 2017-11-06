/*
 * www.yiji.com Inc.
 * Copyright (c) 2016 All Rights Reserved
 */

/*
 * 修订记录:
 * kuli@yiji.com 2016-12-18 19:39 创建
 */
package com.acooly.openapi.framework.trafficlimiter;

import com.acooly.core.utils.enums.Messageable;

import java.util.List;
import java.util.Map;

/**
 * 流控filter链接口
 *
 * @author acooly
 */
public interface TrafficLimiterFilterChain {

  void addFilter(TrafficLimiterFilter filter);

  List<TrafficLimiterFilter> listFilters();

  Map<Messageable, TrafficLimiterFilter> mapFilters();
}
