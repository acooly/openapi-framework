/*
 * www.yiji.com Inc.
 * Copyright (c) 2014 All Rights Reserved
 */

/*
 * 修订记录:
 * qzhanbo@yiji.com 2014-08-02 01:14 创建
 *
 */
package com.yiji.framework.openapi.core.common.threadpool;

import com.yiji.framework.openapi.common.ApiConstants;
import org.apache.log4j.MDC;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import com.yiji.framework.openapi.core.executer.ApiContext;
import com.yiji.framework.openapi.core.executer.ApiContextHolder;

/**
 * @author qzhanbo@yiji.com
 */
public class OpenApiThreadPoolTaskExecutor extends ThreadPoolTaskExecutor {

	@Override
	public void execute(final Runnable task) {
		final String gid = (String) MDC.get(ApiConstants.GID);
		final ApiContext apiContext = ApiContextHolder.getApiContext();
		final String callerThreadName = Thread.currentThread().getName();
		super.execute(new Runnable() {
			@Override
			public void run() {
				if (gid != null) {
					MDC.put(ApiConstants.GID, gid);
				}
				if (apiContext != null) {
					ApiContextHolder.setApiContext(apiContext);
				}
				try {
					task.run();
				} finally {
					// 如果线程池队列满后,由当提交任务线程执行任务不能清空线程变量
					String currentThreadName = Thread.currentThread().getName();
					if (!currentThreadName.equals(callerThreadName)) {
						ApiContextHolder.clear();
					}
				}
			}
		});
	}
}
