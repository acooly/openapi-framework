/*
 * acooly.cn Inc.
 * Copyright (c) 2016 All Rights Reserved.
 * create by zhangpu 
 * date:2016年3月17日
 *
 */
package com.yiji.framework.openapi.domain;

import java.io.Serializable;
import java.util.Date;

import com.yiji.framework.openapi.common.enums.ApiProtocol;

/**
 * 请求订单实体
 * 
 * @author zhangpu
 * 
 */
public class OrderInfo implements Serializable {

	/**
	 * UID
	 */
	private static final long serialVersionUID = -8639008164669020986L;
	private Long id;
	private String gid;
	private String oid;
	private String requestNo;
	/** 商户订单号:merchOrderNo */
	private String orderNo;
	private String partnerId;
	private String service;
	private String version;
	private String signType;
	private String returnUrl;
	private String notifyUrl;
	private String charset;
	private ApiProtocol protocol;
	private String businessInfo;
	private Date rawAddTime;
	private Date rawUpdateTime;

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

	public String getService() {
		return service;
	}

	public void setService(String service) {
		this.service = service;
	}

	public void setPartnerId(String partnerId) {
		this.partnerId = partnerId;
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

	public String getCharset() {
		return charset;
	}

	public void setCharset(String charset) {
		this.charset = charset;
	}

	public String getBusinessInfo() {
		return businessInfo;
	}

	public void setBusinessInfo(String businessInfo) {
		this.businessInfo = businessInfo;
	}

	public Date getRawAddTime() {
		return rawAddTime == null ? null : (Date) rawAddTime.clone();
	}

	public void setRawAddTime(Date rawAddTime) {
		this.rawAddTime = (rawAddTime == null ? null : (Date) rawAddTime.clone());
	}

	public Date getRawUpdateTime() {
		return rawUpdateTime == null ? null : (Date) rawUpdateTime.clone();
	}

	public void setRawUpdateTime(Date rawUpdateTime) {
		this.rawUpdateTime = (rawUpdateTime == null ? null : (Date) rawUpdateTime.clone());
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

	public ApiProtocol getProtocol() {
		return protocol;
	}

	public void setProtocol(ApiProtocol protocol) {
		this.protocol = protocol;
	}

	@Override
	public String toString() {
		return String.format(
		        "{gid:%s, requestNo:%s, orderNo:%s, partnerId:%s, service:%s, version:%s, signType:%s, returnUrl:%s, notifyUrl:%s, rawAddTime:%s, rawUpdateTime:%s, context:%s}",
		        gid, requestNo, orderNo, partnerId, service, version, signType, returnUrl, notifyUrl, rawAddTime,
		        rawUpdateTime, context);
	}

}
