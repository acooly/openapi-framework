/** create by zhangpu date:2015年6月15日 */
package com.acooly.openapi.framework.app.openapi.enums;

import com.acooly.core.utils.enums.Messageable;
import com.google.common.collect.Maps;

import java.util.Map;

/** @author zhangpu */
public enum CommissionType implements Messageable {
  withdraw("withdraw", "提现手续费");

  private String code;
  private String message;

  private CommissionType(String code, String message) {
    this.code = code;
    this.message = message;
  }

  public static Map<String, String> mapping() {
    Map<String, String> map = Maps.newLinkedHashMap();
    for (CommissionType type : values()) {
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

  @Override
  public String code() {
    return code;
  }

  @Override
  public String message() {
    return message;
  }
}
