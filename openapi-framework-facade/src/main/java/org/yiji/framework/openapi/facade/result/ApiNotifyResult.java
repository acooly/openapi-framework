/*
 * acooly.cn Inc.
 * Copyright (c) 2016 All Rights Reserved.
 * create by zhangpu 
 * date:2016年3月20日
 *
 */
package org.yiji.framework.openapi.facade.result;

import java.util.Map;

import com.acooly.core.common.facade.ResultBase;
import org.apache.commons.lang3.StringUtils;

import com.acooly.core.utils.Encodes;

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
		for (Map.Entry<String, Object> entry : getParameters().entrySet()) {
			sb.append(entry.getKey()).append("=").append(Encodes.urlEncode((String) entry.getValue())).append("&");
		}
		sb.substring(0, sb.length() - 1);
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
