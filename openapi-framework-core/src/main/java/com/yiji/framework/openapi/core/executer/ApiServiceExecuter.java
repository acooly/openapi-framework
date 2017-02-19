/*
 * www.yiji.com Inc.
 * Copyright (c) 2014 All Rights Reserved
 */
package com.yiji.framework.openapi.core.executer;

/**
 * 服务框架执行层接口
 * @author zhangpu
 * @date 2014年5月3日
 * @param <R>
 * @param <T>
 */
public interface ApiServiceExecuter<R, T> {
	
	void execute(R request, T response);
	
}
