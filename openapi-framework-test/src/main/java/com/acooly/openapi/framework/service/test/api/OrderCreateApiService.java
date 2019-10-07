package com.acooly.openapi.framework.service.test.api;

import com.acooly.core.utils.enums.SimpleStatus;
import com.acooly.openapi.framework.common.annotation.ApiDocNote;
import com.acooly.openapi.framework.common.annotation.ApiDocType;
import com.acooly.openapi.framework.common.annotation.OpenApiService;
import com.acooly.openapi.framework.common.enums.ApiBusiType;
import com.acooly.openapi.framework.common.enums.ResponseType;
import com.acooly.openapi.framework.core.service.base.BaseApiService;
import com.acooly.openapi.framework.service.test.request.OrderCreateApiRequest;
import com.acooly.openapi.framework.service.test.response.OrderCreateApiResponse;
import lombok.extern.slf4j.Slf4j;

/**
 * 跳转支付
 * <p>
 * 第一步：创建订单
 *
 * @author zhangpu
 * @date 2016/2/12
 */
@Slf4j
@ApiDocType(code = "test", name = "测试")
@ApiDocNote("测试同步请求，跳转支付，异步通知整个过程。<b/>第一步：同步：创建订单")
@OpenApiService(name = "orderCreate", desc = "测试：创建订单服务", responseType = ResponseType.SYN, owner = "acooly", busiType = ApiBusiType.Trade)
public class OrderCreateApiService extends BaseApiService<OrderCreateApiRequest, OrderCreateApiResponse> {
    @Override
    protected void doService(OrderCreateApiRequest request, OrderCreateApiResponse response) {
        log.info("OrderCreateApiService doService request: {}", request);
        response.setExt(request.getExt());
        response.setTestMoney(request.getAmount());
        response.setGoodInfos(request.getGoodsInfos());
        response.setStatus(SimpleStatus.enable);
        log.info("OrderCreateApiService doService response: {}", response);
    }
}
