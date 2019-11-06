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
import com.acooly.openapi.framework.common.utils.ApiUtils;
import com.acooly.openapi.framework.core.OpenAPIProperties;
import com.alibaba.fastjson.JSON;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.annotation.Annotation;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
     * json正则解析表达式.
     * 1、group1-3:数组"xxx":[]
     * 2、group4-6:对象"xxx":{}
     * 3、group7-9:非数字"xxx":"yyy"
     * 4、group10-12:数字"xxx":yyy
     */
    private static final String JSON_REGEX = "(\"([^\"]+)\":(\\[.*?\\]))|" +
            "(\"([^\"]+)\":(\\{.*?\\}))|" +
            "(\"([^\"]+)\":\"([^\"]+)\")|" +
            "(\"([^\"]+)\":([^\",,^\\}]+))";

    private Set<String> masks;
    private Set<String> ignores;

    @Autowired
    private OpenAPIProperties openAPIProperties;


    @Override
    public void log(String label, String msg) {
        log(label, null, msg);
    }

    @Override
    public void log(String label, ApiMessage apiMessage, String msg, Map<String, String> headers, String ext) {
        long start = System.currentTimeMillis();
        if (openAPIProperties.getLogSafety()) {
            if (ApiUtils.isJson(msg)) {
                if (apiMessage == null) {
                    msg = safetyJson(msg, null);
                } else {
                    msg = safetyJson(msg, SafetyLog.getSafetyProperties(apiMessage.getClass()));
                }
                logger.debug("safety-log: {}ms", (System.currentTimeMillis() - start));
            }
        }

        if (isSep()) {
            apiQuerylogger.info("{} {}, headers: {}, {}", StringUtils.trimToEmpty(label), msg, headers, StringUtils.trimToEmpty(ext));
        } else {
            logger.info("{} {}, headers: {}, {}", StringUtils.trimToEmpty(label), msg, headers, StringUtils.trimToEmpty(ext));
        }
    }

    @Override
    public void log(String label, ApiMessage apiMessage, String msg) {
        log(label, apiMessage, msg, null, null);
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
        return needMask(key, null);
    }

    protected boolean needMask(String key, Map<String, Annotation> safetyProperties) {
        boolean needToMask = false;
        for (String maskKey : getMasks()) {
            if (StringUtils.containsIgnoreCase(key, maskKey)) {
                needToMask = true;
                break;
            }
        }

        if (safetyProperties != null) {
            Annotation annotation = safetyProperties.get(key);
            if (annotation != null && ToString.Maskable.class.isAssignableFrom(annotation.getClass())) {
                needToMask = true;
            }
        }
        return needToMask;
    }

    protected boolean needIgnore(String key) {
        return needIgnore(key, null);
    }

    protected boolean needIgnore(String key, Map<String, Annotation> safetyProperties) {
        boolean needToIgnore = false;
        for (String k : getIgnores()) {
            if (StringUtils.containsIgnoreCase(key, k)) {
                needToIgnore = true;
                break;
            }
        }

        if (safetyProperties != null) {
            Annotation annotation = safetyProperties.get(key);
            if (annotation != null && ToString.Invisible.class.isAssignableFrom(annotation.getClass())) {
                needToIgnore = true;
            }
        }
        return needToIgnore;
    }

    protected String doMask(Object object) {
        return ToString.mask(object.toString());
    }

    protected String doMaskIgnore(Object object) {
        return "<Ignore>";
    }

    protected Set<String> getMasks() {
        if (masks == null) {
            synchronized (this) {
                if (masks == null) {
                    masks = Sets.newHashSet(StringUtils.split(DEF_MASK_KEYS, ","));
                    if (Strings.isNoneBlank(openAPIProperties.getLogSafetyMasks())) {
                        masks.addAll(Sets.newHashSet(StringUtils.split(openAPIProperties.getLogSafetyMasks(), ",")));
                    }
                    logger.info("初始化全局 safetylog-mask keys：{}", masks);
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
                    if (StringUtils.isNotBlank(openAPIProperties.getLogSafetyIgnores())) {
                        ignores.addAll(Sets.newHashSet(StringUtils.split(openAPIProperties.getLogSafetyIgnores(), ",")));
                    }
                    logger.info("初始化全局 safetylog-ignore keys：{}", ignores);
                }
            }
        }
        return ignores;
    }


    private String safetyJson(String json, Map<String, Annotation> safetyProperties) {
        if (Strings.isBlank(json)) {
            return Strings.trimToEmpty(json);
        }
        Pattern pattern = Pattern.compile(JSON_REGEX);
        Matcher matcher = pattern.matcher(json);
        String str = null;
        while (matcher.find()) {
            if (Strings.isNotBlank(matcher.group(1))) {
                // 数组集合，递归处理
                json = Strings.replace(json, matcher.group(1), "\"" + matcher.group(2) + "\":" + safetyJson(matcher.group(3), safetyProperties));
            } else if (Strings.isNotBlank(matcher.group(4))) {
                json = Strings.replace(json, matcher.group(4), "\"" + matcher.group(5) + "\":" + safetyJson(matcher.group(6), safetyProperties));
            } else if (Strings.isNotBlank(matcher.group(7))) {
                str = matcher.group(7);
                json = safetyReplace(json, matcher.group(7), matcher.group(8), matcher.group(9), false, safetyProperties);
            } else if (Strings.isNotBlank(matcher.group(10))) {
                str = matcher.group(10);
                json = safetyReplace(json, matcher.group(10), matcher.group(11), matcher.group(12), true, safetyProperties);
            }
        }
        return json;
    }

    private String safetyReplace(String json, String str, String key, String value, boolean isNumber, Map<String, Annotation> safetyProperties) {
        if (needIgnore(key, safetyProperties)) {
            json = Strings.replace(json, str, "\"" + key + "\":" + (isNumber ? doMask(value) : "\"" + doMaskIgnore(value) + "\""));
        } else if (needMask(key, safetyProperties)) {
            json = Strings.replace(json, str, "\"" + key + "\":" + (isNumber ? doMask(value) : "\"" + doMask(value) + "\""));
        }
        return json;
    }
}
