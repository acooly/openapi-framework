package com.acooly.openapi.framework.common.enums;

public enum ApiScheme {
  ALL("all"),

  HTTP("http"),

  HTTPS("https");

  private String scheme;

  private ApiScheme(String scheme) {
    this.scheme = scheme;
  }

  public String getScheme() {
    return scheme;
  }
}
