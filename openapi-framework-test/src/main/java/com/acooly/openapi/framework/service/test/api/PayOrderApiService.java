package com.acooly.openapi.framework.service.test.api;

import com.acooly.openapi.framework.common.enums.ApiBusiType;
import com.acooly.openapi.framework.common.enums.ApiServiceResultCode;
import com.acooly.openapi.framework.common.message.ApiNotify;
import com.acooly.openapi.framework.common.annotation.OpenApiDependence;
import com.acooly.openapi.framework.common.annotation.OpenApiService;
import com.acooly.openapi.framework.core.service.base.BaseApiService;
import com.acooly.openapi.framework.common.enums.ResponseType;
import com.acooly.openapi.framework.service.test.response.PayOrderResponse;
import com.acooly.openapi.framework.service.test.notify.PayOrderNotify;
import com.acooly.openapi.framework.service.test.request.PayOrderRequest;

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
