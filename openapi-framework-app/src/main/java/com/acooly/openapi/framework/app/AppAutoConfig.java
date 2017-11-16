/*
 * www.yiji.com Inc.
 * Copyright (c) 2017 All Rights Reserved
 */

/*
 * 修订记录:
 * kuli@yiji.com 2017-02-14 17:04 创建
 */
package com.acooly.openapi.framework.app;

import com.acooly.core.common.dao.support.StandardDatabaseScriptIniter;
import com.acooly.openapi.framework.app.openapi.support.AppApiLoginService;
import com.acooly.openapi.framework.app.openapi.support.login.AnonymousAppApiLoginService;
import com.acooly.openapi.framework.core.notify.impl.DefaultApiNotifySender;
import com.google.common.collect.Lists;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.util.List;

import static com.acooly.openapi.framework.app.AppProperties.PREFIX;

/** @author kuli@yiji.com */
@Configuration
@EnableConfigurationProperties({AppProperties.class})
@ConditionalOnProperty(value = PREFIX + ".enable", matchIfMissing = true)
@ComponentScan(basePackages = "com.acooly.openapi.framework.app")
public class AppAutoConfig {
  @ConditionalOnMissingBean(AppApiLoginService.class)
  @Bean
  public AppApiLoginService appApiLoginService() {
    return new AnonymousAppApiLoginService();
  }

  @Bean
  public DefaultApiNotifySender defaultApiNotifySender(AppProperties appProperties) {
    DefaultApiNotifySender sender = new DefaultApiNotifySender();
    sender.setConnectionTimeout(appProperties.getNotifysender().getConnectionTimeout());
    sender.setSocketTimeout(appProperties.getNotifysender().getSocketTimeout());
    return sender;
  }
  @Bean
  public StandardDatabaseScriptIniter appScriptIniter() {
    return new StandardDatabaseScriptIniter() {
      @Override
      public String getEvaluateTable() {
        return "app_banner";
      }

      @Override
      public String getComponentName() {
        return "app";
      }

      @Override
      public List<String> getInitSqlFile() {
        return Lists.newArrayList("app", "app_urls");
      }
    };
  }
}
