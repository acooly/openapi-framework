/*
 * acooly.com Inc.
 * Copyright (c) 2014 All Rights Reserved
 */

/*
 * 修订记录:
 * qiubo@qq.com 2017-07-31 12:31 创建
 *
 */
package com.acooly.openapi.framework.core.listener.multicaster;

import com.acooly.openapi.framework.core.listener.OpenApiEventMulticaster;
import com.acooly.openapi.framework.core.listener.SmartListenter;
import com.google.common.collect.Lists;
import com.acooly.openapi.framework.core.common.threadpool.OpenApiThreadPoolTaskExecutorFactory;
import com.acooly.openapi.framework.core.listener.ApiListener;
import com.acooly.openapi.framework.core.listener.event.OpenApiEvent;
import com.acooly.openapi.framework.core.meta.OpenApiListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.task.TaskExecutor;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * 事件分发器.
 * @author qiubo@qq.com
 */
public class AbstractOpenApiEventMulticaster<E extends OpenApiEvent> implements
        OpenApiEventMulticaster<E> {
	private static final Logger logger = LoggerFactory
		.getLogger(AbstractOpenApiEventMulticaster.class);
	
	/**
	 * 设置监听器过多阀值警告,请确认是否在服务执行方法中设置监听器后正确删除监听器.请尽量在服务初始化时设置监听器
	 */
	private static final int INTERCEPTORS_THRESHOLD = 10;
	
	private List<ApiListener<E>> listeners = Lists.newCopyOnWriteArrayList();
	
	private static TaskExecutor taskExecutor = OpenApiThreadPoolTaskExecutorFactory
		.getThreadPoolTaskExecutor();
	
	@Override
	public void addListener(ApiListener<E> listener) {
		if (listener == null) {
			return;
		}
		
		if (listeners.contains(listener)) {
			logger.warn("服务:{} 重复设置拦截器:{}", this.getClass().getName(), listener.getClass()
				.getName());
			return;
		}
		listeners.add(listener);
		sort();
		if (listeners.size() > INTERCEPTORS_THRESHOLD) {
			logger.warn("设置监听器超过阀值警告,请确认是否在服务执行方法中设置监听器后正确删除监听器.请尽量在服务初始化时设置监听器");
		}
	}
	
	@Override
	public void removeListener(ApiListener<E> listener) {
		if (listener == null || listeners.isEmpty()) {
			return;
		}
		listeners.remove(listener);
		sort();
	}
	
	@Override
	public void removeAllListeners() {
		listeners.clear();
	}
	
	@Override
	public void publishEvent(final E event) {
		if (event == null || !canPublish()) {
			return;
		}
		if (accept(event)) {
			for (final ApiListener listener : listeners) {
				if (listener instanceof SmartListenter) {
					boolean support = ((SmartListenter) listener).supportsEventType(event
						.getClass());
					if (!support) {
						continue;
					}
					OpenApiListener openApiListener = listener.getClass().getAnnotation(
						OpenApiListener.class);
					if (openApiListener != null && openApiListener.asyn()) {
						taskExecutor.execute(new Runnable() {
							@Override
							public void run() {
								listener.onOpenApiEvent(event);
							}
						});
					} else {
						listener.onOpenApiEvent(event);
					}
				}
			}
		}
	}
	
	@Override
	public boolean canPublish() {
		return !listeners.isEmpty();
	}
	
	protected boolean accept(E event) {
		return true;
	}
	
	private void sort() {
		//监听器数量小于2不用排序
		if (listeners == null || listeners.size() < 2) {
			return;
		}
		Collections.sort(listeners, new Comparator<ApiListener>() {
			@Override
			public int compare(ApiListener o1, ApiListener o2) {
				return o1.getOrder() - o2.getOrder();
			}
		});
	}
}
