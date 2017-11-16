package com.acooly.openapi.framework.app.openapi.enums;

public enum ContentModule {
  ABOUT_US("30000001"),

  APP_VERSION_INFO("30000002");

  private String value;

  private ContentModule(String value) {
    this.value = value;
  }

  public String getValue() {
    return value;
  }

  public void setValue(String value) {
    this.value = value;
  }
}
