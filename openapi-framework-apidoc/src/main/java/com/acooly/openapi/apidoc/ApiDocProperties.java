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
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;
import java.util.List;

import static com.acooly.openapi.apidoc.ApiDocProperties.PREFIX;

/**
 * @author qiubo@yiji.com
 */

@Slf4j
@Configuration
@ConfigurationProperties(prefix = PREFIX)
@Data
@Validated
public class ApiDocProperties {
    public static final String PREFIX = "acooly.openapi.apidoc";

    public static final String DEF_SCHEME_NO = "SYSTEM";
    public static final String DEF_SCHEME_TITLE = "所有服务";
    public static final String DEF_SCHEME_AUTHOR = "acooly";

    private boolean enable;

    /**
     * 是否将所有服务加入都通用解决方案中，true：加入，false:不加（默认）
     */
    private boolean defaultSchemeEnable = true;

    /**
     * 是否现实通用服务方案（所有API）
     */
    private boolean defaultSchemeShow = true;

    /**
     * 指定输出类型，多选：database,console,pdf,html等，默认database
     */
    private List<String> outputTypes = Lists.newArrayList("database");


    private Portal portal = new Portal();


    private String gateway = "";
    @NotBlank
    private String testGateway = "http://localhost:8090/gateway.do";


    @Data
    public static class Portal {

        private String title = "Acooly-OpenApi 开放平台";

        /**
         * 版权信息
         */
        private String copyright = "@ Copyright Acooly 2019";

        /**
         * LOGO
         */
        private String logo = "/portal/images/logo/logo-acooly-white.png";
    }


}
