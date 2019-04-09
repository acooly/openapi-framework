package com.acooly.openapi.framework.common.message;

import com.acooly.openapi.framework.common.annotation.OpenApiField;
import lombok.Getter;
import lombok.Setter;

/** @author qiubo@yiji.com */
@Getter
@Setter
public class ApiAsyncRequest extends ApiRequest {
  @OpenApiField(desc = "通知地址", constraint = "使用异步服务时必填")
  private String notifyUrl;

  @OpenApiField(desc = "回调地址", constraint = "需要回跳到商户时必填")
  private String returnUrl;
}
