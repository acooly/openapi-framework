/*
 * acooly.cn Inc.
 * Copyright (c) 2016 All Rights Reserved.
 * create by acooly
 * date:2016-07-16
 *
 */
package com.acooly.openapi.framework.service.domain;

import com.acooly.core.common.domain.AbstractEntity;
import com.acooly.openapi.framework.common.enums.SecretType;
import com.acooly.openapi.framework.common.enums.SignType;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;

/**
 * 合作方管理 Entity
 *
 * @author acooly Date: 2016-07-16 02:05:01
 */
@Entity
@Table(name = "api_partner")
@Getter
@Setter
public class ApiPartner extends AbstractEntity {
  /** 合作方编码 */
  private String partnerId;
  /** 合作方名称 */
  private String partnerName;
  /** 安全方案 */
  @Enumerated(EnumType.STRING)
  private SecretType secretType;
  /** 签名类型 */
  @Enumerated(EnumType.STRING)
  private SignType signType;
  /** 秘钥 */
  private String secretKey;
  /** 备注 */
  private String comments;
}
