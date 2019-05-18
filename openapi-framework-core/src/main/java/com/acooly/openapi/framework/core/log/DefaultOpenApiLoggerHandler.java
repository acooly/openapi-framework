/*
 * acooly.cn Inc.
 * Copyright (c) 2016 All Rights Reserved.
 * create by zhangpu
 * date:2016年3月17日
 *
 */
package com.acooly.openapi.framework.core.log;

import com.acooly.core.utils.Strings;
import com.acooly.core.utils.ToString;
import com.acooly.openapi.framework.common.context.ApiContext;
import com.acooly.openapi.framework.common.context.ApiContextHolder;
import com.acooly.openapi.framework.common.enums.ApiBusiType;
import com.acooly.openapi.framework.common.message.ApiMessage;
import com.acooly.openapi.framework.core.OpenAPIProperties;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * OpenApi统一日志默认实现
 *
 * @author zhangpu
 */
@Component
public class DefaultOpenApiLoggerHandler implements OpenApiLoggerHandler {

    public static final String DEF_MASK_KEYS = "cardNo,idcard,mobileNo,userName";
    public static final String DEF_IGNORE_KEYS = "password,pass,passwd";
    private static final Logger logger = LoggerFactory.getLogger("ParamsLogger");
    private static final Logger apiQuerylogger = LoggerFactory.getLogger("API-QUERY");
    /**
     * 需要mask的参数key，多个使用（逗号）分隔(兼容V3)
     */
    @Value("${system.logger.maskkeys:''}")
    private String maskKeys;

    /**
     * 需要忽略的参数key，多个使用（逗号）分隔(兼容V3)
     */
    @Value("${system.logger.ignorekeys:''}")
    private String ignoreKeys;

    private Set<String> masks;
    private Set<String> ignores;

    @Autowired
    private OpenAPIProperties openAPIProperties;


    @Override
    public void log(String label, String msg) {

        if (openAPIProperties.getLogSafety()) {
            try {
                JSONObject jsonObject = JSON.parseObject(msg);
                Map map = safetyJSONObject(jsonObject);
                msg = JSON.toJSONString(map);
            } catch (Exception e) {
                // 不是jSON，ignore
            }
        }

        if (isSep()) {
            apiQuerylogger.info(StringUtils.trimToEmpty(label) + msg);
        } else {
            logger.info(StringUtils.trimToEmpty(label) + msg);
        }
    }


    protected Map safetyJSONObject(JSONObject jsonObject) {
        Map<Object, Object> newMap = Maps.newHashMap();
        for (Map.Entry<String, Object> entry : jsonObject.entrySet()) {
            if (entry.getValue() instanceof JSONObject) {
                newMap.put(entry.getKey(), safetyJSONObject((JSONObject) entry.getValue()));
            } else if (entry.getValue() instanceof JSONArray) {
                newMap.put(entry.getKey(), safetyJSONArray((JSONArray) entry.getValue()));
            } else {
                if (needIgnore(entry.getKey())) {
                    continue;
                } else if (needMask(entry.getKey())) {
                    newMap.put(entry.getKey(), ToString.mask(String.valueOf(entry.getValue())));
                } else {
                    newMap.put(entry.getKey(), entry.getValue());
                }
            }
        }
        return newMap;
    }

    protected List safetyJSONArray(JSONArray jsonArray) {
        List<Object> list = Lists.newArrayList();
        for (Object obj : jsonArray) {
            if (obj instanceof JSONObject) {
                list.add(safetyJSONObject((JSONObject) obj));
            } else if (obj instanceof JSONArray) {
                list.add(safetyJSONArray((JSONArray) obj));
            } else {
                list.add(obj);
            }
        }
        return list;
    }


