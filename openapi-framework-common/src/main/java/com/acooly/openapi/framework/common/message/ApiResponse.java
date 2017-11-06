package com.acooly.openapi.framework.common.message;

import com.acooly.openapi.framework.common.annotation.OpenApiField;
import com.acooly.openapi.framework.common.enums.ApiServiceResultCode;
import org.hibernate.validator.constraints.NotEmpty;

public class ApiResponse extends ApiMessage {

  @NotEmpty
  @OpenApiField(desc = "服务响应编码", constraint = "必填")
  private String resultCode = ApiServiceResultCode.SUCCESS.getCode();

  @NotEmpty
  @OpenApiField(desc = "服务响应信息")
  private String resultMessage = ApiServiceResultCode.SUCCESS.getMessage();

  @OpenApiField(desc = "服务响应信息详情")
  private String resultDetail;

  @OpenApiField(desc = "回调地址", constraint = "需要回跳到商户时必填")
  private String returnUrl;

  @OpenApiField(desc = "服务响应状态")
  private boolean success = true;

  public void setResult(ApiServiceResultCode apiServiceResultCode) {
    setResult(apiServiceResultCode, null);
  }

  public void setResult(ApiServiceResultCode apiServiceResultCode, String detail) {
    setResultCode(apiServiceResultCode.code());
    setResultMessage(apiServiceResultCode.message());
    setResultDetail(detail);
  }

  public String getResultCode() {
    return resultCode;
  }

  public void setResultCode(String resultCode) {
    if (resultCode == null) {
      this.resultCode = resultCode;
      return;
    }
    if (resultCode.equals(ApiServiceResultCode.SUCCESS.code())
        || resultCode.equals(ApiServiceResultCode.PROCESSING.code())) {
      success = true;
    } else {
      success = false;
    }
    this.resultCode = resultCode;
  }

  public String getResultMessage() {
    return resultMessage;
  }

  public void setResultMessage(String resultMessage) {
    this.resultMessage = resultMessage;
  }

  public String getReturnUrl() {
    return returnUrl;
  }

  public void setReturnUrl(String returnUrl) {
    this.returnUrl = returnUrl;
  }

  public String getResultDetail() {
    return resultDetail;
  }

  public void setResultDetail(String resultDetail) {
    this.resultDetail = resultDetail;
  }

  public boolean isSuccess() {
    return success;
  }
}
