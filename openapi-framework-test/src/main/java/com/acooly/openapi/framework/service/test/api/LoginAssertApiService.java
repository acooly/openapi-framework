package com.acooly.openapi.framework.service.test.api;

import com.acooly.openapi.framework.common.annotation.OpenApiField;
import com.acooly.openapi.framework.common.annotation.OpenApiService;
import com.acooly.openapi.framework.common.context.ApiContextHolder;
import com.acooly.openapi.framework.common.enums.ApiBusiType;
import com.acooly.openapi.framework.common.enums.ResponseType;
import com.acooly.openapi.framework.common.message.ApiRequest;
import com.acooly.openapi.framework.common.message.ApiResponse;
import com.acooly.openapi.framework.core.service.base.BaseApiService;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

/**
 * @author qiuboboy@qq.com
 * @date 2017-11-23 14:56
 */
@OpenApiService(
  name = "loginAssert",
  desc = "创建订单服务",
  responseType = ResponseType.SYN,
  owner = "openApi-arch",
  busiType = ApiBusiType.Trade
)
public class LoginAssertApiService
    extends BaseApiService<
        LoginAssertApiService.LoginAssertRequest, LoginAssertApiService.LoginAssertResponse> {

  @Override
  protected void doService(LoginAssertRequest request, LoginAssertResponse response) {
    response.setAccessKey(ApiContextHolder.getApiContext().getAccessKey());
  }

  @Getter
  @Setter
  public static class LoginAssertRequest extends ApiRequest {}

  @Getter
  @Setter
  public static class LoginAssertResponse extends ApiResponse {
    @NotNull
    @OpenApiField(desc = "金额")
    private String accessKey;
  }
}
