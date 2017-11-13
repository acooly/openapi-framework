/*
 * acooly.cn Inc.
 * Copyright (c) 2016 All Rights Reserved.
 * create by zhangpu
 * date:2016年3月17日
 *
 */
package com.acooly.openapi.framework.common.dto;

import com.acooly.core.common.facade.InfoBase;
import com.acooly.openapi.framework.common.enums.ApiProtocol;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * 请求订单实体
 *
 * @author zhangpu
 */
@Getter
@Setter
public class OrderDto extends InfoBase {
  private Long id;
  private String gid;
  private String oid;
  private String requestNo;
  /** 商户订单号:merchOrderNo */
  private String orderNo;

  private String partnerId;
  private String service;
  private String version;
  private String signType;
  private String returnUrl;
  private String notifyUrl;
  private String charset;
  private ApiProtocol protocol;
  private String businessInfo;
  private Date rawAddTime;
  private Date rawUpdateTime;

  private String context;
}
