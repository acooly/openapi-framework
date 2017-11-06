/*
 * acooly.cn Inc.
 * Copyright (c) 2016 All Rights Reserved.
 * create by acooly
 * date:2016-07-16
 *
 */
package com.acooly.openapi.framework.domain;

import com.acooly.core.common.domain.AbstractEntity;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;

/**
 * 服务方案 Entity
 *
 * @author acooly Date: 2016-07-16 01:57:25
 */
@Entity
@Table(name = "api_scheme")
public class ApiScheme extends AbstractEntity {
  /** serialVersionUID */
  private static final long serialVersionUID = 1L;
  /** name */
  private String name;
  /** 创建时间 */
  private Date createTime;
  /** update_time */
  private Date updateTime;
  /** 备注 */
  private String comments;

  public String getName() {
    return this.name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Date getCreateTime() {
    return this.createTime;
  }

  public void setCreateTime(Date createTime) {
    this.createTime = createTime;
  }

  public Date getUpdateTime() {
    return this.updateTime;
  }

  public void setUpdateTime(Date updateTime) {
    this.updateTime = updateTime;
  }

  public String getComments() {
    return this.comments;
  }

  public void setComments(String comments) {
    this.comments = comments;
  }
}
