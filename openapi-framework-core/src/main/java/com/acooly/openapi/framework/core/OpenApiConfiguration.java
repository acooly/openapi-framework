package com.acooly.openapi.framework.core;

import com.acooly.core.utils.Collections3;
import com.acooly.openapi.framework.common.cache.OpenApiCacheManager;
import com.acooly.openapi.framework.core.auth.ApiAuthorization;
import com.acooly.openapi.framework.core.auth.impl.DefaultApiAuthorization;
import com.acooly.openapi.framework.core.auth.realm.AuthInfoRealm;
import com.acooly.openapi.framework.core.auth.realm.impl.DefaultAuthInfoRealm;
import com.acooly.openapi.framework.core.common.cache.impl.MultiLevelOpenApiCacheManager;
import com.acooly.openapi.framework.core.common.cache.impl.NOOPOpenApiCacheManager;
import com.acooly.openapi.framework.core.service.buildin.login.DefaultAppApiLoginService;
import com.acooly.openapi.framework.core.servlet.OpenAPIDispatchServlet;
import com.acooly.openapi.framework.service.service.AppApiLoginService;
import com.acooly.openapi.framework.service.service.OrderInfoService;
import com.acooly.openapi.framework.service.service.impl.NothingToDoOrderInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.util.List;

import static com.acooly.openapi.framework.core.OpenAPIProperties.PREFIX;

/**
 * @author qiubo@yiji.com
 */
@Configuration
@EnableConfigurationProperties({OpenAPIProperties.class})
@ConditionalOnProperty(value = PREFIX + ".enable", matchIfMissing = true)
@ComponentScan(basePackages = "com.acooly.openapi.framework")
@AutoConfigureAfter
public class OpenApiConfiguration {
    @Autowired
    private OpenAPIProperties properties;

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
    public OpenApiCacheManager openApicacheManager() {
        if (properties.getAuthInfoCache().isEnable()) {
            return new MultiLevelOpenApiCacheManager();
        } else {
            return new NOOPOpenApiCacheManager();
        }
    }

    /**
     * 网关入口
     *
     * @return
     */
    @Bean
    public ServletRegistrationBean openAPIServlet() {
        ServletRegistrationBean bean = new ServletRegistrationBean();
        List<String> urlMappings = properties.getGateways();
        if (Collections3.isEmpty(urlMappings)) {
            urlMappings.add("/gateway.do");
        }
        bean.setUrlMappings(urlMappings);
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
    }


    @Configuration
    @ConditionalOnProperty(value = "acooly.openapi.login.enable", matchIfMissing = true)
    public static class ApiLoginConfiguration {
        @ConditionalOnMissingBean(AppApiLoginService.class)
        @Bean
        public AppApiLoginService defaultAppApiLoginService() {
            return new DefaultAppApiLoginService();
        }
    }
}
