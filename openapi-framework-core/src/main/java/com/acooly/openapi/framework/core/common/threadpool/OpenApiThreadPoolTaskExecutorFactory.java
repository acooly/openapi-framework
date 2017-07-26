/*
// * acooly.com Inc.
 * Copyright (c) 2014 All Rights Reserved
 */

/*
 * 修订记录:
 * qiubo@qq.com 2017-08-02 01:46 创建
 *
 */
package com.acooly.openapi.framework.core.common.threadpool;


/**
 * @author qiubo@qq.com
 */
public class OpenApiThreadPoolTaskExecutorFactory {
	private volatile static OpenApiThreadPoolTaskExecutor threadPoolTaskExecutor = null;

	public static OpenApiThreadPoolTaskExecutor getThreadPoolTaskExecutor() {
		if (threadPoolTaskExecutor == null) {
			synchronized (OpenApiThreadPoolTaskExecutorFactory.class) {
				if (threadPoolTaskExecutor == null) {
					init();
				}
			}
		}
		return threadPoolTaskExecutor;
	}

	private static void init() {
		threadPoolTaskExecutor = new OpenApiThreadPoolTaskExecutor();
		threadPoolTaskExecutor.setCorePoolSize(1);
		threadPoolTaskExecutor.setKeepAliveSeconds(300);
		threadPoolTaskExecutor.setMaxPoolSize(8);
		threadPoolTaskExecutor.setQueueCapacity(10);
		// threadPoolTaskExecutor.setAwaitTerminationSeconds(30);
		threadPoolTaskExecutor.setWaitForTasksToCompleteOnShutdown(true);
		threadPoolTaskExecutor.setAllowCoreThreadTimeOut(true);
		threadPoolTaskExecutor.setDaemon(false);
		threadPoolTaskExecutor
				.setRejectedExecutionHandler(new OpenApiRejectedExecutionHandler());
		threadPoolTaskExecutor.setThreadNamePrefix("openapi-listener-");
		threadPoolTaskExecutor.initialize();
		// ShutdownHooks.addShutdownHook(new Runnable() {
		// @Override
		// public void run() {
		// threadPoolTaskExecutor.destroy();
		// }
		// },"openapiThreadpoolShutDownHook");
	}

}
