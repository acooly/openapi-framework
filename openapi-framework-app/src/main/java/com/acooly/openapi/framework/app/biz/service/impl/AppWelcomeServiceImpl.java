package com.acooly.openapi.framework.app.biz.service.impl;

import com.acooly.core.common.service.EntityServiceImpl;
import com.acooly.openapi.framework.app.biz.dao.AppWelcomeDao;
import com.acooly.openapi.framework.app.biz.domain.AppWelcome;
import com.acooly.openapi.framework.app.biz.service.AppWelcomeService;
import org.springframework.stereotype.Service;

@Service("appWelcomeService")
public class AppWelcomeServiceImpl extends EntityServiceImpl<AppWelcome, AppWelcomeDao>
    implements AppWelcomeService {

  @Override
  public AppWelcome getLatestOne() {
    return getEntityDao().getLatestOne();
  }
}
