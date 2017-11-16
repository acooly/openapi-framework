package com.acooly.openapi.framework.app.biz.service;

import com.acooly.core.common.service.EntityService;
import com.acooly.openapi.framework.app.biz.domain.AppVersion;

/**
 * 手机客户端版本 Service
 *
 * <p>Date: 2014-10-25 23:16:15
 *
 * @author Acooly Code Generator
 */
public interface AppVersionService extends EntityService<AppVersion> {

  AppVersion getLatest(String appCode, String deviceType);
}
