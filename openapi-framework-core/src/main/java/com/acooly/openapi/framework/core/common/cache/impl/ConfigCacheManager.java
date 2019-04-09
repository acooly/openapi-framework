package com.acooly.openapi.framework.core.common.cache.impl;

import com.acooly.module.config.entity.AppConfig;
import com.acooly.module.config.service.impl.AppConfigManager;
import com.acooly.openapi.framework.core.common.cache.CacheManager;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.ParserConfig;
import com.alibaba.fastjson.serializer.SerializerFeature;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * @author qiuboboy@qq.com
 * @date 2018-07-23 16:16
 */
public class ConfigCacheManager implements CacheManager, InitializingBean {
    private static final String NAME_SPACE = "openapi.cache.";

    @Autowired
    private AppConfigManager configManager;


    private Integer defaultTimeout;

    public ConfigCacheManager(Integer defaultTimeout) {
        this.defaultTimeout = defaultTimeout;
    }

    @Override
    public void add(String key, Object value) {
        add(key, value, defaultTimeout / 1000);
    }

    @Override
    public void add(String key, Object value, int holdSecond) {
        AppConfig config = new AppConfig();
        config.setConfigName(NAME_SPACE + key);
        if (value instanceof String) {
            config.setConfigValue(value.toString());
            config.setComments("api缓存");
        } else {
            config.setConfigValue(JSON.toJSONString(value, SerializerFeature.WriteClassName));
            config.setComments("api缓存:json");
        }
        config.setLocalCacheExpire(holdSecond * 1000);
        config.setRedisCacheExpire(holdSecond * 1000);
        configManager.create(config);
    }

    @Override
    public Object get(String key) {
        AppConfig appConfig = configManager.getAppConfig(NAME_SPACE + key);
        if (appConfig != null) {
            if (appConfig.getComments().contains("json")) {
                return JSON.parse(appConfig.getConfigValue());
            } else {
                return appConfig.getConfigValue();
            }
        }
        return null;
    }

    @Override
    public void cleanup(String key) {
        configManager.invalidate(NAME_SPACE + key);
    }


    @Override
    public void afterPropertiesSet() throws Exception {
        ParserConfig.getGlobalInstance().addAccept("com.acooly.");
    }
}
