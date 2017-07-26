package com.acooly.openapi.framework.core.service.enums;

/**
 * API所属系统
 * <p/>
 * 主要用于内部管理
 * <p/>
 * Created by zhangpu on 2015/3/11.
 */
public enum ApiOwner {

	Unknown("Unknown", "未归类");

	private String code;

	private String title;

	ApiOwner(String code, String title) {
		this.code = code;
		this.title = title;
	}

	public String getCode() {
		return code;
	}

	public String getTitle() {
		return title;
	}

}
