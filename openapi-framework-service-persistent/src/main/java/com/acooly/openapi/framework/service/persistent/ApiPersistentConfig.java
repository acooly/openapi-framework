package com.acooly.openapi.framework.service.persistent;

import com.acooly.openapi.framework.service.AuthInfoRealmManageService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author qiuboboy@qq.com
 * @date 2017-11-23 14:10
 */
@Configuration
public class ApiPersistentConfig {
  @Bean
  @ConditionalOnMissingBean(AuthInfoRealmManageService.class)
  public AuthInfoRealmManageService defaultAuthInfoRealmManageService() {
    return new DefaultAuthInfoRealmManageService();
  }
}
