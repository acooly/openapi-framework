/*
 * www.yiji.com Inc.
 * Copyright (c) 2014 All Rights Reserved
 */

/*
 * 修订记录:
 * qzhanbo@yiji.com 2014-08-02 02:57 创建
 *
 */
package com.yiji.framework.openapi.core.executer;

import com.yiji.framework.openapi.core.meta.ArchOnly;

/**
 * 仅限框架内调用
 * 
 * @author qzhanbo@yiji.com
 */
@ArchOnly
public class ApiContextHolder {

	static final ThreadLocal<ApiContext> apiContextThreadLocal = new ThreadLocal<ApiContext>();

	@ArchOnly
	public static ApiContext getApiContext() {
		ApiContext apiContext = apiContextThreadLocal.get();
		return apiContext;
	}

	/**
	 * 仅限框架内调用
	 */
	@ArchOnly
	public static void init() {
		ApiContext apiContext = apiContextThreadLocal.get();
		if (apiContext == null) {
			apiContext = new ApiContext();
			apiContextThreadLocal.set(apiContext);
		}
	}

	/**
	 * 仅限框架内调用
	 */
	@ArchOnly
	public static void setApiContext(ApiContext apiContext) {
		if (apiContext != null) {
			apiContextThreadLocal.set(apiContext);
		}
	}

	/**
	 * ApiContextHolder是否已经初始化
	 *
	 * 仅限框架内调用
	 * 
	 * @return
	 */
	@ArchOnly
	public static boolean isInited() {
		return !(apiContextThreadLocal.get() == null);
	}

	/**
	 * 仅限框架内调用
	 *
	 */
	@ArchOnly
	public static void clear() {
		apiContextThreadLocal.remove();
	}
}
