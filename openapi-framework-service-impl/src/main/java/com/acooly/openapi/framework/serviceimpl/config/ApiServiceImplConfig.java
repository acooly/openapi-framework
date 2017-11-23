package com.acooly.openapi.framework.serviceimpl.config;

import com.acooly.core.common.dao.dialect.DatabaseType;
import com.acooly.core.common.dao.support.AbstractDatabaseScriptIniter;
import com.acooly.openapi.framework.service.AuthInfoRealmManageService;
import com.acooly.openapi.framework.serviceimpl.persistent.DefaultAuthInfoRealmManageService;
import com.google.common.collect.Lists;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.util.List;

/**
 * @author qiuboboy@qq.com
 * @date 2017-11-23 14:10
 */
@Configuration
public class ApiServiceImplConfig {
  @Bean
  public AbstractDatabaseScriptIniter openapiManageScriptIniter() {
    return new AbstractDatabaseScriptIniter() {
      @Override
      public String getEvaluateSql(DatabaseType databaseType) {
        return "SELECT count(*) FROM api_partner";
      }

      @Override
      public List<String> getInitSqlFile(DatabaseType databaseType) {
        if (databaseType == DatabaseType.mysql) {
          return Lists.newArrayList(
              "META-INF/database/mysql/openapi-manage.sql",
              "META-INF/database/mysql/openapi-initTest.sql",
              "META-INF/database/mysql/openapi-manage-urls.sql");
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

  @ConditionalOnProperty(value = "acooly.openapi.notify.enable", matchIfMissing = true)
  @EnableScheduling
  public static class ApiNotifyConfig {
    @Configuration
    @ConditionalOnProperty({"dubbo.provider.enable"})
    @ImportResource("classpath:spring/openapi/openapi-facade-dubbo-provider.xml")
    public static class OpenApiRemoteServiceConfig {}
  }
}
