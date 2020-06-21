/**
 * openapi-framework
 * <p>
 * Copyright 2014 Acooly.cn, Inc. All rights reserved.
 *
 * @author zhangpu
 * @date 2019-09-15 10:02
 */
package com.acooly.openapi.framework.notify.handle.impl;

import com.acooly.core.utils.Strings;
import com.acooly.openapi.framework.common.ApiConstants;
import com.acooly.openapi.framework.common.enums.TaskExecuteStatus;
import com.acooly.openapi.framework.common.enums.TaskStatus;
import com.acooly.openapi.framework.notify.OpenApiNotifyProperties;
import com.acooly.openapi.framework.notify.handle.NotifyMessageSendHandler;
import com.acooly.openapi.framework.service.domain.NotifyMessage;
import com.acooly.openapi.framework.service.service.NotifyMessageService;
import com.github.kevinsawicki.http.HttpRequest;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.task.TaskExecutor;

import javax.annotation.Resource;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * 抽象通知发送实现
 *
 * @author zhangpu
 * @date 2019-09-15 10:02
 */
@Slf4j
public abstract class AbstractNotifyMessageSendHandler implements NotifyMessageSendHandler {
    /**
     * 通知间隔时间,第一次隔两分钟，然后隔10分钟... 一共通知11次,失败后通知10次,55小时42分钟
     */
    protected static int[] notifyTime = {
            2, 10, 30, 60, 2 * 60, 6 * 60, 6 * 60, 10 * 60, 15 * 60, 15 * 60
    };
    /**
     * 失败后通知总次数
     */
    protected static int notifyCount = notifyTime.length;

    @Resource
    protected NotifyMessageService notifyMessageService;

    @Resource
    protected TaskExecutor openApiNotifyExecutor;

    @Autowired
    protected OpenApiNotifyProperties openApiNotifyProperties;


    @Override
    public void send(final NotifyMessage notifyMessage) {
        openApiNotifyExecutor.execute(() -> doSend(notifyMessage));
    }

    protected void doSend(final NotifyMessage notifyMessage) {
        MDC.put("gid", notifyMessage.getGid());
        Date now = new Date();
        // 如果非第一次通知，下次通知时间还没有到，则忽略
        if (notifyMessage.getSendCount() != 0
                && notifyMessage.getNextSendTime() != null
                && notifyMessage.getNextSendTime().after(now)) {
            return;
        }
        // 处理前锁定
        int rows = notifyMessageService.updateProccessingStatus(notifyMessage);
        if (rows <= 0) {
            // 被其他节点或线程处理了,放弃发送
            return;
        }
        notifyMessage.setSendCount(notifyMessage.getSendCount() + 1);
        log.info("第{}次通知", notifyMessage.getSendCount());

        String result = null;
        String respInfo;
        try {
            HttpRequest httpRequest = doRequest(notifyMessage);
            result = httpRequest.body();
            respInfo = httpRequest.code() + ":" + result;
        } catch (Exception e) {
            respInfo = e.getMessage();
            log.error("第{}次通知 [失败]，原因:{}", notifyMessage.getSendCount(), e.getMessage());
        }

        TaskStatus taskStatus = null;
        if (isSuccess(result)) {
            log.info("第{}次通知 [成功]", notifyMessage.getSendCount());
            taskStatus = TaskStatus.Success;
        } else {
            int sendCount = notifyMessage.getSendCount();
            if (sendCount >= notifyCount) {
                taskStatus = TaskStatus.Finish;
                notifyMessage.setNextSendTime(null);
                log.info("第{}次通知 [失败]，通知失败次数达到阀值，结束通知任务", sendCount);
            } else {
                taskStatus = TaskStatus.Waitting;
                notifyMessage.setNextSendTime(getNextNotifyTime(sendCount - 1));
                log.info("第{}次通知 [失败]，通知失败次数未达到阀值，等待下次通知", sendCount);
            }
        }
        notifyMessage.setStatus(taskStatus);
        notifyMessage.setExecuteStatus(TaskExecuteStatus.Unprocessed);
        if (Strings.length(respInfo) > 128) {
            respInfo = Strings.substring(respInfo, 0, 128);
        }
        notifyMessage.setRespInfo(respInfo);
        log.debug("asyncNotify save: notifyMessage:{}", notifyMessage);
        notifyMessageService.updateStatus(notifyMessage);
        MDC.clear();
    }

    /**
     * 组装发送内容
     * 如果需要，可以在此进行再次签名处理
     *
     * @param notifyMessage
     * @return
     */
    protected abstract HttpRequest doRequest(NotifyMessage notifyMessage);


    protected boolean isSuccess(String result) {
        if (result == null) {
            return false;
        }
        result = result.toLowerCase();
        if (ApiConstants.NOTIFY_SUCCESS_CONTENT.equalsIgnoreCase(result)) {
            return true;
        }
        result = result.replaceAll("\\s*", "");
        if (ApiConstants.NOTIFY_SUCCESS_CONTENT.equalsIgnoreCase(result)) {
            return true;
        }
        return result.length() < 50 && result.contains(ApiConstants.NOTIFY_SUCCESS_CONTENT);
    }

    private static Date getNextNotifyTime(int num) {
        GregorianCalendar calendar = new GregorianCalendar();
        calendar.setTime(new Date());
        calendar.add(Calendar.MINUTE, notifyTime[num]);
        return calendar.getTime();
    }
}
