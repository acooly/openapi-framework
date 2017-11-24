package com.acooly.openapi.framework.common.message;

import com.acooly.core.common.facade.InfoBase;
import com.acooly.openapi.framework.common.ApiConstants;
import com.acooly.openapi.framework.common.annotation.OpenApiField;
import com.google.common.collect.Maps;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.Size;
import java.util.Map;

/**
 * 基础报文bean
 *
 * @author zhangpu
 */
@Getter
@Setter
public abstract class ApiMessage extends InfoBase {

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

  @OpenApiField(desc = "扩展参数", constraint = "扩展参数")
  private Map<String, String> ext = Maps.newHashMap();

  /** 增加扩展参数 */
  public void ext(String key, String value) {
    this.ext.put(key, value);
  }

  /** 获取扩展参数 */
  public String ext(String key) {
    return this.ext.get(key);
  }
}
