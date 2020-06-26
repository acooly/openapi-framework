/*
 * acooly.com Inc.
 * Copyright (c) 2016 All Rights Reserved
 */

/*
 * 修订记录:
 * kuli@yiji.com 2016-07-26 19:01 创建
 */
package com.acooly.openapi.framework.service.service.impl;

import com.acooly.core.common.dao.support.PageInfo;
import com.acooly.core.common.exception.BusinessException;
import com.acooly.openapi.framework.common.enums.TaskExecuteStatus;
import com.acooly.openapi.framework.common.enums.TaskStatus;
import com.acooly.openapi.framework.service.dao.NotifyMessageDao;
import com.acooly.openapi.framework.service.domain.NotifyMessage;
import com.acooly.openapi.framework.service.service.NotifyMessageService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 通知消息 持久化管理
 *
 * @author acooly
 */
@Service("notifyMessageService")
public class NotifyMessageServiceImpl implements NotifyMessageService {

    @Resource
    private NotifyMessageDao notifyMessageDao;

    @Override
    public NotifyMessage get(Long id) {
        return notifyMessageDao.get(id);
    }

    @Override
    public void insert(NotifyMessage notifyMessage) {
        notifyMessageDao.insert(notifyMessage);
    }

    @Override
    public void updateStatus(NotifyMessage notifyMessage) {
        notifyMessageDao.updateStatus(notifyMessage);
    }

    @Override
    public void updateForRetrySend(Long id) {
        NotifyMessage message = notifyMessageDao.get(id);
        if (message.getExecuteStatus() == TaskExecuteStatus.Processing) {
            throw new BusinessException("该任务正在通知处理中", "Task.Execute.Processing");
        }
        message.setNextSendTime(new Date());
        if (message.getSendCount() > 0) {
            message.setSendCount(message.getSendCount() - 1);
        }
        message.setStatus(TaskStatus.Waitting);
        notifyMessageDao.updateForManage(message);
    }

    @Override
    public int updateProccessingStatus(NotifyMessage notifyMessage) {
        return notifyMessageDao.updateProccessingStatus(notifyMessage);
    }

    @Override
    public void updateForManage(NotifyMessage notifyMessage) {
        notifyMessageDao.updateForManage(notifyMessage);
    }

    @Override
    public void updateProccessingStatus(List<NotifyMessage> notifyMessages) {
        notifyMessageDao.updateProccessingStatus(notifyMessages);
    }

    @Override
    public List<NotifyMessage> listUnProcessed(Integer topNum) {
        return notifyMessageDao.listUnProcessed(topNum);
    }

    @Override
    public PageInfo<NotifyMessage> query(
            PageInfo<NotifyMessage> pageInfo, Map<String, Object> map, Map<String, Boolean> orderMap) {
        return notifyMessageDao.query(pageInfo, map, orderMap);
    }
}
