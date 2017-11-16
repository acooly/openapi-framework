package com.acooly.openapi.framework.app.openapi;

import com.acooly.core.utils.enums.Messageable;

/**
 * 错误码定义
 *
 * @author zhangpu
 */
public enum AppApiErrorCode implements Messageable {
  LOGIN_FAIL("LOGIN_FAIL", "登录失败"),

  VERSION_NOFOUND("VERSION_NOFOUND", "APP最新版本未找到");

  private String code;
  private String message;

  AppApiErrorCode(String code, String message) {
    this.code = code;
    this.message = message;
  }

  @Override
  public String code() {
    return this.code;
  }

  @Override
  public String message() {
    return this.message;
  }
}
