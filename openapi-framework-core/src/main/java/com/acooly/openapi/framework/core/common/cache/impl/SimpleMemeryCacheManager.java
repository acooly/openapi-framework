/*
 * acooly.com Inc.
 * Copyright (c) 2014 All Rights Reserved
 */
package com.acooly.openapi.framework.core.common.cache.impl;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.acooly.openapi.framework.core.common.cache.CacheManager;

/**
 * 本地内存缓存实现
 * 
 * @author zhangpu
 * @date 2014年6月27日
 */
@Component
public class SimpleMemeryCacheManager implements CacheManager {

	private static Logger logger = LoggerFactory.getLogger(SimpleMemeryCacheManager.class);

	/** 默认有效期（秒）1小时 */
	protected int DEFAILT_HOLD_SECOND = 15 * 60;

	/** 成员总数操作阈值，则清理过期数据 */
	protected int CLEAN_THRESHOLD_SIZE = 1000;

	protected Map<String, CacheValue> container = new ConcurrentHashMap<String, CacheValue>();

	@Override
	public void add(String key, Object value) {
		add(key, value, DEFAILT_HOLD_SECOND);
	}

	@Override
	public void add(String key, Object value, int holdSecond) {
		long validTime = System.currentTimeMillis() + holdSecond * 1000;
		container.put(key, new CacheValue(value, validTime));
		logger.debug("add key:{},value:{}, validTime:{}", key, value, validTime);
		clean();
	}

	@Override
	public Object get(String key) {
		CacheValue c = container.get(key);
		if (c == null) {
			return null;
		}
		if (c.expired()) {
			container.remove(key);
			return null;
		}
		clean();
		return c.getValue();
	}

	@Override
	public void cleanup(String key) {
		if (container.get(key) != null) {
			container.remove(key);
			logger.info("缓存清理完成: key:{}" + key);
		}
	}

	@Override
	public void cleanup() {
		int size = container.size();
		container.clear();
		logger.info("缓存清理完成: {}" + size);
	}

	protected void clean() {
		if (container.size() < CLEAN_THRESHOLD_SIZE) {
			return;
		}
		int cleanCount = 0;
		for (Map.Entry<String, CacheValue> entry : container.entrySet()) {
			if (entry.getValue().expired()) {
				container.remove(entry.getKey());
				cleanCount++;
			}
		}
		logger.info("内存缓存清理:size:{},clean:{}", container.size(), cleanCount);
	}

	static class CacheValue {
		private Object value;
		/** 有效期 */
		private long validTime;

		/**
		 * @param value
		 * @param validTime
		 */
		public CacheValue(Object value, long validTime) {
			super();
			this.value = value;
			this.validTime = validTime;
		}

		public Object getValue() {
			return value;
		}

		public void setValue(Object value) {
			this.value = value;
		}

		public long getValidTime() {
			return validTime;
		}

		public void setValidTime(long validTime) {
			this.validTime = validTime;
		}

		private boolean expired() {
			return validTime < System.currentTimeMillis();
		}
	}

	public static void main(String[] args) throws Exception {
		CacheManager cacheManager = new SimpleMemeryCacheManager();
		for (int i = 1; i <= 12; i++) {
			if (i == 10) {
				Thread.sleep(5000);
			}
			cacheManager.add(String.valueOf(i), i, 1);
		}
	}

}
