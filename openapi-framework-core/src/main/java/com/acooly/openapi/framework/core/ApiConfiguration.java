package com.acooly.openapi.framework.core;

import com.acooly.openapi.framework.core.auth.ApiAuthorization;
import com.acooly.openapi.framework.core.auth.impl.DefaultApiAuthorization;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;

/** @author qiubo@yiji.com */
@Configuration
public class ApiConfiguration {
  @Bean
  @ConditionalOnMissingBean(ApiAuthorization.class)
  public ApiAuthorization apiAuthorization() {
    return new DefaultApiAuthorization();
  }

  @Configuration
  @ConditionalOnProperty({"dubbo.provider.enable"})
  @ImportResource("classpath:spring/openapi/openapi-facade-dubbo-provider.xml")
  public static class OpenApiRemoteServiceConfig{

  }
}
