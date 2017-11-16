/** create by zhangpu date:2015年5月12日 */
package com.acooly.openapi.framework.app.biz.enums;

import com.google.common.collect.Maps;

import java.util.Map;

/** @author zhangpu */
public enum EntityStatus {
  Enable("Enable", "有效"),

  Disable("Disable", "无效");

  private String code;
  private String message;

  private EntityStatus(String code, String message) {
    this.code = code;
    this.message = message;
  }

  public static Map<String, String> mapping() {
    Map<String, String> map = Maps.newLinkedHashMap();
    for (EntityStatus type : values()) {
      map.put(type.getCode(), type.getMessage());
    }
    return map;
  }

  public String getCode() {
    return code;
  }

  public String getMessage() {
    return message;
  }

  @Override
  public String toString() {
    return this.code + " : " + this.message;
  }
}
