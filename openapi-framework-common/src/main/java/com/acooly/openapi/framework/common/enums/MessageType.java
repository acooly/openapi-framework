/*
 * acooly.cn Inc.
 * Copyright (c) 2016 All Rights Reserved.
 * create by zhangpu
 * date:2016年3月20日
 *
 */
package com.acooly.openapi.framework.common.enums;

import com.acooly.core.utils.enums.Messageable;
import com.google.common.collect.Maps;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/** @author zhangpu */
public enum MessageType implements Messageable {
  HTTP("HTTP", "httpPost通知"),

  SMS("SMS", "短信通知"),

  MAIL("MAIL", "邮件通知");

  private final String code;
  private final String message;

  MessageType(String code, String message) {
    this.code = code;
    this.message = message;
  }

  public static Map<String, String> mapping() {
    Map<String, String> map = Maps.newLinkedHashMap();
    for (MessageType type : values()) {
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
  public static MessageType findStatus(String code) {
    for (MessageType status : values()) {
      if (status.getCode().equals(code)) {
        return status;
      }
    }
    throw new IllegalArgumentException("NotifyMessageType not legal:" + code);
  }

  /**
   * 获取全部枚举值。
   *
   * @return 全部枚举值。
   */
  public static List<MessageType> getAllStatus() {
    List<MessageType> list = new ArrayList<MessageType>();
    for (MessageType status : values()) {
      list.add(status);
    }
    return list;
  }

  /**
   * 获取全部枚举值码。
   *
   * @return 全部枚举值码。
   */
  public static List<String> getAllStatusCode() {
    List<String> list = new ArrayList<String>();
    for (MessageType status : values()) {
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
