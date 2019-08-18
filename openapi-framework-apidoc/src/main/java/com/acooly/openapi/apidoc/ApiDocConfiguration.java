/*
 * www.yiji.com Inc.
 * Copyright (c) 2017 All Rights Reserved
 */

/*
 * 修订记录:
 * qiubo@yiji.com 2017-03-13 16:33 创建
 */
package com.acooly.openapi.apidoc;

import com.acooly.core.common.dao.support.StandardDatabaseScriptIniter;
import com.google.common.collect.Lists;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.util.List;

import static com.acooly.openapi.apidoc.ApiDocProperties.PREFIX;

/**
 * @author zhangpu@acooly.cn
 */
@Configuration
@EnableConfigurationProperties({ApiDocProperties.class})
@ConditionalOnProperty(value = PREFIX + ".enable", matchIfMissing = true)
@ComponentScan(basePackages = "com.acooly.openapi.apidoc")
@EntityScan(basePackages = "com.acooly.openapi.apidoc.persist.entity")
public class ApiDocConfiguration {

    @Bean
    public StandardDatabaseScriptIniter ApiDocScriptIniter() {
        return new StandardDatabaseScriptIniter() {

            @Override
            public String getEvaluateTable() {
                return "api_doc_item";
            }

            @Override
            public String getComponentName() {
                return "apidoc";
            }

            @Override
            public List<String> getInitSqlFile() {
                return Lists.newArrayList("apidoc", "apidoc_urls");
            }
        };
    }

}
