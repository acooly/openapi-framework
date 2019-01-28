/*
 * www.yiji.com Inc.
 * Copyright (c) 2017 All Rights Reserved
 */

/*
 * 修订记录:
 * qiubo@yiji.com 2017-02-14 16:11 创建
 */
package com.acooly.openapi.framework.notify;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;

import static com.acooly.openapi.framework.core.OpenAPIProperties.PREFIX;

/**
 * OpenAPI Notify 模块配置
 *
 * @author zhangpu
 * @date 2019-01-27
 */
@ConfigurationProperties(prefix = PREFIX)
@Data
@Slf4j
public class OpenApiNotifyProperties {
    public static final String PREFIX = "acooly.openapi.notify";
    /**
     * 异步通知功能开关
     */
    private boolean enable = true;

    /**
     * 是否启动OpenApi的dubbo-facade服务
     */
    private boolean enableFacade = false;

    /**
     * 每次从数据库中拉取待通知的最大数量
     */
    private int retryFetchSize = 5;

    /**
     * 异步通知http连接超时时间（毫秒），默认10秒
     */
    private int connTimeout = 10 * 1000;
    /**
     * 异步通知http连接读取超时时间（毫秒），默认10秒
     */
    private int readTimeout = 10 * 1000;

    /**
     * 异步通知线程池配置
     */
    private ThreadPool threadPool = new ThreadPool();

    @Data
    public static class ThreadPool {

        private int corePoolSize = 10;
        private int maxPoolSize = 50;
        private int queueCapacity = 100;
        private String namePrefix = "OPENAPI-NOTIFY-TASK";
    }


}
