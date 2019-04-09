/*
 * acooly.cn Inc.
 * Copyright (c) 2016 All Rights Reserved.
 * create by zhangpu
 * date:2016年3月17日
 *
 */
package com.acooly.openapi.framework.service;

import com.acooly.core.common.dao.support.PageInfo;
import com.acooly.openapi.framework.common.dto.OrderDto;

import java.util.Map;

/**
 * @author zhangpu
 * @date 2014年7月27日
 */
public interface OrderInfoService {

  void insert(OrderDto orderInfo);

  void checkUnique(String partnerId, String orderNo);

  OrderDto findByGid(String gid, String partnerId);

  String findGidByTrade(String partnerId, String service, String version, String merchOrderNo);

  PageInfo<OrderDto> query(
          PageInfo<OrderDto> pageInfo, Map<String, Object> map, Map<String, Boolean> orderMap);
}
