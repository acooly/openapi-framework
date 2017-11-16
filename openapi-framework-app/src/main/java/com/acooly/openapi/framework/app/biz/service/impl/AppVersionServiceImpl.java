package com.acooly.openapi.framework.app.biz.service.impl;

import com.acooly.core.common.service.EntityServiceImpl;
import com.acooly.openapi.framework.app.biz.dao.AppVersionDao;
import com.acooly.openapi.framework.app.biz.domain.AppVersion;
import com.acooly.openapi.framework.app.biz.service.AppVersionService;
import org.springframework.stereotype.Service;

@Service("appVersionService")
public class AppVersionServiceImpl extends EntityServiceImpl<AppVersion, AppVersionDao>
    implements AppVersionService {

  @Override
  public AppVersion getLatest(String appCode, String deviceType) {
    return getEntityDao().getLatest(appCode, deviceType);
  }
}
