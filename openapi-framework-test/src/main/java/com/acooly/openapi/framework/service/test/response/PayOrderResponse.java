package com.acooly.openapi.framework.service.test.response;


import com.acooly.openapi.framework.common.annotation.OpenApiMessage;
import com.acooly.openapi.framework.common.enums.ApiMessageType;
import com.acooly.openapi.framework.common.message.ApiResponse;

/**
 * Created by zhangpu on 2016/2/12.
 */
@OpenApiMessage(service = "payOrder", type = ApiMessageType.Response)
public class PayOrderResponse extends ApiResponse {
}
