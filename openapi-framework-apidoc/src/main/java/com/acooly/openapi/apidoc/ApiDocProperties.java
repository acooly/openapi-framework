/*
 * www.yiji.com Inc.
 * Copyright (c) 2017 All Rights Reserved
 */

/*
 * 修订记录:
 * qiubo@yiji.com 2017-03-13 17:36 创建
 */
package com.acooly.openapi.apidoc;

import com.google.common.collect.Lists;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.annotation.Validated;

import java.util.List;

import static com.acooly.openapi.apidoc.ApiDocProperties.PREFIX;

/**
 * @author qiubo@yiji.com
 */
@Configuration
@ConfigurationProperties(prefix = PREFIX)
@Data
@Slf4j
@Validated
public class ApiDocProperties {
    public static final String PREFIX = "acooly.openapi.apidoc";
    private boolean enable;

    /**
     * 是否将所有服务加入都通用解决方案中，true：加入，false:不加（默认）
     */
    private boolean intoCommon = true;

    /**
     * 是否现实通用服务方案（所有API）
     */
    private boolean showCommon = true;

    /**
     * 版权信息
     */
    private String copyright = "Copyright@acooly";

    private String logo = "/portal/images/logo-acooly-white.png";


    private String gateway = "";
    @NotBlank
    private String testGateway = "http://localhost:8090/gateway.do";


    private List<String> outputTypes = Lists.newArrayList("database");

}