    @Override
    public void log(String label, Map<String, ?> data) {
        boolean sep = isSep();
        if (data == null || data.size() == 0) {
            if (sep) {
                apiQuerylogger.info(StringUtils.trimToEmpty(label) + "{}", "{}");
            } else {
                logger.info(StringUtils.trimToEmpty(label) + "{}", "{}");
            }
            return;
        }
        Map logData = null;
        if (openAPIProperties.getLogSafety()) {
            logData = Maps.newTreeMap();
            for (Map.Entry<String, ?> entry : data.entrySet()) {
                if (entry.getValue() == null) {
                    logData.put(entry.getKey(), null);
                    continue;
                }
                if (needIgnore(entry.getKey())) {
                    continue;
                } else if (needMask(entry.getKey())) {
                    logData.put(entry.getKey(), doMask(entry.getValue()));
                } else {
                    logData.put(entry.getKey(), entry.getValue().toString());
                }
            }
        } else {
            logData = data;
        }
        if (sep) {
            apiQuerylogger.info(StringUtils.trimToEmpty(label) + JSON.toJSONString(logData));
        } else {
            logger.info(StringUtils.trimToEmpty(label) + JSON.toJSONString(logData));
        }
    }

    @Override
    public void log(String label, ApiMessage apiMessage) {

    }

    @Override
    public void log(ApiMessage apiMessage) {

    }

    private boolean isSep() {
        if (!openAPIProperties.getQueryLogSeparationEnable()) {
            return false;
        }
        ApiContext apiContext = ApiContextHolder.getApiContext();
        if (apiContext == null) {
            return false;
        }
        if (ApiContextHolder.getApiContext().getOpenApiService() == null) {
            return false;
        }
        return ApiContextHolder.getApiContext().getOpenApiService().busiType() == ApiBusiType.Query;
    }

    protected boolean needMask(String key) {
        boolean needToMask = false;
        for (String maskKey : getMasks()) {
            if (StringUtils.containsIgnoreCase(key, maskKey)) {
                needToMask = true;
                break;
            }
        }
        return needToMask;
    }

    protected boolean needIgnore(String key) {
        boolean needToIgnore = false;
        for (String k : getIgnores()) {
            if (StringUtils.containsIgnoreCase(key, k)) {
                needToIgnore = true;
                break;
            }
        }
        return needToIgnore;
    }

    protected String doMask(Object object) {
        return ToString.toString(object);
    }

    protected String doMaskIgnore(Object object) {
        return "[Ignore]";
    }

    protected Set<String> getMasks() {
        if (masks == null) {
            synchronized (this) {
                if (masks == null) {
                    masks = Sets.newHashSet(StringUtils.split(DEF_MASK_KEYS, ","));
                    if (StringUtils.isNotBlank(maskKeys)) {
                        masks.addAll(Sets.newHashSet(StringUtils.split(maskKeys, ",")));
                    }
                    if (Strings.isNoneBlank(openAPIProperties.getLogSafetyMasks())) {
                        masks.addAll(Sets.newHashSet(StringUtils.split(openAPIProperties.getLogSafetyMasks(), ",")));
                    }
                    logger.info("初始化加载 mask keys：{}", masks);
                }
            }
        }
        return masks;
    }

    protected Set<String> getIgnores() {
        if (ignores == null) {
            synchronized (this) {
                if (ignores == null) {
                    ignores = Sets.newHashSet(StringUtils.split(DEF_IGNORE_KEYS, ","));
                    if (StringUtils.isNotBlank(ignoreKeys)) {
                        ignores.addAll(Sets.newHashSet(StringUtils.split(ignoreKeys, ",")));
                    }
                    if (StringUtils.isNotBlank(openAPIProperties.getLogSafetyIgnores())) {
                        ignores.addAll(Sets.newHashSet(StringUtils.split(openAPIProperties.getLogSafetyIgnores(), ",")));
                    }
                    logger.info("初始化加载 ignore keys：{}", ignores);
                }
            }
        }
        return ignores;
    }


    public void setMaskKeys(String maskKeys) {
        this.maskKeys = maskKeys;
    }

    public void setIgnoreKeys(String ignoreKeys) {
        this.ignoreKeys = ignoreKeys;
    }

}
