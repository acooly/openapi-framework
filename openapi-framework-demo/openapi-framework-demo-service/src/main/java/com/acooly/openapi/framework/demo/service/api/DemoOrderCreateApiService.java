package com.acooly.openapi.framework.demo.service.api;

import com.acooly.core.utils.enums.SimpleStatus;
import com.acooly.openapi.framework.common.annotation.ApiDocNote;
import com.acooly.openapi.framework.common.annotation.ApiDocType;
import com.acooly.openapi.framework.common.annotation.OpenApiService;
import com.acooly.openapi.framework.common.enums.ApiBusiType;
import com.acooly.openapi.framework.common.enums.ResponseType;
import com.acooly.openapi.framework.core.service.base.BaseApiService;
import com.acooly.openapi.framework.demo.message.request.DemoOrderCreateApiRequest;
import com.acooly.openapi.framework.demo.message.response.DemoOrderCreateApiResponse;
import com.acooly.openapi.framework.demo.service.DemoApiUtils;
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
@ApiDocType(code = DemoApiUtils.API_DEMO_DOC_TYPE_CODE, name = DemoApiUtils.API_DEMO_DOC_TYPE_NAME)
@ApiDocNote("测试同步请求，跳转支付，异步通知整个过程。<b/>第一步：同步：创建订单")
@OpenApiService(name = "demoOrderCreate", desc = "测试：创建订单服务", responseType = ResponseType.SYN, owner = "acooly", busiType = ApiBusiType.Trade)
public class DemoOrderCreateApiService extends BaseApiService<DemoOrderCreateApiRequest, DemoOrderCreateApiResponse> {
    @Override
    protected void doService(DemoOrderCreateApiRequest request, DemoOrderCreateApiResponse response) {
        log.info("OrderCreateApiService doService request: {}", request);
        response.setExt(request.getExt());
        response.setTestMoney(request.getAmount());
        response.setGoodsInfos(request.getGoodsInfos());
        response.setStatus(SimpleStatus.enable);
        log.info("OrderCreateApiService doService response: {}", response);
    }
}
