/*
 * acooly.cn Inc.
 * Copyright (c) 2016 All Rights Reserved.
 * create by zhangpu
 * date:2016年3月20日
 *
 */
package com.acooly.openapi.framework.service.persistent.dao;

import com.acooly.core.common.dao.DynamicPagedQueryDao;
import com.acooly.openapi.framework.domain.NotifyMessage;

import java.util.List;

/** @author zhangpu */
public interface NotifyMessageDao extends DynamicPagedQueryDao<NotifyMessage> {

  NotifyMessage get(Long id);

  void insert(NotifyMessage notifyMessage);

  void updateForManage(NotifyMessage notifyMessage);

  void updateStatus(NotifyMessage notifyMessage);

  int updateProccessingStatus(NotifyMessage notifyMessage);

  void updateProccessingStatus(List<NotifyMessage> notifyMessages);

  List<NotifyMessage> listUnProcessed(Integer topNum);
}
