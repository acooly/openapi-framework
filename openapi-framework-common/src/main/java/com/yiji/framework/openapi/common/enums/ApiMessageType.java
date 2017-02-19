/*
 * acooly.cn Inc.
 * Copyright (c) 2016 All Rights Reserved.
 * create by zhangpu 
 * date:2016年4月23日
 *
 */
package com.yiji.framework.openapi.common.enums;

import com.acooly.core.utils.enums.Messageable;
import com.google.common.collect.Maps;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 定义报文类型
 * 
 * @author zhangpu
 */
public enum ApiMessageType implements Messageable {

	Request("Request","请求报文"),
	Response("Response","响应报文"),
	Return("Retrun","同步通知报文"),
	Notify("Notify","异步通知报文");
	
	private final String code;
	private final String message;

	private ApiMessageType(String code, String message) {
		this.code = code;
		this.message = message;
	}

	public String getCode() {
		return code;
	}

	public String getMessage() {
		return message;
	}

	public String code() {
		return code;
	}

	public String message() {
		return message;
	}

	public static Map<String, String> mapping() {
		Map<String, String> map = Maps.newLinkedHashMap();
		for (ApiMessageType type : values()) {
			map.put(type.getCode(), type.getMessage());
		}
		return map;
	}

	/**
	 * 通过枚举值码查找枚举值。
	 * 
	 * @param code
	 *            查找枚举值的枚举值码。
	 * @return 枚举值码对应的枚举值。
	 * @throws IllegalArgumentException
	 *             如果 code 没有对应的 Status 。
	 */
	public static ApiMessageType find(String code) {
		for (ApiMessageType status : values()) {
			if (status.getCode().equals(code)) {
				return status;
			}
		}
		throw new IllegalArgumentException("ApiMessageType not legal:" + code);
	}

	/**
	 * 获取全部枚举值。
	 * 
	 * @return 全部枚举值。
	 */
	public static List<ApiMessageType> getAll() {
		List<ApiMessageType> list = new ArrayList<ApiMessageType>();
		for (ApiMessageType status : values()) {
			list.add(status);
		}
		return list;
	}

	/**
	 * 获取全部枚举值码。
	 * 
	 * @return 全部枚举值码。
	 */
	public static List<String> getAllCode() {
		List<String> list = new ArrayList<String>();
		for (ApiMessageType status : values()) {
			list.add(status.code());
		}
		return list;
	}

	@Override
	public String toString() {
		return String.format("%s:%s", this.code, this.message);
	}
	
	
}
