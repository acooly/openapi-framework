/** create by zhangpu date:2015年5月7日 */
package com.acooly.openapi.framework.app.openapi.enums;

import com.acooly.core.utils.enums.Messageable;
import com.google.common.collect.Maps;

import java.util.Map;

/**
 * 短信验证码类型
 *
 * @author zhangpu
 */
public enum CaptchaType implements Messageable {
  DEDUCT("DEDUCT", "充值", "amount"),

  WITHDRAW("WITHDRAW", "提现", "amount");

  private String code;
  private String message;
  private String detail;

  private CaptchaType(String code, String message, String detail) {
    this.code = code;
    this.message = message;
    this.detail = detail;
  }

  public static Map<String, String> mapping() {
    Map<String, String> map = Maps.newLinkedHashMap();
    for (CaptchaType type : values()) {
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

  public String getDetail() {
    return detail;
  }

  @Override
  public String toString() {
    return this.code + " : " + this.message + "(" + this.detail + ")";
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
