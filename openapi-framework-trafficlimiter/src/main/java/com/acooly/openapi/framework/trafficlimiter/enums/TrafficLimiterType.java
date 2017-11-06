/*
 * www.yiji.com Inc.
 * Copyright (c) 2016 All Rights Reserved
 */

/*
 * 修订记录:
 * kuli@yiji.com 2016-12-18 19:44 创建
 */
package com.acooly.openapi.framework.trafficlimiter.enums;

import com.acooly.core.utils.enums.Messageable;
import com.google.common.collect.Maps;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/** @author acooly */
public enum TrafficLimiterType implements Messageable {
  Partner_Traffic_limit("Partner_Traffic_limit", "商户流控限制"),

  Partner_Service_Traffic_limit("Partner_Service_Traffic_limit", "商户单个服务流控限制");

  private final String code;
  private final String message;

  TrafficLimiterType(String code, String message) {
    this.code = code;
    this.message = message;
  }

  public static Map<String, String> mapping() {
    Map<String, String> map = Maps.newLinkedHashMap();
    for (TrafficLimiterType type : values()) {
      map.put(type.getCode(), type.getMessage());
    }
    return map;
  }

  /**
   * 通过枚举值码查找枚举值。
   *
   * @param code 查找枚举值的枚举值码。
   * @return 枚举值码对应的枚举值。
   * @throws IllegalArgumentException 如果 code 没有对应的 Status 。
   */
  public static TrafficLimiterType find(String code) {
    for (TrafficLimiterType status : values()) {
      if (status.getCode().equals(code)) {
        return status;
      }
    }
    throw new IllegalArgumentException("TrafficLimiterType not legal:" + code);
  }

  /**
   * 获取全部枚举值。
   *
   * @return 全部枚举值。
   */
  public static List<TrafficLimiterType> getAll() {
    List<TrafficLimiterType> list = new ArrayList<TrafficLimiterType>();
    for (TrafficLimiterType status : values()) {
      list.add(status);
    }
    return list;
  }

  /**
   * 获取全部枚举值码。
   *
   * @return 全部枚举值码。
   */
  public static List<String> getAllCode() {
    List<String> list = new ArrayList<String>();
    for (TrafficLimiterType status : values()) {
      list.add(status.code());
    }
    return list;
  }

  public String getCode() {
    return code;
  }

  public String getMessage() {
    return message;
  }

  public String code() {
    return code;
  }

  public String message() {
    return message;
  }

  @Override
  public String toString() {
    return String.format("%s:%s", this.code, this.message);
  }
}
