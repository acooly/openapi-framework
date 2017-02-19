package com.yiji.framework.openapi.demo.server.message.response;


import com.yiji.framework.openapi.common.annotation.OpenApiMessage;
import com.yiji.framework.openapi.common.enums.ApiMessageType;
import com.yiji.framework.openapi.common.message.ApiResponse;

/**
 * Created by zhangpu on 2016/2/12.
 */
@OpenApiMessage(service = "payOrder", type = ApiMessageType.Response)
public class PayOrderResponse extends ApiResponse {
}
