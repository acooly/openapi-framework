/*
 * acooly.cn Inc.
 * Copyright (c) 2016 All Rights Reserved.
 * create by zhangpu
 * date:2016年3月17日
 */
package com.acooly.openapi.framework.core.auth.realm;

import java.util.List;

/**
 * AuthInfoRealm 简单接口
 *
 * @author zhangpu
 */
public interface SimpleAuthInfoRealm {
  /**
   * 获取安全校验码
   *
   * @param accessKey
   * @return
   */
  String getSecretKey(String accessKey);

  /**
   * 获取授权的服务列表.
   *
   * @param accessKey
   * @return
   */
  List<String> getAuthorizedServices(String accessKey);
}
