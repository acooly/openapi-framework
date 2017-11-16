package com.acooly.openapi.framework.app.openapi.enums;

public enum ContentInfoType {
  Service_Agreement("code", "10600001", "网站服务协议"),

  Risk_Warning_Agreement("code", "10050001", "投资风险提示书"),

  Contact("module", "1010", "联系我们"),

  About("module", "1009", "关于我们"),

  News("News", "1005", "公司动态");

  private String type;
  private String code;
  private String message;

  private ContentInfoType(String type, String code, String message) {
    this.type = type;
    this.code = code;
    this.message = message;
  }

  public String getType() {
    return type;
  }

  public String getCode() {
    return code;
  }

  public String getMessage() {
    return message;
  }

  @Override
  public String toString() {
    return this.name() + ":" + this.getMessage();
  }
}
