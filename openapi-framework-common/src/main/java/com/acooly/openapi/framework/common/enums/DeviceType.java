package com.acooly.openapi.framework.common.enums;

import com.acooly.core.utils.enums.Messageable;

/**
 * @author qiuboboy@qq.com
 * @date 2018-07-23 13:35
 */
public enum DeviceType implements Messageable {
  IPHONE("IPHONE", "IPHONE"),

  IPHONE4("IPHONE4", "IPHONE4"),

  IPHONE5("IPHONE5", "IPHONE5"),

  IPHONE6("IPHONE6", "IPHONE6"),

  IPHONE7("IPHONE7", "IPHONE7"),

  IPHONE8("IPHONE8", "IPHONE8"),

  IPHONEX("IPHONEX", "IPHONEX"),

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
