package com.acooly.openapi.framework.service.test.api;

import com.acooly.core.utils.Money;
import com.acooly.openapi.framework.common.annotation.ApiDocNote;
import com.acooly.openapi.framework.common.annotation.ApiDocType;
import com.acooly.openapi.framework.common.annotation.OpenApiService;
import com.acooly.openapi.framework.common.context.ApiContextHolder;
import com.acooly.openapi.framework.common.enums.ApiBusiType;
import com.acooly.openapi.framework.common.enums.ApiServiceResultCode;
import com.acooly.openapi.framework.common.enums.ResponseType;
import com.acooly.openapi.framework.common.message.ApiNotify;
import com.acooly.openapi.framework.core.service.base.BaseApiService;
import com.acooly.openapi.framework.facade.api.OpenApiRemoteService;
import com.acooly.openapi.framework.facade.order.ApiNotifyOrder;
import com.acooly.openapi.framework.service.test.notify.PayOrderNotify;
import com.acooly.openapi.framework.service.test.request.PayOrderRequest;
import com.acooly.openapi.framework.service.test.response.PayOrderResponse;
import com.alibaba.dubbo.config.annotation.Reference;

/**
 * Created by zhangpu on 2016/2/12.
 */
@ApiDocType(code = "test", name = "测试")
@ApiDocNote("简单支付订单服务的测试")
@OpenApiService(
        name = "orderPayFacade",
        desc = "测试：支付订单服务(Facade)",
        responseType = ResponseType.ASNY,
        owner = "openApi-arch",
        busiType = ApiBusiType.Trade
)
public class OrderPayApiFacadeService extends BaseApiService<PayOrderRequest, PayOrderResponse> {

    @Reference(version = "1.0")
    private OpenApiRemoteService openApiRemoteService;


    @Override
    protected void doService(PayOrderRequest request, PayOrderResponse response) {
        response.setResult(ApiServiceResultCode.PROCESSING);
        doMockNotify(request);
    }

    protected void doMockNotify(PayOrderRequest request) {
        // OK，这里直接mock下层服务异步通知回来处理了。
        String gid = ApiContextHolder.getApiContext().getGid();
        ApiNotifyOrder apiNotifyOrder = new ApiNotifyOrder();
        PayOrderNotify payOrderNotify = new PayOrderNotify();
        payOrderNotify.setAmount(new Money("1000"));
        payOrderNotify.setTradeNo("xdf");
        payOrderNotify.setOutOrderNo(request.getRequestNo());
        apiNotifyOrder.setPartnerId(request.getPartnerId());
        apiNotifyOrder.setGid(gid);
        getOpenApiRemoteService().asyncNotify(apiNotifyOrder);
    }

    protected OpenApiRemoteService getOpenApiRemoteService() {
        return this.openApiRemoteService;
    }

    @Override
    public ApiNotify getApiNotifyBean() {
        return new PayOrderNotify();
    }
}
