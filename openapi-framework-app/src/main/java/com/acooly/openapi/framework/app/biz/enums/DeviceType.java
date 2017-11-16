/** create by zhangpu date:2015年5月7日 */
package com.acooly.openapi.framework.app.biz.enums;

import com.acooly.core.utils.enums.Messageable;

/** @author zhangpu */
public enum DeviceType implements Messageable {
  IPHONE("IPHONE", "IPHONE"),

  IPHONE4("IPHONE4", "IPHONE4"),

  IPHONE5("IPHONE5", "IPHONE5"),

  IPHONE6("IPHONE6", "IPHONE6"),

  ANDROID("ANDROID", "ANDROID");

  private String code;
  private String message;

  private DeviceType(String code, String message) {
    this.code = code;
    this.message = message;
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
