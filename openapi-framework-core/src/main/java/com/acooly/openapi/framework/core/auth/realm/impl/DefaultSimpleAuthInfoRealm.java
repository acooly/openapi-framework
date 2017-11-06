/*
 * acooly.cn Inc.
 * Copyright (c) 2016 All Rights Reserved.
 * create by zhangpu
 * date:2016年3月17日
 */
package com.acooly.openapi.framework.core.auth.realm.impl;

import com.acooly.openapi.framework.core.OpenApiConstants;
import com.acooly.openapi.framework.core.auth.realm.SimpleAuthInfoRealm;
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
public class DefaultSimpleAuthInfoRealm implements SimpleAuthInfoRealm {

  /**
   * 获取商户安全校验码
   *
   * @param accessKey
   * @return
   */
  @Override
  public String getSecretKey(String accessKey) {
    return OpenApiConstants.DEF_SECRETKEY;
  }

  /**
   * 获取商户授权的权限列表.
   *
   * @param accessKey
   * @return
   */
  public List<String> getAuthorizedServices(String accessKey) {
    return Lists.newArrayList(OpenApiConstants.WILDCARD_TOKEN);
  }
}
