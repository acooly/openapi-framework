package com.acooly.openapi.framework.core;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.Appender;
import com.acooly.core.common.boot.Apps;
import com.acooly.core.common.boot.log.LogbackConfigurator;
import com.acooly.core.common.boot.log.initializer.AbstractLogInitializer;
import com.acooly.openapi.framework.common.ApiConstants;

import static com.acooly.core.common.boot.Apps.buildProperties;

/** @author qiubo@yiji.com */
public class OpenAPIPerfLogInitializer extends AbstractLogInitializer {
  @Override
  public void init(LogbackConfigurator configurator) {
    OpenAPIProperties openAPIProperties = buildProperties(OpenAPIProperties.class);
    if (openAPIProperties.getEnablePerfLog()) {
      String fileName = "api-pref.log";
      configurator.log("设置api性能日志，日志文件为:%s", fileName);
      Appender<ILoggingEvent> appender =
          configurator.fileAppender("api-perf", "%msg%n", fileName, 5);
      configurator.logger(ApiConstants.PERFORMANCE_LOGGER, Level.INFO, false, appender);
    }else {
        configurator.logger(ApiConstants.PERFORMANCE_LOGGER,Level.OFF);
    }

    if (openAPIProperties.getQueryLogSeparationEnable()) {
      String apiQuery = "api-query-30de.log";
      configurator.log("设置info级别的api-query日志，日志文件为:%s", apiQuery);
      Appender<ILoggingEvent> apiQueryAppender =
          configurator.asyncFileAppender("api-query", configurator.getPattern(), apiQuery);
      //开发者模式时，追加到主日志
      boolean additive = Apps.isDevMode();
      configurator.logger("API-QUERY", Level.INFO, additive, apiQueryAppender);
    }
  }
}
