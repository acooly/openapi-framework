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

import javax.annotation.PostConstruct;

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
    public static final String ALWAYS = "always";
    public static final String NONE = "none";
    private boolean enable;
    /**
     * 文档扫描包路径,多个包用逗号隔开
     */
    private String scanPackagePartern = "";
    /**
     * 扫描标识 为空默认按天扫描，值为always标识总是扫描，其它标识只扫描成功一次
     */
    private String genIndex = NONE;

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

    private String logo = "/portal/images/logo.png";


    private String gateway = "";
    @NotBlank
    private String testGateway = "http://localhost:8090/gateway.do";


    private List<String> outputTypes = Lists.newArrayList("database");

    @PostConstruct
    public void init() {
        scanPackagePartern += ",com.acooly.openapi,com.acooly.module.appopenapi,com.acooly.module.**.openapi";
    }

}
