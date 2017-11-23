package com.acooly.openapi.framework.service.persistent.dao;

/*
 * acooly.cn Inc.
 * Copyright (c) 2016 All Rights Reserved.
 * create by zhangpu
 * date:2016年3月17日
 *
 */

import com.acooly.core.common.dao.DynamicPagedQueryDao;
import com.acooly.openapi.framework.common.dto.OrderDto;

import java.util.List;

/**
 * 请求订单DAO
 *
 * @author zhangpu
 */
public interface OrderInfoDao extends DynamicPagedQueryDao<OrderDto> {

  /**
   * 插入新订单
   *
   * @param orderInfo
   * @return
   */
  void insert(OrderDto orderInfo);

  /**
   * 更新订单
   *
   * @param orderInfo
   */
  void update(OrderDto orderInfo);

  /**
   * 根据合作商ID和订单号获取唯一的订单信息
   *
   * @param partnerId
   * @param requestNo
   * @return
   */
  OrderDto findByPartnerIdAndOrderNo(String partnerId, String requestNo);

  /**
   * 根据合作商ID和订单号获取订单条数
   *
   * @param partnerId
   * @param requestNo
   * @return
   */
  int countByPartnerIdAndOrderNo(String partnerId, String requestNo);

  /**
   * 根据GID获取订单
   *
   * @param gid
   * @return
   */
  @Deprecated
  OrderDto findByGid(String gid);

  /**
   * 根据GID获取对应的所有请求
   *
   * @param gid
   * @return
   */
  List<OrderDto> findByGid(String partnerId, String gid);

  List<OrderDto> findGidByTrade(String partnerId, String service, String version, String orderNo);
}
