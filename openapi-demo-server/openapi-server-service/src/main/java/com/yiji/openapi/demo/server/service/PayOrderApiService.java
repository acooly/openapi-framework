package com.yiji.framework.openapi.demo.server.service;

import com.yiji.framework.openapi.common.enums.ApiServiceResultCode;
import com.yiji.framework.openapi.common.message.ApiNotify;
import com.yiji.framework.openapi.core.meta.OpenApiDependence;
import com.yiji.framework.openapi.core.meta.OpenApiService;
import com.yiji.framework.openapi.core.service.base.BaseApiService;
import com.yiji.framework.openapi.core.service.enums.ApiBusiType;
import com.yiji.framework.openapi.core.service.enums.ResponseType;
import com.yiji.framework.openapi.demo.server.message.notify.PayOrderNotify;
import com.yiji.framework.openapi.demo.server.message.request.PayOrderRequest;
import com.yiji.framework.openapi.demo.server.message.response.PayOrderResponse;

/**
 * Created by zhangpu on 2016/2/12.
 */
@OpenApiDependence("createOrder")
@OpenApiService(name = "payOrder", desc = "支付订单服务", responseType = ResponseType.ASNY, owner = "openApi-arch", busiType = ApiBusiType.Trade)
public class PayOrderApiService extends BaseApiService<PayOrderRequest, PayOrderResponse> {
    @Override
    protected void doService(PayOrderRequest request, PayOrderResponse response) {
        response.setResult(ApiServiceResultCode.PROCESSING);
        // OK mock 处理了。
    }

    @Override
    public ApiNotify getApiNotifyBean() {
        return new PayOrderNotify();
    }

}
