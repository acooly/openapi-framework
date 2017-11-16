package com.acooly.openapi.framework.app.biz.service.impl;

import com.acooly.core.common.dao.support.PageInfo;
import com.acooly.core.common.service.EntityServiceImpl;
import com.acooly.openapi.framework.app.biz.dao.AppMessageDao;
import com.acooly.openapi.framework.app.biz.domain.AppMessage;
import com.acooly.openapi.framework.app.biz.service.AppMessageService;
import org.springframework.stereotype.Service;

@Service("appMessageService")
public class AppMessageServiceImpl extends EntityServiceImpl<AppMessage, AppMessageDao>
    implements AppMessageService {

  @Override
  public PageInfo<AppMessage> query(PageInfo<AppMessage> pageInfo, String userName) {
    return getEntityDao().pageQueryUserMessages(pageInfo, userName);
  }
}
