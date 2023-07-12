/**
 * openapi-framework
 * <p>
 * Copyright 2014 Acooly.cn, Inc. All rights reserved.
 *
 * @author zhangpu
 * @date 2023-07-10 14:44
 */
package com.acooly.openapi.test.cache;

import com.acooly.openapi.framework.core.OpenAPIProperties;
import com.acooly.openapi.framework.core.common.cache.impl.MultiLevelOpenApiCacheManager;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;

/**
 * @author zhangpu
 * @date 2023-07-10 14:44
 */
@Slf4j
public class OpenApiCacheTest {

    MultiLevelOpenApiCacheManager openApiCacheManager;

    OpenAPIProperties openAPIProperties;

    @Test
    public void test() {
        // 1. 测试本地缓存
        String key = "testKey";
        String value = "testValue";
        openApiCacheManager.add(key, value);
        Object result = openApiCacheManager.get(key);
        assert result.equals(value) : "本地缓存无效";
        // 等待一段时间：本地缓存过期时间+1s
        try {
            for (int i = 1; i <= (openAPIProperties.getAuthInfoCache().getLevelOneTimeout() + 1000) / 1000; i++) {
                result = openApiCacheManager.get(key);
                System.out.println("等待本地缓存过期：" + i + "s。 本地缓存过期:" + !value.equals(result));
                Thread.sleep(1000);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        result = openApiCacheManager.get(key);
        assert result == null : "本地缓存过期测试未通过";
    }


    /**
     * 测试初始化
     *
     * @throws Exception
     */
    @Before
    public void before() throws Exception {
        openAPIProperties = new OpenAPIProperties();
        OpenAPIProperties.AuthInfoCache authInfoCache = new OpenAPIProperties.AuthInfoCache();
        authInfoCache.setLevelOneTimeout(1000 * 20);
        openAPIProperties.setAuthInfoCache(authInfoCache);
        openApiCacheManager = new MultiLevelOpenApiCacheManager();
        openApiCacheManager.setOpenAPIProperties(openAPIProperties);
        openApiCacheManager.afterPropertiesSet();

    }

}
