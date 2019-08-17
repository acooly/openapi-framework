package com.acooly.openapi.framework.service;

import com.acooly.core.common.dao.support.StandardDatabaseScriptIniter;
import com.acooly.module.mybatis.EntityMybatisDao;
import com.acooly.openapi.framework.service.service.AuthInfoRealmManageService;
import com.acooly.openapi.framework.service.service.impl.DefaultAuthInfoRealmManageService;
import com.google.common.collect.Lists;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * @author qiuboboy@qq.com
 * @date 2017-11-23 14:10
 */
@Configuration
@ComponentScan(basePackages = {"com.acooly.openapi.framework.service"})
@MapperScan(basePackages = "com.acooly.openapi.framework.service.dao"
        , markerInterface = EntityMybatisDao.class)
public class ApiServiceAutoConfig {
    @Bean
    public StandardDatabaseScriptIniter openapiManageScriptIniter() {
        return new StandardDatabaseScriptIniter() {

            @Override
            public String getEvaluateTable() {
                return "api_tenant";
            }

            @Override
            public String getComponentName() {
                return "openapi";
            }

            @Override
            public List<String> getInitSqlFile() {
                return Lists.newArrayList("openapi", "openapi-initTest", "openapi-urls");
            }
        };
    }

    @Bean
    @ConditionalOnMissingBean(AuthInfoRealmManageService.class)
    public AuthInfoRealmManageService defaultAuthInfoRealmManageService() {
        return new DefaultAuthInfoRealmManageService();
    }

}
