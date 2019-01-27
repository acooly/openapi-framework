package com.acooly.openapi.framework.notify.scheduling;

import com.acooly.module.distributedlock.DistributedLockFactory;
import com.acooly.openapi.framework.notify.handle.NotifyMessageSendService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.concurrent.locks.Lock;

/**
 * 定时通知重试启动器
 * Spring Scheduling实现
 *
 * @author zhangpu
 * @date 2019-01-27 21:52
 */
@Slf4j
@Component
public class OpenApiNotifySpringScheduling {

    @Autowired
    private NotifyMessageSendService notifyMessageSendService;

    @Autowired(required = false)
    private DistributedLockFactory factory;

    /**
     * 每两秒启动一次任务
     */
    @Scheduled(fixedDelay = 5 * 1000)
    public void autoNotify() {

        if (factory == null) {
            log.debug("启动 StandAlone-Notify-Retry...");
            doNotify();
            return;
        }

        Lock lock = factory.newLock("OpenApi-Notify-Retry");
        if (lock.tryLock()) {
            try {
                log.debug("启动 Distributed-Notify-Retry...");
                doNotify();
            } finally {
                lock.unlock();
            }
        }
    }

    protected void doNotify() {
        notifyMessageSendService.autoNotifyMessage();
    }


}
