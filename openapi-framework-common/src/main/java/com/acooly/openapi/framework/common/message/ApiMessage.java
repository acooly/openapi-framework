package com.acooly.openapi.framework.common.message;

import com.acooly.core.utils.ToString;
import com.acooly.openapi.framework.common.ApiConstants;
import com.acooly.openapi.framework.common.annotation.OpenApiAlias;
import com.acooly.openapi.framework.common.annotation.OpenApiField;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.Size;

/**
 * 基础报文bean
 *
 * @author zhangpu
 */
@Getter
@Setter
public abstract class ApiMessage {

  @NotEmpty
  @Size(min = 16, max = 40)
  @OpenApiField(desc = "请求流水号", constraint = "商户请求唯一性")
  @OpenApiAlias("orderNo")
  private String requestNo;

  @NotEmpty
  @OpenApiField(desc = "Api服务名", constraint = "必填")
  private String service;

  @NotEmpty
  @OpenApiField(desc = "商户ID", constraint = "必填")
  private String partnerId;

  @OpenApiField(desc = "服务版本", constraint = "非必填")
  private String version = ApiConstants.VERSION_DEFAULT;

  @Size(max = 128)
  @OpenApiField(desc = "会话参数", constraint = "调用端的API调用会话参数，请求参数任何合法值，在响应时会回传给调用端")
  private String context;

  @Size(min = 10, max = 40)
  @OpenApiField(desc = "交易订单号", constraint = "在有交易的场景必须填写。具有交易唯一性")
  private String merchOrderNo;

  @OpenApiField(desc = "是否为移动客户端访问", constraint = "非必填")
  private boolean appClient = false;

  @Override
  public String toString() {
    return ToString.toString(this);
  }
}
