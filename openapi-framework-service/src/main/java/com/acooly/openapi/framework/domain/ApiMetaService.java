/*
 * acooly.cn Inc.
 * Copyright (c) 2017 All Rights Reserved.
 * create by shuijing
 * date:2017-11-27
 */
package com.acooly.openapi.framework.domain;

import com.acooly.core.common.domain.AbstractEntity;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.Size;

/** @author qiubo */
@Getter
@Setter
@Entity
@Table(name = "api_meta_service")
public class ApiMetaService extends AbstractEntity {
  /** 服务名称 */
  @NotEmpty
  @Size(max = 128)
  private String serviceName;

  /** 版本号 */
  @Size(max = 32)
  private String version;

  /** 服务描述 */
  @Size(max = 128)
  private String serviceDesc;

  /** 服务响应类型 */
  @Size(max = 128)
  private String responseType;

  /** 所属系统 */
  @Size(max = 64)
  private String owner;

  /** 服务名称 */
  @Size(max = 64)
  private String busiType;

  /** 服务介绍 */
  @Size(max = 255)
  private String note;

  /** 服务类 */
  @Size(max = 128)
  private String serviceClass;

  /** 请求类 */
  @Size(max = 128)
  private String requestClass;

  /** 同步响应类 */
  @Size(max = 128)
  private String responseClass;

  /** 异步响应类 */
  @Size(max = 128)
  private String notifyClass;
}
