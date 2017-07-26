/*
 * acooly.cn Inc.
 * Copyright (c) 2016 All Rights Reserved.
 * create by zhangpu 
 * date:2016年3月17日
 *
 */
package com.acooly.openapi.framework.common.exception;

import com.acooly.core.utils.enums.Messageable;

/**
 * openapi异常父类,所有异常必须继承于此异常.
 * 
 * @author zhangpu
 * @author Bohr.Qiu <qiubo@qq.com>
 */
public class ApiServiceException extends RuntimeException {

	/** UID */
	private static final long serialVersionUID = 4741339021195297955L;
	private String resultCode;
	private String resultMessage;
	private String detail;

	public ApiServiceException() {
		super();
	}

	/**
	 * @param resultCode
	 * @param resultMessage
	 * @param detail
	 */
	public ApiServiceException(String resultCode, String resultMessage, String detail) {
		super(resultCode + ":" + resultMessage + ":" + detail);
		this.resultCode = resultCode;
		this.resultMessage = resultMessage;
		this.detail = detail;
	}

	/**
	 * @param resultCode
	 * @param resultMessage
	 */
	public ApiServiceException(String resultCode, String resultMessage) {
		this(resultCode, resultMessage, null);
	}

	public ApiServiceException(Messageable apiServiceResultCode) {
		this(apiServiceResultCode, null);
	}

	public ApiServiceException(Messageable apiServiceResultCode, String detail) {
		this(apiServiceResultCode.code(), apiServiceResultCode.message(), detail);
	}

	public String getDetail() {
		return detail;
	}

	public void setDetail(String detail) {
		this.detail = detail;
	}

	public String getResultCode() {
		return resultCode;
	}

	public void setResultCode(String resultCode) {
		this.resultCode = resultCode;
	}

	public String getResultMessage() {
		return resultMessage;
	}

	public void setResultMessage(String resultMessage) {
		this.resultMessage = resultMessage;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("{resultCode:").append(resultCode).append(", resultMessage:").append(resultMessage)
				.append(", detail:").append(detail).append("}");
		return builder.toString();
	}

	@Override
	public synchronized Throwable fillInStackTrace() {
		return this;
	}
}
