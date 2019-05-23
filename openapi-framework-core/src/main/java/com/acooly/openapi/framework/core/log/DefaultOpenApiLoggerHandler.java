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
import com.acooly.openapi.framework.core.util.Pair;
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

import java.lang.annotation.Annotation;
import java.util.List;
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
        log(label, null, msg);
    }


    @Override
    public void log(String label, ApiMessage apiMessage, String msg) {
        if (openAPIProperties.getLogSafety()) {
            if (isJson(msg)) {
                if (apiMessage == null) {
                    msg = safetyJson(msg, null);
                } else {
                    msg = safetyJson(msg, SafetyLog.getSafetyProperties(apiMessage.getClass()));
                }

            }
        }
        if (isSep()) {
            apiQuerylogger.info(StringUtils.trimToEmpty(label) + msg);
        } else {
            logger.info(StringUtils.trimToEmpty(label) + msg);
        }

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


    private boolean isJson(String json) {
        try {
            JSON.parse(json);
            if ((Strings.startsWith(json, "[")
                    && Strings.endsWith(json, "]")) || (Strings.startsWith(json, "{")
                    && Strings.endsWith(json, "}"))) {
                return true;
            }
        } catch (Exception e) {
            //ig
        }
        return false;
    }

    private String safetyJson(String json, Map<String, Annotation> safetyProperties) {
        Pattern pattern = Pattern.compile("\"(.*?)\":\"(.*?)\"");
        Matcher matcher = pattern.matcher(json);
        String str = null;
        while (matcher.find()) {
            str = matcher.group();
            Pair<String, String> pair = getJsonPair(str);
            if (needIgnore(pair.getF(), safetyProperties)) {
                json = Strings.replace(json, str, "\"" + pair.getF() + "\":\"" + doMaskIgnore(pair.getS()) + "\"");
            } else if (needMask(pair.getF(), safetyProperties)) {
                json = Strings.replace(json, str, "\"" + pair.getF() + "\":\"" + doMask(pair.getS()) + "\"");
            }
        }
        return json;
    }

    private Pair<String, String> getJsonPair(String str) {
        String[] keyPair = Strings.split(str, ":");
        String key = null;
        String val = null;
        if (keyPair.length > 0) {
            key = keyPair[0];
            if (Strings.startsWith(key, "\"")) {
                key = Strings.removeStart(key, "\"");
            }
            if (Strings.endsWith(key, "\"")) {
                key = Strings.removeEnd(key, "\"");
            }
        }
        if (keyPair.length > 1) {
            val = keyPair[1];
            if (Strings.startsWith(val, "\"")) {
                val = Strings.removeStart(val, "\"");
            }
            if (Strings.endsWith(val, "\"")) {
                val = Strings.removeEnd(val, "\"");
            }
        }
        return Pair.build(key, val);
    }

    public void setMaskKeys(String maskKeys) {
        this.maskKeys = maskKeys;
    }

    public void setIgnoreKeys(String ignoreKeys) {
        this.ignoreKeys = ignoreKeys;
    }


    public static void main(String[] args) {
        String json = "{\"amount\":\"[Ignore]\",\"buyerCertNo\":\"330702********5014\",\"buyerUserId\":\"09876543211234567890\"," +
                "\"buyeryEmail\":\"qiuboboy@qq.com\",\"buyeryMobileNo\":\"138*****453\",\"context\":\"778b7e37-fd45-47a4-9be9-a36d8af7e465\",\"ext\":{\"xx\":\"oo\"}," +
                "\"goodsInfos\":[{\"goodType\":\"actual\",\"name\":\"[Ignore]\",\"price\":\"400.00\",\"quantity\":0,\"referUrl\":\"http://acooly.cn/tianzi\"}],\"merchOrderNo\":\"19052318200108200001\",\"partnerId\":\"test\",\"password\":\"[Ignore]\",\"payeeUserId\":\"[Ignore]\",\"payerUserId\":\"09876543211234567890\",\"protocol\":\"JSON\",\"requestNo\":\"19052318200108200000\",\"service\":\"orderCreate\",\"title\":\"同步请求创建******哥\uD83D\uDC3E \",\"version\":\"1.0\"}";

        Pattern pattern = Pattern.compile("(\".*?\":\\[}.*?,)|(\".*?\":.*?,)|(\".*?\":\".*?\")");
        Matcher matcher = pattern.matcher(json);
        while (matcher.find()) {
            System.out.println(matcher.group(1));
        }

    }
}
