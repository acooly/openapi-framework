package com.acooly.openapi.framework.service.test.api;

import com.acooly.core.utils.Money;
import com.acooly.openapi.framework.common.annotation.OpenApiDependence;
import com.acooly.openapi.framework.common.annotation.OpenApiService;
import com.acooly.openapi.framework.common.context.ApiContextHolder;
import com.acooly.openapi.framework.common.enums.ApiBusiType;
import com.acooly.openapi.framework.common.enums.ApiServiceResultCode;
import com.acooly.openapi.framework.common.enums.ResponseType;
import com.acooly.openapi.framework.common.exception.ApiServiceException;
import com.acooly.openapi.framework.common.message.ApiNotify;
import com.acooly.openapi.framework.core.service.base.BaseApiService;
import com.acooly.openapi.framework.facade.api.OpenApiRemoteService;
import com.acooly.openapi.framework.facade.order.ApiNotifyOrder;
import com.acooly.openapi.framework.service.test.notify.PayOrderNotify;
import com.acooly.openapi.framework.service.test.request.PayOrderRequest;
import com.acooly.openapi.framework.service.test.response.PayOrderResponse;
import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

/** Created by zhangpu on 2016/2/12. */
@OpenApiDependence("createOrder")
@OpenApiService(
  name = "payOrder",
  desc = "支付订单服务",
  responseType = ResponseType.ASNY,
  owner = "openApi-arch",
  busiType = ApiBusiType.Trade
)
public class PayOrderApiService extends BaseApiService<PayOrderRequest, PayOrderResponse> {
  @Autowired private OpenApiRemoteService openApiRemoteService;

  @Override
  protected void doService(PayOrderRequest request, PayOrderResponse response) {
    response.setResult(ApiServiceResultCode.PROCESSING);
    String gid = ApiContextHolder.getApiContext().getGid();
    // OK mock 处理了。
    new Thread(
            () -> {
              ApiNotifyOrder apiNotifyOrder = new ApiNotifyOrder();
              PayOrderNotify payOrderNotify = new PayOrderNotify();
              payOrderNotify.setAmount(new Money("1000"));
              payOrderNotify.setTradeNo("xdf");
              payOrderNotify.setOutOrderNo(request.getRequestNo());
              apiNotifyOrder.setPartnerId(request.getPartnerId());
              apiNotifyOrder.setGid(gid);
              try {
                apiNotifyOrder.setParameters(BeanUtils.describe(payOrderNotify));
              } catch (Exception e) {
                throw new ApiServiceException(ApiServiceResultCode.INTERNAL_ERROR, e);
              }
              openApiRemoteService.asyncNotify(apiNotifyOrder);
            })
        .start();
  }

  @Override
  public ApiNotify getApiNotifyBean() {
    return new PayOrderNotify();
  }
}
