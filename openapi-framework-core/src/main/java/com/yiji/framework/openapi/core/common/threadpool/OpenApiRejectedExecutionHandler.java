/*
 * www.yiji.com Inc.
 * Copyright (c) 2014 All Rights Reserved
 */

/*
 * 修订记录:
 * qzhanbo@yiji.com 2014-08-02 01:34 创建
 *
 */
package com.yiji.framework.openapi.core.common.threadpool;

import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * 如果线程池处理不过来,交给调用线程执行
 * @author qzhanbo@yiji.com
 */
public class OpenApiRejectedExecutionHandler implements RejectedExecutionHandler {
	
	public void rejectedExecution(Runnable r, ThreadPoolExecutor e) {
		if (!e.isShutdown()) {
			r.run();
		}
	}
}
