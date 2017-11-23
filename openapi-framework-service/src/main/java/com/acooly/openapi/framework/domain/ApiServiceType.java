/*
 * acooly.cn Inc.
 * Copyright (c) 2016 All Rights Reserved.
 * create by acooly
 * date:2016-07-16
 *
 */
package com.acooly.openapi.framework.domain;

import com.acooly.core.common.domain.AbstractEntity;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

/**
 * 服务分类 Entity
 *
 * @author acooly Date: 2016-07-16 01:57:05
 */
@Entity
@Table(name = "api_service_type")
@Getter
@Setter
@JsonIgnoreProperties({"handler", "hibernateLazyInitializer"})
public class ApiServiceType extends AbstractEntity {

  /** 父类型 */
  private Long parentId;

  /** 搜索路径 */
  private String path;

  /** 排序 */
  private long sortTime;

  /** 类型名称 */
  private String name;
  /** 备注 */
  private String comments;

  /** 所有的子 */
  @Transient private List<ApiServiceType> children = new LinkedList<ApiServiceType>();

  public void addChild(ApiServiceType node) {
    this.children.add(node);
  }

  public static class NodeComparator implements Comparator<ApiServiceType> {
    @Override
    public int compare(ApiServiceType node1, ApiServiceType node2) {
      long orderTime1 = node1.getSortTime();
      long orderTime2 = node2.getSortTime();
      return orderTime1 > orderTime2 ? -1 : (orderTime1 == orderTime2 ? 0 : 1);
    }
  }
}
