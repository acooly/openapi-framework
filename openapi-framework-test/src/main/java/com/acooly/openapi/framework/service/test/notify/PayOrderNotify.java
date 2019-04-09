package com.acooly.openapi.framework.service.test.notify;

import com.acooly.core.utils.Money;
import com.acooly.core.utils.validate.jsr303.MoneyConstraint;
import com.acooly.openapi.framework.common.annotation.OpenApiField;
import com.acooly.openapi.framework.common.message.ApiNotify;

import javax.validation.constraints.Size;

/** Created by zhangpu on 2016/2/12. */
public class PayOrderNotify extends ApiNotify {

  @Size(min = 16, max = 64)
  @OpenApiField(desc = "订单号")
  private String outOrderNo;

  @Size(min = 20, max = 20)
  @OpenApiField(desc = "交易号")
  private String tradeNo;

  @MoneyConstraint
  @OpenApiField(desc = "付款金额")
  private Money amount;

  public String getOutOrderNo() {
    return outOrderNo;
  }

  public void setOutOrderNo(String outOrderNo) {
    this.outOrderNo = outOrderNo;
  }

  public String getTradeNo() {
    return tradeNo;
  }

  public void setTradeNo(String tradeNo) {
    this.tradeNo = tradeNo;
  }

  public Money getAmount() {
    return amount;
  }

  public void setAmount(Money amount) {
    this.amount = amount;
  }
}
