/*
 * acooly.com Inc.
 * Copyright (c) 2014 All Rights Reserved
 */

/*
 * 修订记录:
 * qiubo@qq.com 2017-08-02 01:34 创建
 *
 */
package com.acooly.openapi.framework.core.common.threadpool;

import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * 如果线程池处理不过来,交给调用线程执行
 * @author qiubo@qq.com
 */
public class OpenApiRejectedExecutionHandler implements RejectedExecutionHandler {
	
	public void rejectedExecution(Runnable r, ThreadPoolExecutor e) {
		if (!e.isShutdown()) {
			r.run();
		}
	}
}
