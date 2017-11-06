/*
 * acooly.cn Inc.
 * Copyright (c) 2016 All Rights Reserved.
 * create by zhangpu
 * date:2016年3月17日
 */
package com.acooly.openapi.framework.core.auth.realm.impl;

import com.acooly.openapi.framework.common.enums.ApiServiceResultCode;
import com.acooly.openapi.framework.common.exception.ApiServiceException;
import com.acooly.openapi.framework.core.OpenApiConstants;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;

import java.util.List;

/**
 * 框架AuthInfoRealm默认实现
 *
 * <p>常量实现.
 *
 * @author qiubo@qq.com
 * @author acooly
 */
public class DefaultAuthInfoRealm extends CacheableAuthInfoRealm {

  private static void parseProductions(List<String> list, String productions) {
    if (!Strings.isNullOrEmpty(productions)) {
      String[] prods = productions.split(",");
      for (String prod : prods) {
        if (!Strings.isNullOrEmpty(prod)) {
          list.add(prod);
        }
      }
    }
  }

  /**
   * 获取商户安全校验码
   *
   * @param partnerId
   * @return
   */
  public String getSecretKey(String partnerId) {
    try {
      return OpenApiConstants.DEF_SECRETKEY;
    } catch (Exception e) {
      logger.error("获取用户安全校验码失败", e);
      throw new ApiServiceException(
          ApiServiceResultCode.INTERNAL_ERROR, "获取用户安全校验码失败:" + e.getMessage());
    }
  }

  /**
   * 获取商户授权的权限列表.
   *
   * @param partnerId
   * @return
   */
  public List<String> getAuthorizedServices(String partnerId) {

    try {
      List<String> list = Lists.newArrayList();
      String productions = "*";
      parseProductions(list, productions);
      return list;
    } catch (Exception e) {
      logger.error("获取用户产品列表失败", e);
      throw new ApiServiceException(
          ApiServiceResultCode.INTERNAL_ERROR, "获取用户产品列表失败:" + e.getMessage());
    }
  }
}
