/*
 * acooly.cn Inc.
 * Copyright (c) 2016 All Rights Reserved.
 * create by zhangpu 
 * date:2016年3月17日
 *
 */
package com.yiji.framework.openapi.service.persistent;

import com.acooly.core.common.dao.support.PageInfo;
import com.yiji.framework.openapi.common.enums.ApiServiceResultCode;
import com.yiji.framework.openapi.common.exception.ApiServiceException;
import com.yiji.framework.openapi.domain.OrderInfo;
import com.yiji.framework.openapi.service.OrderInfoService;
import com.yiji.framework.openapi.service.persistent.dao.OrderInfoDao;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * @author zhangpu
 * @date 2014年7月27日
 */
@Service("orderInfoService")
public class OrderInfoServiceImpl implements OrderInfoService {
    @Autowired
    private OrderInfoDao orderInfoDao;

    @Override
    public PageInfo<OrderInfo> query(PageInfo<OrderInfo> pageInfo, Map<String, Object> map,
                                     Map<String, Boolean> orderMap) {
        return orderInfoDao.query(pageInfo, map, orderMap);
    }

    @Transactional
    @Override
    public void insert(OrderInfo orderInfo) {
        try {
            orderInfoDao.insert(orderInfo);
        } catch (Exception e) {
            throw new ApiServiceException(ApiServiceResultCode.INTERNAL_ERROR, "写入订单到数据库失败:" + e.getMessage());
        }
    }

    @Override
    public void checkUnique(String partnerId, String orderNo) {
        if (orderInfoDao.countByPartnerIdAndOrderNo(partnerId, orderNo) > 0) {
            throw new ApiServiceException(ApiServiceResultCode.REQUEST_NO_NOT_UNIQUE, "{requestNo:" + orderNo + "}");
        }
    }

    @Override
    public OrderInfo findByGid(String gid) {
        return findByGid(gid, null);
    }

    @Override
    public OrderInfo findByGid(String gid, String partnerId) {
        try {
            List<OrderInfo> orderInfos = orderInfoDao.findByGid(partnerId, gid);
            if (orderInfos == null || orderInfos.size() == 0) {
                return null;
            }
            if (orderInfos.size() == 1) {
                return orderInfos.iterator().next();
            }

            for (OrderInfo orderInfo : orderInfos) {
                if (StringUtils.isNotBlank(orderInfo.getNotifyUrl())) {
                    return orderInfo;
                }
            }
            return null;
        } catch (Exception e) {
            throw new ApiServiceException(ApiServiceResultCode.INTERNAL_ERROR, "GID对应的请求订单失败:" + e.getMessage());
        }
    }

    @Override
    public String findGidByTrade(String partnerId, String service, String version, String merchOrderNo) {
        try {
            List<OrderInfo> orderInfos = orderInfoDao.findGidByTrade(partnerId, service, version, merchOrderNo);
            if (orderInfos == null || orderInfos.size() == 0) {
                return null;
            }
            return orderInfos.iterator().next().getGid();
        } catch (Exception e) {
            throw new ApiServiceException(ApiServiceResultCode.INTERNAL_ERROR, "交易对应的GID查詢:" + e.getMessage());
        }
    }
}
