package com.acooly.openapi.framework.service.test.request;

import com.acooly.core.utils.Money;
import com.acooly.openapi.framework.common.annotation.OpenApiField;
import com.acooly.openapi.framework.common.annotation.OpenApiMessage;
import com.acooly.openapi.framework.common.enums.ApiMessageType;
import com.acooly.openapi.framework.common.message.ApiAsyncRequest;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;

/**
 * 支付订单 请求报文
 *
 * <p>Created by zhangpu on 2016/2/12.
 */
@OpenApiMessage(service = "payOrder", type = ApiMessageType.Request)
public class PayOrderRequest extends ApiAsyncRequest {

  @NotNull
  @OpenApiField(desc = "金额")
  private Money amount;

  @NotEmpty
  @OpenApiField(desc = "买家用户ID")
  private String payerUserId;

  public Money getAmount() {
    return amount;
  }

  public void setAmount(Money amount) {
    this.amount = amount;
  }

  public String getPayerUserId() {
    return payerUserId;
  }

  public void setPayerUserId(String payerUserId) {
    this.payerUserId = payerUserId;
  }
}
