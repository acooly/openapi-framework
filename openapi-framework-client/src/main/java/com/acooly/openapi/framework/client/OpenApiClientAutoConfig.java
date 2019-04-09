/*
 * www.yiji.com Inc.
 * Copyright (c) 2017 All Rights Reserved
 */

/*
 * 修订记录:
 * qiubo@yiji.com 2017-03-13 16:33 创建
 */
package com.acooly.openapi.framework.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import static com.acooly.openapi.framework.client.OpenApiClientProperties.PREFIX;


/**
 * @author zhangpu
 */
@Configuration
@EnableConfigurationProperties({OpenApiClientProperties.class})
@ConditionalOnProperty(value = PREFIX + ".enable", matchIfMissing = true)
@ComponentScan(basePackages = "com.acooly.openapi.framework.client")
public class OpenApiClientAutoConfig {

    @Autowired
    private OpenApiClientProperties openApiClientProperties;

    @Bean
    @ConditionalOnMissingBean
    public OpenApiClient openApiClient() {
        OpenApiClient openApiClient = new OpenApiClient(openApiClientProperties.getGateway(),
                openApiClientProperties.getAccessKey(),
                openApiClientProperties.getSecretKey(),
                openApiClientProperties.isShowLog());
        return openApiClient;
    }
}
