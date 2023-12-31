/*
 * acooly.cn Inc.
 * Copyright (c) 2014 All Rights Reserved
 */

/*
 * 修订记录:
 * qiubo@qq.com 2017-07-25 15:00 增加openapi服务路由
 *
 */
package com.acooly.openapi.framework.core.service.factory;

import com.acooly.core.common.boot.Env;
import com.acooly.core.utils.Assert;
import com.acooly.core.utils.Reflections;
import com.acooly.openapi.framework.common.ApiConstants;
import com.acooly.openapi.framework.common.annotation.ApiDocNote;
import com.acooly.openapi.framework.common.annotation.ApiDocType;
import com.acooly.openapi.framework.common.annotation.OpenApiService;
import com.acooly.openapi.framework.common.executor.ApiService;
import com.acooly.openapi.framework.common.message.ApiAsyncRequest;
import com.acooly.openapi.framework.common.message.ApiNotify;
import com.acooly.openapi.framework.common.message.ApiRequest;
import com.acooly.openapi.framework.common.message.ApiResponse;
import com.acooly.openapi.framework.common.utils.json.ObjectAccessor;
import com.acooly.openapi.framework.core.OpenAPIProperties;
import com.acooly.openapi.framework.core.service.route.ServiceRouter;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

import static com.acooly.openapi.framework.common.enums.ResponseType.ASNY;
import static com.acooly.openapi.framework.common.enums.ResponseType.SYN;

/**
 * 服务工厂
 *
 * <p>根据服务命名约定查找spring容器内的服务
 *
 * @author zhangpu
 * @author Bohr.Qiu <qiubo@qq.com>
 */
@Slf4j
@Component
public class ApiServiceFactoryImpl implements ApiServiceFactory, ApplicationContextAware, InitializingBean {

    private ApplicationContext applicationContext;

    private Multimap<String, ApiService> servicesMap = null;

    @Resource(name = "serviceRouter")
    private ServiceRouter serviceRouter;

    @Autowired
    private OpenAPIProperties openAPIProperties;

    /**
     * @param serviceName
     * @param version
     * @return
     */
    @Override
    public ApiService getApiService(String serviceName, String version) {
        Collection<ApiService> apiServices = servicesMap.get(serviceName);
        return serviceRouter.route(serviceName, version, apiServices);
    }


    @Override
    public void afterPropertiesSet() throws Exception {
        servicesMap = HashMultimap.create();
        Map<String, ApiService> apiServiceBeansMap = applicationContext.getBeansOfType(ApiService.class);
        if (apiServiceBeansMap.isEmpty()) {
            log.warn("OpenApi没有对外提供服务");
            return;
        }
        for (ApiService apiService : apiServiceBeansMap.values()) {
            registerService(apiService, servicesMap);
        }
    }

    private void registerService(ApiService curApiService, Multimap<String, ApiService> servicesMap) {
        OpenApiService openApiService = getOpenApiServiceAnnotation(curApiService);
        if (openApiService == null) {
            throw new RuntimeException("OpenApi服务" + curApiService.getClass() + "必须要标记com.acooly.openapi.framework.core.meta.OpenApiService注解");
        }
        if (!openAPIProperties.getLogin().isEnable()) {
            if (openApiService.name().equals(ApiConstants.LOGIN_SERVICE_NAME)) {
                return;
            }
        }
        if (!checkApiService(curApiService)) {
            return;
        }
        if (servicesMap.containsKey(openApiService.name())) {
            Iterator<ApiService> iterator = servicesMap.get(openApiService.name()).iterator();
            while (iterator.hasNext()) {
                ApiService apiService = iterator.next();
                if (getOpenApiServiceAnnotation(apiService).version().equals(openApiService.version())) {
                    throw new RuntimeException("服务冲突:" + curApiService.getClass() + "和" + apiService.getClass());
                }
            }
        }
        // 设置mock
        curApiService.setMock(openApiService.mock());
        servicesMap.put(openApiService.name(), curApiService);

        log.info(
                "加载OpenApi服务[{}]{} {}:{}  {} {}",
                openApiService.desc(),
                openApiService.mock() ? " MOCK" : "",
                openApiService.name(),
                openApiService.version(),
                curApiService.getClass().getName(),
                openApiService.responseType().name());

        // 启动时检查是否有属性名重复的情况.并加载缓存
        ObjectAccessor.of(curApiService.getRequestBean());
        ObjectAccessor.of(curApiService.getResponseBean());
    }

    private boolean checkApiService(ApiService curApiService) {
        Class requestClazz = Reflections.getSuperClassGenricType(curApiService.getClass(), 0);
        Class responseClazz = Reflections.getSuperClassGenricType(curApiService.getClass(), 1);
        Assert.isAssignable(ApiRequest.class, requestClazz);
        Assert.isAssignable(ApiResponse.class, responseClazz);
        OpenApiService apiServiceAnnotation = getOpenApiServiceAnnotation(curApiService);
        if (apiServiceAnnotation.responseType() == ASNY) {
            ApiNotify apiNotifyBean = curApiService.getApiNotifyBean();
            Assert.notNull(apiNotifyBean);
        }
        if (apiServiceAnnotation.responseType() != SYN) {
            Assert.isAssignable(ApiAsyncRequest.class, requestClazz,
                    "异步服务" + curApiService + "请求对象必须为ApiAsyncRequest及其子类");
        }
        // 目前只警告
        if (!Env.isOnline()) {
            ApiDocType apiDocType = AnnotationUtils.findAnnotation(curApiService.getClass(), ApiDocType.class);
            if (apiDocType == null) {
                log.warn("[警告] OpenApi服务[{}] {}:{} 未标记@ApiDocType，请告诉Apidoc应该放到那个菜单",
                        apiServiceAnnotation.desc(), apiServiceAnnotation.name(), apiServiceAnnotation.version());
            }
            ApiDocNote apiDocNote = AnnotationUtils.findAnnotation(curApiService.getClass(), ApiDocNote.class);
            if (apiDocNote == null) {
                log.warn("[警告] OpenApi服务[{}] {}:{} 未标记@ApiDocNote，请告诉客户端这个接口做什么的，什么场景用，是否有特别注意的？",
                        apiServiceAnnotation.desc(), apiServiceAnnotation.name(), apiServiceAnnotation.version());
            }
        }
        return true;
    }


    private OpenApiService getOpenApiServiceAnnotation(ApiService apiService) {
        return apiService.getClass().getAnnotation(OpenApiService.class);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
