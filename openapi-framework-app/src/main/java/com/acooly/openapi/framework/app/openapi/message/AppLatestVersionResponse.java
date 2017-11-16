package com.acooly.openapi.framework.app.openapi.message;


import com.acooly.openapi.framework.common.annotation.OpenApiField;
import com.acooly.openapi.framework.common.annotation.OpenApiMessage;
import com.acooly.openapi.framework.common.enums.ApiMessageType;
import com.acooly.openapi.framework.common.message.ApiResponse;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * 客户最新版本 响应报文
 *
 * @author zhangpu
 */
@OpenApiMessage(service = "appLatestVersion", type = ApiMessageType.Response)
public class AppLatestVersionResponse extends ApiResponse {
  @OpenApiField(desc = "应用编码")
  private String appCode;

  @OpenApiField(desc = "应用名称")
  private String appName;

  @NotEmpty
  @OpenApiField(desc = "设备类型", constraint = "可选值:android和iphone")
  private String deviceType;

  @NotNull
  @OpenApiField(desc = "版本编码", constraint = "系统返回最大的版本编码，表示最新版本。")
  private int versionCode;

  @NotEmpty
  @OpenApiField(desc = "版本号", constraint = "版本编码对应的版本号，用于显示")
  private String versionName;

  @NotEmpty
  @OpenApiField(desc = "版本说明", constraint = "版本更新说明，可用于changelog")
  private String subject;

  @NotEmpty
  @OpenApiField(desc = "下载地址", constraint = "程序对应的下载地址全URL")
  private String url;

  @NotEmpty
  @OpenApiField(desc = "发布时间")
  private Date pubTime;

  @NotNull
  @OpenApiField(desc = "是否强制更新", constraint = "可选值:0:否，1:是")
  private int forceUpdate;

  public String getDeviceType() {
    return deviceType;
  }

  public void setDeviceType(String deviceType) {
    this.deviceType = deviceType;
  }

  public int getVersionCode() {
    return versionCode;
  }

  public void setVersionCode(int versionCode) {
    this.versionCode = versionCode;
  }

  public String getVersionName() {
    return versionName;
  }

  public void setVersionName(String versionName) {
    this.versionName = versionName;
  }

  public String getSubject() {
    return subject;
  }

  public void setSubject(String subject) {
    this.subject = subject;
  }

  public String getUrl() {
    return url;
  }

  public void setUrl(String url) {
    this.url = url;
  }

  public Date getPubTime() {
    return pubTime;
  }

  public void setPubTime(Date pubTime) {
    this.pubTime = pubTime;
  }

  public int getForceUpdate() {
    return forceUpdate;
  }

  public void setForceUpdate(int forceUpdate) {
    this.forceUpdate = forceUpdate;
  }

  @Override
  public String toString() {
    return String.format(
        "AppLatestVersionResponse: {appCode:%s, appName:%s, deviceType:%s, versionCode:%s, versionName:%s, subject:%s, url:%s, pubTime:%s, forceUpdate:%s}",
        appCode, appName, deviceType, versionCode, versionName, subject, url, pubTime, forceUpdate);
  }
}
