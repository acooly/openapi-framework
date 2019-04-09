package com.acooly.openapi.framework.service.test.api;

import com.acooly.core.utils.mapper.BeanCopier;
import com.acooly.openapi.framework.common.annotation.ApiDocNote;
import com.acooly.openapi.framework.common.annotation.ApiDocType;
import com.acooly.openapi.framework.common.annotation.OpenApiService;
import com.acooly.openapi.framework.common.enums.ApiBusiType;
import com.acooly.openapi.framework.common.enums.ResponseType;
import com.acooly.openapi.framework.common.message.ApiNotify;
import com.acooly.openapi.framework.core.service.base.BaseApiService;
import com.acooly.openapi.framework.service.test.request.OrderCashierPayRequest;
import com.acooly.openapi.framework.service.test.response.OrderCashierPayNotify;
import com.acooly.openapi.framework.service.test.response.OrderCashierPayRedirect;
import lombok.extern.slf4j.Slf4j;

/**
 * Created by zhangpu on 2016/2/12.
 */
@Slf4j
@ApiDocType(code = "test", name = "测试")
@ApiDocNote("测试同步请求，跳转支付，异步通知整个过程。<b/>第二步：跳转：订单收银台支付")
@OpenApiService(name = "orderCashierPay", desc = "测试：订单收银台支付", responseType = ResponseType.REDIRECT, owner = "openApi-arch", busiType = ApiBusiType.Trade)
public class OrderCashierPayApiService extends BaseApiService<OrderCashierPayRequest, OrderCashierPayRedirect> {

    @Override
    protected void doService(OrderCashierPayRequest request, OrderCashierPayRedirect redirect) {
        // 1、对request进行必要的逻辑检查，也可以调用下层facade完成

        // 2、设置跳转到下层服务的跳转地址
        setRedirectUrl("http://localhost:8089/openapi/test/server/cashier.html");

        // 3、组装需要传递个下层的参数
        BeanCopier.copy(request, redirect);
    }


    @Override
    public ApiNotify getApiNotifyBean() {
        return new OrderCashierPayNotify();
    }
}
