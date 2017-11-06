package com.acooly.openapi.framework.service.test.response;

import com.acooly.core.common.exception.OrderCheckException;
import com.acooly.core.utils.Money;
import com.acooly.openapi.framework.common.annotation.OpenApiField;
import com.acooly.openapi.framework.common.annotation.OpenApiMessage;
import com.acooly.openapi.framework.common.enums.ApiMessageType;
import com.acooly.openapi.framework.common.exception.ApiServiceException;
import com.acooly.openapi.framework.common.message.ApiResponse;
import com.acooly.openapi.framework.service.test.FinancePayApiErrorCode;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;

/**
 * 跳转提现页面
 *
 * @author cuifuqiang
 */
@OpenApiMessage(service = "redirectWithdraw", type = ApiMessageType.Response)
public class WithdrawRedirectResponse extends ApiResponse {

  @NotEmpty
  @Length(max = 32)
  @OpenApiField(desc = "用户UserId", constraint = "用户UserId")
  private String userId;

  @NotNull
  @OpenApiField(desc = "交易金额", constraint = "交易金额，单位：元")
  private Money amount;

  @Length(max = 32)
  @OpenApiField(desc = "提现账户", constraint = "提现账户")
  private String accountNo;

  @NotNull
  @Length(min = 20, max = 64)
  @OpenApiField(desc = "全站统一流水号")
  private String gid;

  /** 参数校验,校验失败请抛出OrderCheckException */
  public void check() throws OrderCheckException {
    String orderNo = getMerchOrderNo();
    if (StringUtils.isBlank(orderNo)) {
      throw new ApiServiceException(
          FinancePayApiErrorCode.TRADE_ORDER_CREATE_ERROR, "orderNo:交易订单号不能为空");
    }
    long amongCent = getAmount().getCent();
    if (amongCent <= 0) {
      throw new ApiServiceException(FinancePayApiErrorCode.PARAMETER_ERROR, "交易金额必须大于0");
    }
  }

  public String getUserId() {
    return userId;
  }

  public void setUserId(String userId) {
    this.userId = userId;
  }

  public Money getAmount() {
    return amount;
  }

  public void setAmount(Money amount) {
    this.amount = amount;
  }

  public String getAccountNo() {
    return accountNo;
  }

  public void setAccountNo(String accountNo) {
    this.accountNo = accountNo;
  }

  public String getGid() {
    return this.gid;
  }

  public void setGid(String gid) {
    this.gid = gid;
  }
}
