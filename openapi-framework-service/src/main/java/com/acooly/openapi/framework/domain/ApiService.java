/*
 * acooly.cn Inc.
 * Copyright (c) 2016 All Rights Reserved.
 * create by acooly
 * date:2016-07-16
 *
 */
package com.acooly.openapi.framework.domain;

import com.acooly.core.common.domain.AbstractEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

/**
 * 服务分类 Entity
 *
 * @author acooly Date: 2016-07-16 01:57:14
 */
@Entity
@Table(name = "api_service")
@Getter
@Setter
public class ApiService extends AbstractEntity {
  /** 服务编码 */
  private String code;
  /** 服务名 */
  private String name;
  /** 服务版本 */
  private String version;
  /** 中文描述 */
  private String title;
  /** 备注 */
  private String comments;

  @ManyToOne(
    fetch = FetchType.LAZY,
    cascade = {CascadeType.REFRESH}
  )
  @JoinColumn(name = "type_id")
  private ApiServiceType apiServiceType;
}
