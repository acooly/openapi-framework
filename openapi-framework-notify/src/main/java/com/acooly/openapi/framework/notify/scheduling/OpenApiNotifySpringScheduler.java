package com.acooly.openapi.framework.notify.scheduling;

import com.acooly.core.utils.ShutdownHooks;
import com.acooly.module.distributedlock.DistributedLockFactory;
import com.acooly.openapi.framework.notify.OpenApiNotifyProperties;
import com.acooly.openapi.framework.notify.handle.NotifyMessageSendService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.concurrent.BasicThreadFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
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
public class OpenApiNotifySpringScheduler implements InitializingBean {

    @Autowired
    private NotifyMessageSendService notifyMessageSendService;

    @Autowired(required = false)
    private DistributedLockFactory factory;

    @Autowired
    private OpenApiNotifyProperties openApiNotifyProperties;

    private ScheduledExecutorService retryExecutorService;

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

    private void startRetryScheduler() {
        if (retryExecutorService == null) {
            retryExecutorService = new ScheduledThreadPoolExecutor(1,
                    new BasicThreadFactory.Builder().namingPattern("NOTIFY-RETRY-TASK-%d").daemon(true).build());
            ShutdownHooks.addShutdownHook(() -> retryExecutorService.shutdown(), "NOTIFY-RETRY-TASK-HOOK");
            retryExecutorService.scheduleAtFixedRate(
                    () -> this.autoNotify(), 60, openApiNotifyProperties.getRetryPeriod(), TimeUnit.SECONDS);
        }
    }


    @Override
    public void afterPropertiesSet() throws Exception {
        try {
            startRetryScheduler();
        } catch (Exception e) {
            log.error("The retry asyncNotify  scheduling init failed", e);
        }
    }
}
