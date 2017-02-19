/*
 * acooly.cn Inc.
 * Copyright (c) 2016 All Rights Reserved.
 * create by zhangpu 
 * date:2016年3月17日
 *
 */
package com.yiji.framework.openapi.service;

import com.acooly.core.common.dao.support.PageInfo;
import com.yiji.framework.openapi.domain.OrderInfo;

import java.util.Map;

/**
 * @author zhangpu
 * @date 2014年7月27日
 */
public interface OrderInfoService {

    void insert(OrderInfo orderInfo);

    void checkUnique(String partnerId, String orderNo);

    @Deprecated
    OrderInfo findByGid(String gid);

    OrderInfo findByGid(String gid, String partnerId);

    String findGidByTrade(String partnerId, String service, String version, String merchOrderNo);

    PageInfo<OrderInfo> query(PageInfo<OrderInfo> pageInfo, Map<String, Object> map, Map<String, Boolean> orderMap);
}
