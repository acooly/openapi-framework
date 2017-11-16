/** create by zhangpu date:2015年11月4日 */
package com.acooly.openapi.framework.app.biz.enums;

import com.google.common.collect.Maps;

import java.util.Map;

/**
 * 系统消息类型
 *
 * @author zhangpu
 * @date 2015年11月4日
 */
public enum AppMessageStatus {
  apply("apply", "提交"),

  sending("sending", "发送中"),

  success("success", "成功"),

  fail("fail", "失败");

  private String code;
  private String message;

  private AppMessageStatus(String code, String message) {
    this.code = code;
    this.message = message;
  }

  public static Map<String, String> mapping() {
    Map<String, String> map = Maps.newLinkedHashMap();
    for (AppMessageStatus type : values()) {
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
