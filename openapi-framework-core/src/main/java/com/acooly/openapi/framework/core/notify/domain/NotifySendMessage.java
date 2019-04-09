/*
 * acooly.cn Inc.
 * Copyright (c) 2016 All Rights Reserved.
 * create by zhangpu
 * date:2016年3月20日
 *
 */
package com.acooly.openapi.framework.core.notify.domain;

import com.acooly.core.common.facade.LinkedHashMapParameterize;
import com.acooly.core.utils.validate.jsr303.HttpUrl;
import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.Id;
import java.io.Serializable;

/** @author zhangpu */
public class NotifySendMessage extends LinkedHashMapParameterize<String, String>
    implements Serializable {

  /** serialVersionUID */
  private static final long serialVersionUID = -4139923883446595514L;

  @Id private Long id;
  @NotBlank private String gid;
  @NotBlank private String requestNo;
  @NotBlank private String merchOrderNo;
  @NotBlank private String partnerId;
  @NotBlank @HttpUrl private String url;
  @NotBlank private String service;
  @NotBlank private String version;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getGid() {
    return gid;
  }

  public void setGid(String gid) {
    this.gid = gid;
  }

  public String getPartnerId() {
    return partnerId;
  }

  public void setPartnerId(String partnerId) {
    this.partnerId = partnerId;
  }

  public String getUrl() {
    return url;
  }

  public void setUrl(String url) {
    this.url = url;
  }

  public String getService() {
    return service;
  }

  public void setService(String service) {
    this.service = service;
  }

  public String getVersion() {
    return version;
  }

  public void setVersion(String version) {
    this.version = version;
  }

  public String getRequestNo() {
    return requestNo;
  }

  public void setRequestNo(String requestNo) {
    this.requestNo = requestNo;
  }

  public String getMerchOrderNo() {
    return merchOrderNo;
  }

  public void setMerchOrderNo(String merchOrderNo) {
    this.merchOrderNo = merchOrderNo;
  }
}
