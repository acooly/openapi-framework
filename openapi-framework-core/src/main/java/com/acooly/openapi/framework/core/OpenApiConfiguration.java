package com.acooly.openapi.framework.core;

import com.acooly.core.common.dao.dialect.DatabaseType;
import com.acooly.core.common.dao.support.AbstractDatabaseScriptIniter;
import com.acooly.module.jpa.ex.AbstractEntityJpaDao;
import com.acooly.openapi.framework.core.auth.ApiAuthorization;
import com.acooly.openapi.framework.core.auth.impl.DefaultApiAuthorization;
import com.acooly.openapi.framework.core.auth.realm.AuthInfoRealm;
import com.acooly.openapi.framework.core.auth.realm.impl.DefaultAuthInfoRealm;
import com.acooly.openapi.framework.core.notify.ApiNotifySender;
import com.acooly.openapi.framework.core.notify.api.OpenApiRemoteServiceImpl;
import com.acooly.openapi.framework.core.service.support.NothingToDoOrderInfoService;
import com.acooly.openapi.framework.core.service.support.UnsupportApiNotifySender;
import com.acooly.openapi.framework.core.service.support.login.DefaultAppApiLoginService;
import com.acooly.openapi.framework.core.servlet.OpenAPIDispatchServlet;
import com.acooly.openapi.framework.facade.api.OpenApiRemoteService;
import com.acooly.openapi.framework.service.AppApiLoginService;
import com.acooly.openapi.framework.service.OrderInfoService;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.util.List;

import static com.acooly.openapi.framework.core.OpenAPIProperties.PREFIX;

/** @author qiubo@yiji.com */
@Configuration
@EnableConfigurationProperties({OpenAPIProperties.class})
@ConditionalOnProperty(value = PREFIX + ".enable", matchIfMissing = true)
@ComponentScan(basePackages = "com.acooly.openapi.framework")
@EnableJpaRepositories(
  repositoryBaseClass = AbstractEntityJpaDao.class,
  basePackages = "com.acooly.openapi.framework"
)
@EntityScan(basePackages = "com.acooly.openapi.framework.domain")
public class OpenApiConfiguration {
  @Autowired private OpenAPIProperties properties;

  @Bean
  @ConditionalOnMissingBean(ApiAuthorization.class)
  public ApiAuthorization apiAuthorization() {
    return new DefaultApiAuthorization();
  }

  @Bean
  @ConditionalOnMissingBean(AuthInfoRealm.class)
  public DefaultAuthInfoRealm authInfoRealm() {
    return new DefaultAuthInfoRealm();
  }

  @Bean
  public ServletRegistrationBean openAPIServlet() {
    ServletRegistrationBean bean = new ServletRegistrationBean();
    bean.setUrlMappings(Lists.newArrayList("/gateway.do"));
    OpenAPIDispatchServlet openAPIDispatchServlet = new OpenAPIDispatchServlet();
    bean.setServlet(openAPIDispatchServlet);
    bean.setLoadOnStartup(1);
    return bean;
  }

  @Configuration
  public static class SaveOrderConfig {
    @Bean
    @ConditionalOnMissingClass
    @ConditionalOnProperty(name = "acooly.openapi.saveOrder", havingValue = "false")
    public OrderInfoService nothingToDoOrderInfoService() {
      return new NothingToDoOrderInfoService();
    }

    @ConditionalOnProperty(name = "acooly.openapi.saveOrder", matchIfMissing = true)
    @Bean
    public AbstractDatabaseScriptIniter openapiOrderInfoScriptIniter() {
      return new AbstractDatabaseScriptIniter() {
        @Override
        public String getEvaluateSql(DatabaseType databaseType) {
          return "SELECT count(*) FROM api_order_info";
        }

        @Override
        public List<String> getInitSqlFile(DatabaseType databaseType) {
          if (databaseType == DatabaseType.mysql) {
            return Lists.newArrayList("META-INF/database/mysql/openapi-order.sql");
          } else {
            return Lists.newArrayList("META-INF/database/oracle/openapi-order.sql");
          }
        }
      };
    }
  }

  @Configuration
  public static class NotifyConfig {
    @Configuration
    @ConditionalOnProperty({"dubbo.provider.enable", "acooly.openapi.notify.enable"})
    @ImportResource("classpath:spring/openapi/openapi-facade-dubbo-provider.xml")
    @EnableScheduling
    public static class Enable {
      @Bean
      public OpenApiRemoteService openApiRemoteService() {
        return new OpenApiRemoteServiceImpl();
      }

      @Bean
      public AbstractDatabaseScriptIniter openapiNotifyScriptIniter() {
        return new AbstractDatabaseScriptIniter() {
          @Override
          public String getEvaluateSql(DatabaseType databaseType) {
            return "SELECT count(*) FROM api_notify_message";
          }

          @Override
          public List<String> getInitSqlFile(DatabaseType databaseType) {
            if (databaseType == DatabaseType.mysql) {
              return Lists.newArrayList("META-INF/database/mysql/openapi-notify.sql");
            } else {
              return Lists.newArrayList("META-INF/database/oracle/openapi-notify.sql");
            }
          }
        };
      }
    }

    @Configuration
    @ConditionalOnProperty(name = "acooly.openapi.notify.enable", havingValue = "false")
    public static class Disable {
      @Bean
      @ConditionalOnMissingClass
      public ApiNotifySender unsupportApiNotifySender() {
        return new UnsupportApiNotifySender();
      }
    }
  }

  @Configuration
  @ConditionalOnProperty("acooly.openapi.login.enable")
  public static class ApiLoginConfiguration {
    @ConditionalOnMissingBean(AppApiLoginService.class)
    @Bean
    public AppApiLoginService defaultAppApiLoginService() {
      return new DefaultAppApiLoginService();
    }
  }
}
