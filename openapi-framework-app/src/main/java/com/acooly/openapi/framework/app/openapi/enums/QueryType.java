/** create by zhangpu date:2015年10月18日 */
package com.acooly.openapi.framework.app.openapi.enums;

import com.google.common.collect.Maps;

import java.util.Map;

/**
 * @author zhangpu
 * @date 2015年10月18日
 */
public enum QueryType {
  byId("byId", "唯一标识方式"),

  byTypeTop("byTypeTop", "分类TOP1方式");

  private String code;
  private String message;

  private QueryType(String code, String message) {
    this.code = code;
    this.message = message;
  }

  public static Map<String, String> mapping() {
    Map<String, String> map = Maps.newLinkedHashMap();
    for (QueryType type : values()) {
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
