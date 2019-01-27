package com.acooly.openapi.framework.service.config;

import com.acooly.core.common.dao.dialect.DatabaseType;
import com.acooly.core.common.dao.support.AbstractDatabaseScriptIniter;
import com.acooly.openapi.framework.service.service.AuthInfoRealmManageService;
import com.acooly.openapi.framework.service.service.impl.DefaultAuthInfoRealmManageService;
import com.google.common.collect.Lists;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * @author qiuboboy@qq.com
 * @date 2017-11-23 14:10
 */
@Configuration
public class ApiServiceAutoConfig {
    @Bean
    public AbstractDatabaseScriptIniter openapiManageScriptIniter() {
        return new AbstractDatabaseScriptIniter() {
            @Override
            public String getEvaluateSql(DatabaseType databaseType) {
                return "SELECT count(*) FROM api_tenant";
            }

            @Override
            public List<String> getInitSqlFile(DatabaseType databaseType) {
                if (databaseType == DatabaseType.mysql) {
                    return Lists.newArrayList(
                            "META-INF/database/mysql/openapi.sql",
                            "META-INF/database/mysql/openapi-initTest.sql",
                            "META-INF/database/mysql/openapi-urls.sql");
                } else {
                    throw new UnsupportedOperationException("还不支持oracle");
                }
            }
        };
    }

    @Bean
    @ConditionalOnMissingBean(AuthInfoRealmManageService.class)
    public AuthInfoRealmManageService defaultAuthInfoRealmManageService() {
        return new DefaultAuthInfoRealmManageService();
    }

}
