/** create by zhangpu date:2015年11月22日 */
package com.acooly.openapi.framework.app.biz.notify;

import com.google.common.collect.Maps;

import java.util.Map;

/**
 * 扩展参数名称定义
 *
 * <p>通过NotifyMessage的Properties传入
 *
 * @author zhangpu
 * @date 2015年11月22日
 */
public enum NotifyProperty {
  JPush_Notification("JPush_Notification", "JPush实现的Notification通知结构参数"),

  JPush_Options("JPush_Options", "JPush实现的Options通知结构参数"),
  ;

  private String code;
  private String message;

  private NotifyProperty(String code, String message) {
    this.code = code;
    this.message = message;
  }

  public static Map<String, String> mapping() {
    Map<String, String> map = Maps.newLinkedHashMap();
    for (NotifyProperty type : values()) {
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
