/*
 * acooly.cn Inc.
 * Copyright (c) 2016 All Rights Reserved.
 * create by zhangpu
 * date:2016年3月17日
 *
 */
package com.acooly.openapi.framework.serviceimpl.persistent;

import com.acooly.core.common.dao.support.PageInfo;
import com.acooly.openapi.framework.common.enums.ApiServiceResultCode;
import com.acooly.openapi.framework.common.exception.ApiServiceException;
import com.acooly.openapi.framework.common.dto.OrderDto;
import com.acooly.openapi.framework.service.OrderInfoService;
import com.acooly.openapi.framework.serviceimpl.persistent.dao.OrderInfoDao;
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
  @Autowired private OrderInfoDao orderInfoDao;

  @Override
  public PageInfo<OrderDto> query(
          PageInfo<OrderDto> pageInfo, Map<String, Object> map, Map<String, Boolean> orderMap) {
    return orderInfoDao.query(pageInfo, map, orderMap);
  }

  @Transactional
  @Override
  public void insert(OrderDto orderInfo) {
    try {
      orderInfoDao.insert(orderInfo);
    } catch (Exception e) {
      throw new ApiServiceException(
          ApiServiceResultCode.INTERNAL_ERROR, "写入订单到数据库失败:" + e.getMessage());
    }
  }

  @Override
  public void checkUnique(String partnerId, String orderNo) {
    if (orderInfoDao.countByPartnerIdAndOrderNo(partnerId, orderNo) > 0) {
      throw new ApiServiceException(
          ApiServiceResultCode.REQUEST_NO_NOT_UNIQUE, "{requestNo:" + orderNo + "}");
    }
  }

  @Override
  public OrderDto findByGid(String gid, String partnerId) {
    try {
      List<OrderDto> orderInfos = orderInfoDao.findByGid(partnerId, gid);
      if (orderInfos == null || orderInfos.size() == 0) {
        return null;
      }
      if (orderInfos.size() == 1) {
        return orderInfos.iterator().next();
      }

      for (OrderDto orderInfo : orderInfos) {
        if (StringUtils.isNotBlank(orderInfo.getNotifyUrl())) {
          return orderInfo;
        }
      }
      return null;
    } catch (Exception e) {
      throw new ApiServiceException(
          ApiServiceResultCode.INTERNAL_ERROR, "GID对应的请求订单失败:" + e.getMessage());
    }
  }

  @Override
  public String findGidByTrade(
      String partnerId, String service, String version, String merchOrderNo) {
    try {
      List<OrderDto> orderInfos =
          orderInfoDao.findGidByTrade(partnerId, service, version, merchOrderNo);
      if (orderInfos == null || orderInfos.size() == 0) {
        return null;
      }
      return orderInfos.iterator().next().getGid();
    } catch (Exception e) {
      throw new ApiServiceException(
          ApiServiceResultCode.INTERNAL_ERROR, "交易对应的GID查詢:" + e.getMessage());
    }
  }
}
