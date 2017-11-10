package com.acooly.openapi.framework.common.message;

import com.acooly.core.utils.ToString;
import com.acooly.openapi.framework.common.ApiConstants;
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
  @Size(min = 8, max = 64)
  @OpenApiField(desc = "请求流水号", constraint = "商户请求号，全局唯一。建议规则为：商户前缀+唯一标识")
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

  @Override
  public String toString() {
    return ToString.toString(this);
  }
}
