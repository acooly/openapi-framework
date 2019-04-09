package com.acooly.openapi.mock;

import com.acooly.core.common.dao.dialect.DatabaseType;
import com.acooly.core.common.dao.support.AbstractDatabaseScriptIniter;
import com.google.common.collect.Lists;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * @author qiuboboy@qq.com
 * @date 2018-08-14 17:36
 */
@Configuration
public class ApiMockConfig {
    @Bean
    public AbstractDatabaseScriptIniter apiMockScriptIniter() {
        return new AbstractDatabaseScriptIniter() {
            @Override
            public String getEvaluateSql(DatabaseType databaseType) {
                return "SELECT count(*) FROM api_mock";
            }

            @Override
            public List<String> getInitSqlFile(DatabaseType databaseType) {
                return Lists.newArrayList("META-INF/database/mysql/apiMock.sql",
                        "META-INF/database/mysql/apiMock_urls.sql");
            }
        };
    }
}
