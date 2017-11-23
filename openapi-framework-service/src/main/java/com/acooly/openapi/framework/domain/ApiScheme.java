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

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 服务方案 Entity
 *
 * @author acooly Date: 2016-07-16 01:57:25
 */
@Entity
@Table(name = "api_scheme")
@Getter
@Setter
public class ApiScheme extends AbstractEntity {
  /** name */
  private String name;
  /** 备注 */
  private String comments;
}
