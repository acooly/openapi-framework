/*
 * acooly.cn Inc.
 * Copyright (c) 2016 All Rights Reserved.
 * create by zhangpu 
 * date:2016年3月20日
 *
 */
package com.acooly.openapi.framework.facade.result;

import com.acooly.core.common.facade.ResultBase;
import com.acooly.core.utils.Encodes;
import org.apache.commons.lang3.StringUtils;

import java.util.Iterator;
import java.util.Map;

/**
 * @author zhangpu
 */
public class ApiNotifyResult extends ResultBase {

	/** serialVersionUID */
	private static final long serialVersionUID = -348730141776307996L;
	/**
	 * 签名
	 */
	private String sign;
	/**
	 * 跳转通知URL
	 */
	private String returnUrl;
	/**
	 * 异步通知URL
	 */
	private String notifyUrl;

	public String getQueryString() {
		setParameter("sign", this.sign);
		StringBuilder sb = new StringBuilder();
		Iterator<Map.Entry<String, Object>> it = getParameters().entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry<String, Object> entry = it.next();
			if (entry.getValue() != null) {
				sb.append(entry.getKey()).append('=').append(Encodes.urlEncode((String)entry.getValue()));
			}else{
				continue;
			}
			if (it.hasNext()) {
				sb.append('&');
			}
		}
		return sb.toString();
	}

	public String getCompleteReturnUrl() {

		if (StringUtils.isEmpty(getReturnUrl())) {
			return null;
		}
		String queryString = getQueryString();
		if (StringUtils.contains(getReturnUrl(), "?")) {
			return getReturnUrl() + "&" + queryString;
		} else {
			return getReturnUrl() + "?" + queryString;
		}

	}

	public String getCompleteNotifyUrl() {
		if (StringUtils.isEmpty(getNotifyUrl())) {
			return null;
		}
		String queryString = getQueryString();
		if (StringUtils.contains(getNotifyUrl(), "?")) {
			return getNotifyUrl() + "&" + queryString;
		} else {
			return getNotifyUrl() + "?" + queryString;
		}
	}

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
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

}
