package com.acooly.openapi.framework.service.service.impl;

import com.acooly.core.common.dao.support.PageInfo;
import com.acooly.openapi.framework.common.dto.OrderDto;
import com.acooly.openapi.framework.service.service.OrderInfoService;

import java.util.Map;

/**
 * @author qiuboboy@qq.com
 * @date 2017-12-08 17:37
 */
public class NothingToDoOrderInfoService implements OrderInfoService {
    @Override
    public void insert(OrderDto orderInfo) {

    }

    @Override
    public void checkUnique(String partnerId, String orderNo) {

    }

    @Override
    public OrderDto findByGid(String gid, String partnerId) {
        return null;
    }

    @Override
    public String findGidByTrade(String partnerId, String service, String version, String merchOrderNo) {
        return null;
    }

    @Override
    public PageInfo<OrderDto> query(PageInfo<OrderDto> pageInfo, Map<String, Object> map, Map<String, Boolean> orderMap) {
        return null;
    }
}
