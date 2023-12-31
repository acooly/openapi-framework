/*
 * www.yiji.com Inc.
 * Copyright (c) 2017 All Rights Reserved
 */

/*
 * 修订记录:
 * qiubo@yiji.com 2017-03-13 17:36 创建
 */
package com.acooly.openapi.framework.client;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;

import static com.acooly.openapi.framework.client.OpenApiClientProperties.PREFIX;


/**
 * OpenAPI client configarution
 */
@Configuration
@ConfigurationProperties(prefix = PREFIX)
@Data
@Slf4j
@Validated
public class OpenApiClientProperties {
    public static final String PREFIX = "acooly.openapi.client";
    private boolean enable = false;

    @NotBlank
    private String gateway = "http://localhost:8089/gateway.do";
    @NotBlank
    private String accessKey = "test";
    @NotBlank
    private String secretKey = "06f7aab08aa2431e6dae6a156fc9e0b4";
    private boolean showLog = true;
}
