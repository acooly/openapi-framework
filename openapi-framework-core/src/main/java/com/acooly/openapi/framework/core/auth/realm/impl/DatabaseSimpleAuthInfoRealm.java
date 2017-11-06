/*
 * acooly.com Inc.
 * Copyright (c) 2016 All Rights Reserved
 */

/*
 * 修订记录:
 * kuli@yiji.com 2016-07-31 03:17 创建
 */
package com.acooly.openapi.framework.core.auth.realm.impl;

import com.acooly.openapi.framework.core.auth.realm.SimpleAuthInfoRealm;
import com.acooly.openapi.framework.service.ApiPartnerService;
import com.acooly.openapi.framework.service.ApiPartnerServiceService;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

/** @author acooly 默认实现:直接读取数据库实现 */
@Component
public class DatabaseSimpleAuthInfoRealm implements SimpleAuthInfoRealm {

  @Resource private ApiPartnerService apiPartnerService;

  @Resource private ApiPartnerServiceService apiPartnerServiceService;

  @Override
  public String getSecretKey(String accessKey) {
    return apiPartnerService.getPartnerSercretKey(accessKey);
  }

  @Override
  public List<String> getAuthorizedServices(String accessKey) {
    return apiPartnerServiceService.getAuthorizedServices(accessKey);
  }
}
