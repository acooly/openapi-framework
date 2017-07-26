package com.acooly.openapi.framework.service.test.api;

import com.acooly.core.utils.enums.SimpleStatus;
import com.acooly.openapi.framework.common.enums.ApiBusiType;
import com.acooly.openapi.framework.core.meta.OpenApiNote;
import com.acooly.openapi.framework.core.meta.OpenApiService;
import com.acooly.openapi.framework.core.service.base.BaseApiService;
import com.acooly.openapi.framework.core.service.enums.ResponseType;
import com.acooly.openapi.framework.service.test.response.CreateOrderResponse;
import com.acooly.openapi.framework.service.test.request.CreateOrderRequest;

/**
 * Created by zhangpu on 2016/2/12.
 */
@OpenApiNote("APP在运营过程中，因为手机本地兼容问题，可能会造成APP崩溃，错误。为了改善用户体验，不断优化APP，我们需要在客户手机发生崩溃或错误的时候，收集崩溃 现场的错误堆栈信息，并通过该接口发送到服务器端，我们会在运营系统保存并展示实时上报的信息，并通过分析该崩溃报告在下个版本解决对应的问题。")
@OpenApiService(name = "createOrder", desc = "创建订单服务", responseType = ResponseType.SYN, owner = "openApi-arch", busiType = ApiBusiType.Trade)
public class CreateOrderApiService extends BaseApiService<CreateOrderRequest, CreateOrderResponse> {
	@Override
	protected void doService(CreateOrderRequest request, CreateOrderResponse response) {
		response.setTestMoney(request.getAmount());
		response.setGoodInfos(request.getGoodsInfos());
		response.setStatus(SimpleStatus.enable);
		// OK mock 处理了。
	}
}
