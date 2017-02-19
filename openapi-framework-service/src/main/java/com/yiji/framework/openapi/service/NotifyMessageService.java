/*
 * acooly.cn Inc.
 * Copyright (c) 2016 All Rights Reserved.
 * create by zhangpu 
 * date:2016年3月28日
 *
 */
package com.yiji.framework.openapi.service;

import java.util.List;
import java.util.Map;

import com.acooly.core.common.dao.support.PageInfo;
import com.yiji.framework.openapi.domain.NotifyMessage;

/**
 * @author zhangpu
 */
public interface NotifyMessageService {

    NotifyMessage get(Long id);

	void insert(NotifyMessage notifyMessage);

	void updateForManage(NotifyMessage notifyMessage);

	void updateStatus(NotifyMessage notifyMessage);

	int updateProccessingStatus(NotifyMessage notifyMessage);

	void updateProccessingStatus(List<NotifyMessage> notifyMessages);

	List<NotifyMessage> listUnProcessed(Integer topNum);

	PageInfo<NotifyMessage> query(PageInfo<NotifyMessage> pageInfo, Map<String, Object> map,
			Map<String, Boolean> orderMap);

}
