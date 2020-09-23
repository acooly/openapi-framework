package com.acooly.openapi.framework.notify;

import com.acooly.openapi.framework.facade.api.OpenApiRemoteService;
import com.alibaba.dubbo.config.ApplicationConfig;
import com.alibaba.dubbo.config.ProtocolConfig;
import com.alibaba.dubbo.config.RegistryConfig;
import com.alibaba.dubbo.config.ServiceConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

import static com.acooly.openapi.framework.notify.OpenApiNotifyProperties.PREFIX;


/**
 * OpenAPI通知框架配置
 *
 * @author zhangpu
 * @date 2019-1-27
 */
@Configuration
@EnableConfigurationProperties({OpenApiNotifyProperties.class})
@ConditionalOnProperty(value = PREFIX + ".enable", matchIfMissing = true)
@EnableScheduling
@EnableAsync
public class OpenApiNotifyAutoConfig {

    @Autowired
    private OpenApiNotifyProperties openApiNotifyProperties;


    @Bean
    @ConditionalOnProperty(value = {PREFIX + ".enableFacade"}, matchIfMissing = false, havingValue = "true")
    @ConditionalOnBean(ProtocolConfig.class)
    @DependsOn({"applicationConfig", "registryConfig", "protocolConfig"})
    public ServiceConfig<OpenApiRemoteService> feedbackFacadeConfig(ApplicationConfig applicationConfig,
                                                                    RegistryConfig registryConfig,
                                                                    ProtocolConfig protocolConfig,
                                                                    OpenApiRemoteService openApiRemoteService) {
        ServiceConfig<OpenApiRemoteService> service = new ServiceConfig<>();
        service.setApplication(applicationConfig);
        service.setRegistry(registryConfig);
        service.setProtocol(protocolConfig);
        service.setInterface(OpenApiRemoteService.class);
        service.setRef(openApiRemoteService);
        service.setVersion("1.0");
        service.export();
        return service;
    }


    @Bean
    public Executor openApiNotifyExecutor() {
        OpenApiNotifyProperties.ThreadPool threadPool = openApiNotifyProperties.getThreadPool();
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(threadPool.getCorePoolSize());
        executor.setMaxPoolSize(threadPool.getMaxPoolSize());
        executor.setQueueCapacity(threadPool.getQueueCapacity());
        executor.setThreadNamePrefix(threadPool.getNamePrefix());
        executor.initialize();
        return executor;
    }


}
