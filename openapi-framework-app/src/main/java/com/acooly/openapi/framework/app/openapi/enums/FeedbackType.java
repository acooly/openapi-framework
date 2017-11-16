/** create by zhangpu date:2015年5月19日 */
package com.acooly.openapi.framework.app.openapi.enums;

import com.google.common.collect.Maps;

import java.util.Map;

/** @author zhangpu */
public enum FeedbackType {
  suggest("suggest", "建议"),

  complaint("complaint", "投诉"),

  bug("bug", "故障");

  private String code;
  private String message;

  private FeedbackType(String code, String message) {
    this.code = code;
    this.message = message;
  }

  public static Map<String, String> mapping() {
    Map<String, String> map = Maps.newLinkedHashMap();
    for (FeedbackType type : values()) {
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
    return this.code + ":" + this.message;
  }
}
