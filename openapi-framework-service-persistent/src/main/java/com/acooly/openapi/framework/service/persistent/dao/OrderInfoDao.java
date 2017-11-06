package com.acooly.openapi.framework.service.persistent.dao;

/*
 * acooly.cn Inc.
 * Copyright (c) 2016 All Rights Reserved.
 * create by zhangpu
 * date:2016年3月17日
 *
 */

import com.acooly.core.common.dao.DynamicPagedQueryDao;
import com.acooly.openapi.framework.domain.OrderInfo;

import java.util.List;

/**
 * 请求订单DAO
 *
 * @author zhangpu
 */
public interface OrderInfoDao extends DynamicPagedQueryDao<OrderInfo> {

  /**
   * 插入新订单
   *
   * @param orderInfo
   * @return
   */
  void insert(OrderInfo orderInfo);

  /**
   * 更新订单
   *
   * @param orderInfo
   */
  void update(OrderInfo orderInfo);

  /**
   * 根据合作商ID和订单号获取唯一的订单信息
   *
   * @param partnerId
   * @param orderNo
   * @return
   */
  OrderInfo findByPartnerIdAndOrderNo(String partnerId, String requestNo);

  /**
   * 根据合作商ID和订单号获取订单条数
   *
   * @param partnerId
   * @param orderNo
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
  OrderInfo findByGid(String gid);

  /**
   * 根据GID获取对应的所有请求
   *
   * @param gid
   * @return
   */
  List<OrderInfo> findByGid(String partnerId, String gid);

  List<OrderInfo> findGidByTrade(String partnerId, String service, String version, String orderNo);
}
