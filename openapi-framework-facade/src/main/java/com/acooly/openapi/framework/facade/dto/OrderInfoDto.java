/*
 * acooly.com Inc.
 * Copyright (c) 2016 All Rights Reserved
 */

/*
 * 修订记录:
 * kuli@yiji.com 2016-11-10 01:39 创建
 */
package com.acooly.openapi.framework.facade.dto;

import java.io.Serializable;
import java.util.Date;

/** @author acooly */
public class OrderInfoDto implements Serializable {
  /** UID */
  private static final long serialVersionUID = -8639008164669020986L;

  private Long id;
  private String gid;
  private String oid;
  private String requestNo;
  private String orderNo;
  private String partnerId;
  private String service;
  private String version;
  private String signType;
  private String returnUrl;
  private String notifyUrl;
  private Date rawAddTime;
  private String context;

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

  public String getOrderNo() {
    return orderNo;
  }

  public void setOrderNo(String orderNo) {
    this.orderNo = orderNo;
  }

  public String getPartnerId() {
    return partnerId;
  }

  public void setPartnerId(String partnerId) {
    this.partnerId = partnerId;
  }

  public String getService() {
    return service;
  }

  public void setService(String service) {
    this.service = service;
  }

  public String getReturnUrl() {
    return returnUrl;
  }

  public void setReturnUrl(String returnUrl) {
    this.returnUrl = returnUrl;
  }

  public String getNotifyUrl() {
    return notifyUrl;
  }

  public void setNotifyUrl(String notifyUrl) {
    this.notifyUrl = notifyUrl;
  }

  public Date getRawAddTime() {
    return rawAddTime == null ? null : (Date) rawAddTime.clone();
  }

  public void setRawAddTime(Date rawAddTime) {
    this.rawAddTime = (rawAddTime == null ? null : (Date) rawAddTime.clone());
  }

  public String getVersion() {
    return version;
  }

  public void setVersion(String version) {
    this.version = version;
  }

  public String getSignType() {
    return signType;
  }

  public void setSignType(String signType) {
    this.signType = signType;
  }

  public String getRequestNo() {
    return requestNo;
  }

  public void setRequestNo(String requestNo) {
    this.requestNo = requestNo;
  }

  public String getOid() {
    return oid;
  }

  public void setOid(String oid) {
    this.oid = oid;
  }

  public String getContext() {
    return context;
  }

  public void setContext(String context) {
    this.context = context;
  }

  @Override
  public String toString() {
    return String.format(
        "{gid:%s, requestNo:%s, orderNo:%s, partnerId:%s, service:%s, version:%s, signType:%s, returnUrl:%s, notifyUrl:%s, rawAddTime:%s, context:%s}",
        gid,
        requestNo,
        orderNo,
        partnerId,
        service,
        version,
        signType,
        returnUrl,
        notifyUrl,
        rawAddTime,
        context);
  }
}
