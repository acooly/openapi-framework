/*
 * www.yiji.com Inc.
 * Copyright (c) 2014 All Rights Reserved
 */

/*
 * 修订记录:
 * qzhanbo@yiji.com 2014-07-27 01:44 创建
 *
 */
package com.yiji.framework.openapi.core.service.enums;

import java.util.Map;

import com.yiji.framework.openapi.common.ApiConstants;
import com.yiji.framework.openapi.core.util.ApiUtils;
import org.apache.commons.lang3.StringUtils;

import com.yiji.framework.openapi.core.service.base.ServiceValidator;

/**
 * 定义服务响应类型
 *
 * @author qzhanbo@yiji.com
 */
public enum ResponseType implements ServiceValidator {

	/**
	 * 请求直接响应
	 */
	SYN("同步服务") {
		@Override
		public void validate(Map<String, String> requestData) {

		}
	},
	/**
	 * 请求需要异步通知确认
	 */
	ASNY("异步服务") {
		@Override
		public void validate(Map<String, String> requestData) {
			String name = ApiConstants.NOTIFY_URL;
			if (StringUtils.isNotEmpty(requestData.get(name))) {
				ApiUtils.checkOpenAPIUrl(requestData.get(name), name);
			}
		}
	},
	/**
	 * 请求响应为重定向
	 */
	REDIRECT("重定向服务") {
		@Override
		public void validate(Map<String, String> requestData) {
			String name = ApiConstants.RETURN_URL;
			if (StringUtils.isNotEmpty(requestData.get(name))) {
				ApiUtils.checkOpenAPIUrl(requestData.get(name), name);
			}
			name = ApiConstants.NOTIFY_URL;
			if (StringUtils.isNotEmpty(requestData.get(name))) {
				ApiUtils.checkOpenAPIUrl(requestData.get(name), name);
			}
		}
	};

	private String msg;

	private ResponseType(String msg) {
		this.msg = msg;
	}

	public String getMsg() {
		return msg;
	}

}
