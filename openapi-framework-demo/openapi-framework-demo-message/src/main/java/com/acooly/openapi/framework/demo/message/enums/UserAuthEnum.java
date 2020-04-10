/*
 * acooly.com Inc.
 * Copyright (c) 2015 All Rights Reserved.
 */

/*
 * 修订记录：
 * faZheng 2016年2月22日 下午2:45:51 创建
 */

package com.acooly.openapi.framework.demo.message.enums;

import com.acooly.core.utils.enums.Messageable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/** @author faZheng */
public enum UserAuthEnum implements Messageable {

  /** 未认证 */
  AUTH_NO("AUTH_NO", "未认证"),

  /** 认证中 */
  AUTH_ING("AUTH_ING", "认证中"),

  /** 已认证 */
  AUTH_OK("AUTH_OK", "已认证"),

  /** 认证失败 */
  AUTH_FAIL("AUTH_FAIL", "认证失败"),

  /** 认证过期 */
  AUTH_OVER("AUTH_OVER", "认证过期");

  private final String code;
  private final String message;

  UserAuthEnum(String code, String message) {
    this.code = code;
    this.message = message;
  }

  public static Map<String, String> mapping() {
    Map<String, String> map = new HashMap<String, String>();
    for (UserAuthEnum type : values()) {
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
  public static UserAuthEnum getByCode(String code) {
    for (UserAuthEnum status : values()) {
      if (status.getCode().equals(code)) {
        return status;
      }
    }
    throw new IllegalArgumentException("UserAuthEnum not legal:" + code);
  }

  /**
   * 获取全部枚举值。
   *
   * @return 全部枚举值。
   */
  public static List<UserAuthEnum> getAllEnum() {
    List<UserAuthEnum> list = new ArrayList<UserAuthEnum>();
    for (UserAuthEnum status : values()) {
      list.add(status);
    }
    return list;
  }

  /**
   * 获取全部枚举值码。
   *
   * @return 全部枚举值码。
   */
  public static List<String> getAllEnumCode() {
    List<String> list = new ArrayList<String>();
    for (UserAuthEnum status : values()) {
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
