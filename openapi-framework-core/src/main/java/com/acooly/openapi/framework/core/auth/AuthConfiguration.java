package com.acooly.openapi.framework.core.auth;

import com.acooly.openapi.framework.core.auth.impl.DefaultApiAuthorization;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/** @author qiubo@yiji.com */
@Configuration
public class AuthConfiguration {
  @Bean
  @ConditionalOnMissingBean(ApiAuthorization.class)
  public ApiAuthorization apiAuthorization() {
    return new DefaultApiAuthorization();
  }
}
