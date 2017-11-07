package com.acooly.openapi.framework;

import com.acooly.core.common.BootApp;
import com.acooly.core.common.boot.Apps;
import com.acooly.core.common.dao.dialect.DatabaseType;
import com.acooly.core.common.dao.support.AbstractDatabaseScriptIniter;
import com.acooly.openapi.framework.core.servlet.OpenAPIDispatchServlet;
import com.google.common.collect.Lists;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;

import java.util.List;

/** @author qiubo */
@BootApp(sysName = "openapi", httpPort = 8089)
public class Main {
  public static void main(String[] args) {
    Apps.setProfileIfNotExists("sdev");
    new SpringApplication(Main.class).run(args);
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

  @Bean
  public AbstractDatabaseScriptIniter openapiCoreScriptIniter() {
    return new AbstractDatabaseScriptIniter() {
      @Override
      public String getEvaluateSql(DatabaseType databaseType) {
        return "SELECT count(*) FROM api_order_info";
      }

      @Override
      public List<String> getInitSqlFile(DatabaseType databaseType) {
        if (databaseType == DatabaseType.mysql) {
          return Lists.newArrayList("META-INF/database/mysql/openapi-core.sql");
        } else {
          return Lists.newArrayList("META-INF/database/oracle/openapi-core.sql");
        }
      }
    };
  }

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
}
