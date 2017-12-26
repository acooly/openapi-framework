/*
 * www.yiji.com Inc.
 * Copyright (c) 2017 All Rights Reserved
 */

/*
 * 修订记录:
 * qiubo@yiji.com 2017-03-13 16:33 创建
 */
package com.acooly.openapi.apidoc;

import com.acooly.core.common.dao.dialect.DatabaseType;
import com.acooly.core.common.dao.support.AbstractDatabaseScriptIniter;
import com.google.common.collect.Lists;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.ServletContextInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.util.List;

import static com.acooly.openapi.apidoc.ApiDocProperties.PREFIX;

/**
 * @author qiubo@yiji.com
 */
@Configuration
@EnableConfigurationProperties({ApiDocProperties.class})
@ConditionalOnProperty(value = PREFIX + ".enable", matchIfMissing = true)
@ComponentScan(basePackages = "com.acooly.openapi.apidoc")
public class ApiDocConfiguration {

    @Bean
    public AbstractDatabaseScriptIniter ApiDocScriptIniter() {
        return new AbstractDatabaseScriptIniter() {
            @Override
            public String getEvaluateSql(DatabaseType databaseType) {
                return "SELECT count(*) FROM api_doc_item";
            }

            @Override
            public List<String> getInitSqlFile(DatabaseType databaseType) {
                return Lists.newArrayList("META-INF/database/mysql/apidoc.sql",
                        "META-INF/database/mysql/apidoc_urls.sql");
            }
        };
    }

    @Bean
    public ServletContextInitializer ApiDocServletContextInitializer(ApiDocProperties apiDocProperties) {
        return servletContext -> {
            servletContext.setInitParameter("openapi.apidoc.copyright", apiDocProperties.getCopyright());
        };
    }
}
