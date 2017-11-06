package com.acooly.openapi.framework.common.message;

import com.acooly.core.utils.ToString;
import com.acooly.openapi.framework.common.ApiConstants;
import com.acooly.openapi.framework.common.annotation.OpenApiAlias;
import com.acooly.openapi.framework.common.annotation.OpenApiField;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.Size;

/**
 * 基础报文bean
 *
 * @author zhangpu
 */
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

  @NotEmpty
  @OpenApiField(desc = "签名类型", constraint = "必填")
  private String signType = "MD5";

  @NotEmpty
  @OpenApiField(desc = "签名", constraint = "必填")
  private String sign;

  @OpenApiField(desc = "服务协议", constraint = "非必填")
  private String protocol = "JSON";

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

  public boolean isAppClient() {
    return appClient;
  }

  public void setAppClient(boolean appClient) {
    this.appClient = appClient;
  }

  public String getMerchOrderNo() {
    return merchOrderNo;
  }

  public void setMerchOrderNo(String merchOrderNo) {
    this.merchOrderNo = merchOrderNo;
  }

  public String getService() {
    return service;
  }

  public void setService(String service) {
    this.service = service;
  }

  public String getProtocol() {
    return protocol;
  }

  public void setProtocol(String protocol) {
    this.protocol = protocol;
  }

  public String getPartnerId() {
    return partnerId;
  }

  public void setPartnerId(String partnerId) {
    this.partnerId = partnerId;
  }

  public String getSignType() {
    return signType;
  }

  public void setSignType(String signType) {
    this.signType = signType;
  }

  public String getSign() {
    return sign;
  }

  public void setSign(String sign) {
    this.sign = sign;
  }

  public String getVersion() {
    return version;
  }

  public void setVersion(String version) {
    this.version = version;
  }

  public String getContext() {
    return context;
  }

  public void setContext(String context) {
    this.context = context;
  }

  public String getRequestNo() {
    return requestNo;
  }

  public void setRequestNo(String requestNo) {
    this.requestNo = requestNo;
  }

  @Override
  public String toString() {
    return ToString.toString(this);
  }
}
