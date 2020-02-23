/*
 * acooly.com Inc.
 * Copyright (c) 2015 All Rights Reserved.
 */

/*
 * 修订记录：
 * faZheng 2016年9月30日 上午11:03:22 创建
 */

package com.acooly.openapi.framework.service.test.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * 经营人类型
 *
 * @author faZheng
 */
public enum TransactorType {
  LEGAL_PERSON("LEGAL_PERSON", "法人"),

  TRANSACTOR("TRANSACTOR", "经营人"),
  ;

  /** 枚举值 */
  private final String code;

  /** 枚举描述 */
  private final String message;

  /**
   * @param code 枚举值
   * @param message 枚举描述
   */
  private TransactorType(String code, String message) {
    this.code = code;
    this.message = message;
  }

  /**
   * 通过枚举<code>code</code>获得枚举
   *
   * @param code
   * @return TransactorType
   */
  public static TransactorType getByCode(String code) {
    for (TransactorType _enum : values()) {
      if (_enum.getCode().equals(code)) {
        return _enum;
      }
    }
    return null;
  }

  /**
   * 获取全部枚举
   *
   * @return List<TransactorType>
   */
  public static java.util.List<TransactorType> getAllEnum() {
    java.util.List<TransactorType> list = new java.util.ArrayList<TransactorType>(values().length);
    for (TransactorType _enum : values()) {
      list.add(_enum);
    }
    return list;
  }

  /**
   * 获取全部枚举值
   *
   * @return List<String>
   */
  public static java.util.List<String> getAllEnumCode() {
    java.util.List<String> list = new java.util.ArrayList<String>(values().length);
    for (TransactorType _enum : values()) {
      list.add(_enum.code());
    }
    return list;
  }

  /**
   * 通过code获取msg
   *
   * @param code 枚举值
   * @return
   */
  public static String getMsgByCode(String code) {
    if (code == null) {
      return null;
    }
    TransactorType _enum = getByCode(code);
    if (_enum == null) {
      return null;
    }
    return _enum.getMessage();
  }

  /**
   * 获取枚举code
   *
   * @param _enum
   * @return
   */
  public static String getCode(TransactorType _enum) {
    if (_enum == null) {
      return null;
    }
    return _enum.getCode();
  }

  /**
   * 经营人类型map
   *
   * @return
   */
  public static Map<String, String> maps() {
    Map<String, String> map = new HashMap<String, String>();
    for (TransactorType type : getAllEnum()) {
      map.put(type.getCode(), type.getMessage());
    }
    return map;
  }

  /** @return Returns the code. */
  public String getCode() {
    return code;
  }

  /** @return Returns the message. */
  public String getMessage() {
    return message;
  }

  /** @return Returns the code. */
  public String code() {
    return code;
  }

  /** @return Returns the message. */
  public String message() {
    return message;
  }

  @Override
  public String toString() {
    return String.format("%s:%s", this.code, this.message);
  }
}
