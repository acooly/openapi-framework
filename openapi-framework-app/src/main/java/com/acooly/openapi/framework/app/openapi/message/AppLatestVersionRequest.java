package com.acooly.openapi.framework.app.openapi.message;


import com.acooly.openapi.framework.common.annotation.OpenApiField;
import com.acooly.openapi.framework.common.annotation.OpenApiMessage;
import com.acooly.openapi.framework.common.enums.ApiMessageType;
import com.acooly.openapi.framework.common.message.ApiRequest;
import org.hibernate.validator.constraints.NotEmpty;

/**
 * 客户最新版本 请求报文
 *
 * @note 格式：HTTP-POST格式,如：name=xxx&amp;age=12
 * @author zhangpu
 */
@OpenApiMessage(service = "appLatestVersion", type = ApiMessageType.Request)
public class AppLatestVersionRequest extends ApiRequest {

  @OpenApiField(desc = "APP编码", constraint = "APP唯一标志,默认为:woldd")
  private String appCode = "woldd";

  @NotEmpty
  @OpenApiField(desc = "设备类型", constraint = "可选值:android和iphone")
  private String deviceType;

  public String getDeviceType() {
    return deviceType;
  }

  public void setDeviceType(String deviceType) {
    this.deviceType = deviceType;
  }

  public String getAppCode() {
    return appCode;
  }

  public void setAppCode(String appCode) {
    this.appCode = appCode;
  }

  @Override
  public String toString() {
    return String.format(
        "AppLatestVersionRequest: {appCode:%s, deviceType:%s}", appCode, deviceType);
  }
}
