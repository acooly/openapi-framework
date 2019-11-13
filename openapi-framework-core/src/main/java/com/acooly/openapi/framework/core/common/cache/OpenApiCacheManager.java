/*
 * acooly.com Inc.
 * Copyright (c) 2014 All Rights Reserved
 */
package com.acooly.openapi.framework.core.common.cache;

/**
 * 缓存接口
 *
 * @author qiubo
 * @author zhangpu
 * @date 2014年6月27日
 */
public interface OpenApiCacheManager {


    /**
     * 添加缓存
     *
     * @param key
     * @param value
     * @param holdSecond 秒
     */
    void add(String key, Object value, int holdSecond);

    /**
     * 添加缓存（默认配置时长）
     *
     * @param key
     * @param value
     */
    void add(String key, Object value);

    /**
     * 获取缓存
     *
     * @param key
     * @return
     */
    Object get(String key);

    /**
     * 清理缓存
     *
     * @param key
     */
    void cleanup(String key);

}
