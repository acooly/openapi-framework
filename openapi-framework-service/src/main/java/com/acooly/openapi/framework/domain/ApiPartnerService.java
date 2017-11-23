/*
 * acooly.cn Inc.
 * Copyright (c) 2016 All Rights Reserved.
 * create by acooly
 * date:2016-07-28
 *
 */
package com.acooly.openapi.framework.domain;

import com.acooly.core.common.domain.AbstractEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * api_partner_service Entity
 *
 * @author acooly Date: 2016-07-28 15:33:42
 */
@Entity
@Table(name = "api_partner_service")
@Getter
@Setter
public class ApiPartnerService extends AbstractEntity {
  /** 接入方编码 */
  private String partnerId;
  /** 接入方名称 */
  private String parnerName;
  /** 接入方主键 */
  private Long apipartnerid;
  /** 服务主键 */
  private Long apiserviceid;
  /** 服务名称 */
  private String serviceName;
  /** 服务版本 */
  private String serviceVersion;
  /** 服务中文名 */
  private String serviceTitle;
  /** 备注 */
  private String comments;

}
