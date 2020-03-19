package com.acooly.openapi.framework.demo.service.api;

import com.acooly.core.common.boot.Apps;
import com.acooly.core.utils.mapper.BeanCopier;
import com.acooly.openapi.framework.common.annotation.ApiDocNote;
import com.acooly.openapi.framework.common.annotation.ApiDocType;
import com.acooly.openapi.framework.common.annotation.OpenApiService;
import com.acooly.openapi.framework.common.enums.ApiBusiType;
import com.acooly.openapi.framework.common.enums.ResponseType;
import com.acooly.openapi.framework.core.service.base.AbstractAsyncApiService;
import com.acooly.openapi.framework.demo.message.notify.OrderCashierPayNotify;
import com.acooly.openapi.framework.demo.message.request.OrderCashierPayApiRequest;
import com.acooly.openapi.framework.demo.message.response.OrderCashierPayApiRedirect;
import com.acooly.openapi.framework.demo.service.DemoApiUtils;
import lombok.extern.slf4j.Slf4j;

/**
 * 跳转支付
 * <p>
 * 第二步：跳转收银台
 *
 * @author zhangpu
 * @date 2016/2/12
 * @see com.acooly.openapi.framework.demo.service.web.OrderCashierPayServerTestController
 */
@Slf4j
@ApiDocType(code = DemoApiUtils.API_DEMO_DOC_TYPE_CODE, name = DemoApiUtils.API_DEMO_DOC_TYPE_NAME)
@ApiDocNote("测试同步请求，跳转支付，异步通知整个过程。<b/>第二步：跳转：订单收银台支付")
@OpenApiService(name = "orderCashierPay", desc = "测试：订单收银台支付", responseType = ResponseType.REDIRECT, owner = "acooly", busiType = ApiBusiType.Trade)
public class OrderCashierPayApiService extends AbstractAsyncApiService<OrderCashierPayApiRequest, OrderCashierPayApiRedirect, OrderCashierPayNotify> {

    @Override
    protected void doService(OrderCashierPayApiRequest request, OrderCashierPayApiRedirect redirect) {
        // 1、对request进行必要的逻辑检查，也可以调用下层facade完成；
        //   也可以是中转参数，如没有很大的必要，建议不调用，传入下层服务处理
        // ...
        // 2、组装需要传递个下层的参数，这里可以是第1步调用后返回的参数进行回填到redirect中。
        BeanCopier.copy(request, redirect);
        // 3、设置跳转到下层服务的跳转地址,别忘记了。这里只有你（OpenApi）知道应该向那个服务跳转，不知道的去问这个服务的接收方。
        //    这里开发一个controller来mock下层服务：OrderCashierPayServerTestController
        setRedirectUrl("http://127.0.0.1:" + Apps.getHttpPort() + "/openapi/demo/orderCashierPay/server/cashier.html");
    }
}
