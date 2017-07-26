/*
 * acooly.com Inc.
 * Copyright (c) 2014 All Rights Reserved
 */

/*
 * 修订记录:
 * qiubo@qq.com 2017-07-31 16:02 创建
 *
 */
package com.acooly.openapi.framework.core.listener;

import com.acooly.openapi.framework.core.listener.event.OpenApiEvent;
import com.acooly.openapi.framework.core.util.GenericsUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author qiubo@qq.com
 */
public abstract class AbstractListener<E extends OpenApiEvent> implements SmartListenter<E> {
	protected final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	private Class<E> clazz;
	
	public AbstractListener() {
		Class clazz = GenericsUtils.getSuperClassGenricType(getClass(), 0);
		if (clazz.equals(Object.class)) {
			throw new RuntimeException("监听器:"
																	+ this.getClass().getName()
																	+ "的类型参数必须是"
																	+ OpenApiEvent.class.getName()
																	+ "的子类");
		}
		this.clazz = clazz;
	}
	
	@Override
	public boolean supportsEventType(Class<E> eventType) {
		if (eventType == null) {
			return false;
		}
		return clazz.isAssignableFrom(eventType);
	}
}
