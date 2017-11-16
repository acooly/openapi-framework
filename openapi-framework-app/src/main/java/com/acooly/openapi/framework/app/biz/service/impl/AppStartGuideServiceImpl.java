package com.acooly.openapi.framework.app.biz.service.impl;

import com.acooly.core.common.service.EntityServiceImpl;
import com.acooly.openapi.framework.app.biz.dao.AppStartGuideDao;
import com.acooly.openapi.framework.app.biz.domain.AppStartGuide;
import com.acooly.openapi.framework.app.biz.enums.EntityStatus;
import com.acooly.openapi.framework.app.biz.service.AppStartGuideService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("appStartGuideService")
public class AppStartGuideServiceImpl extends EntityServiceImpl<AppStartGuide, AppStartGuideDao>
    implements AppStartGuideService {

  @Override
  public List<AppStartGuide> loadValidGuides() {
    return getEntityDao().findByStatusOrderBySortOrderAsc(EntityStatus.Enable);
  }
}
