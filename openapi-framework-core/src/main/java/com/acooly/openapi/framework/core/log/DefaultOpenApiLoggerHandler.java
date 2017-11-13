/*
 * acooly.cn Inc.
 * Copyright (c) 2016 All Rights Reserved.
 * create by zhangpu
 * date:2016年3月17日
 *
 */
package com.acooly.openapi.framework.core.log;

import com.acooly.openapi.framework.common.enums.ApiBusiType;
import com.acooly.openapi.framework.common.context.ApiContext;
import com.acooly.openapi.framework.common.context.ApiContextHolder;
import com.alibaba.fastjson.JSON;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Set;

/**
 * OpenApi统一日志默认实现
 *
 * @author zhangpu
 */
@Component
public class DefaultOpenApiLoggerHandler implements OpenApiLoggerHandler, InitializingBean {

  public static final String DEF_MASK_KEYS = "cardNo,idcard,mobileNo,userName";
  public static final String DEF_IGNORE_KEYS = "password,pass,passwd";
  private static final Logger logger = LoggerFactory.getLogger("ParamsLogger");
  private static final Logger apiQuerylogger = LoggerFactory.getLogger("API-QUERY");
  /** 需要mask的参数key，多个使用（逗号）分隔 */
  @Value("${system.logger.maskkeys:''}")
  private String maskKeys;

  /** 需要忽略的参数key，多个使用（逗号）分隔 */
  @Value("${system.logger.ignorekeys:''}")
  private String ignoreKeys;

  private String maskChars = "*";

  private Set<String> masks;
  private Set<String> ignores;

  private Boolean queryLogSeparation;

  @Override
  public void log(String label, String msg) {
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
    Map<String, String> logData = Maps.newTreeMap();
    for (Map.Entry<String, ?> entry : data.entrySet()) {
      if (entry.getValue() == null) {
        logData.put(entry.getKey(), null);
        continue;
      }
      if (needIgnore(entry.getKey())) {
        logData.put(entry.getKey(), doMaskIgnore(entry.getValue()));
      } else if (needMask(entry.getKey())) {
        logData.put(entry.getKey(), doMask(entry.getValue()));
      } else {
        logData.put(entry.getKey(), entry.getValue().toString());
      }
    }
    if (sep) {
      apiQuerylogger.info(StringUtils.trimToEmpty(label) + JSON.toJSONString(logData));
    } else {
      logger.info(StringUtils.trimToEmpty(label) + JSON.toJSONString(logData));
    }
  }

  private boolean isSep() {
    if (!queryLogSeparation) {
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
    String logValue = object.toString();
    int maskLen = logValue.length() / 2;
    return StringUtils.rightPad(
        StringUtils.substring(logValue, 0, maskLen), logValue.length(), maskChars);
  }

  protected String doMaskIgnore(Object object) {
    String logValue = object.toString();
    return StringUtils.leftPad("", logValue.length(), maskChars);
  }

  protected Set<String> getMasks() {
    if (masks == null) {
      synchronized (this) {
        if (masks == null) {
          masks = Sets.newHashSet(StringUtils.split(DEF_MASK_KEYS, ","));
          if (StringUtils.isNotBlank(maskKeys)) {
            masks.addAll(Sets.newHashSet(StringUtils.split(maskKeys, ",")));
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

  @Override
  public void afterPropertiesSet() throws Exception {
    String property = System.getProperty("openapi.queryLogSeparationEnable");
    queryLogSeparation = Boolean.parseBoolean(property);
  }
}
