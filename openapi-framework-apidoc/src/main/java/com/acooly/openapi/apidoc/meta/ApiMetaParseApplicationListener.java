package com.acooly.openapi.apidoc.meta;

import com.acooly.openapi.framework.common.annotation.OpenApiNote;
import com.acooly.openapi.framework.common.annotation.OpenApiService;
import com.acooly.openapi.framework.common.enums.ResponseType;
import com.acooly.openapi.framework.common.event.ApiMetaParseFinish;
import com.acooly.openapi.framework.common.executor.ApiService;
import com.acooly.openapi.framework.domain.ApiMetaService;
import com.acooly.openapi.apidoc.persist.service.ApiMetaServiceService;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.List;

/**
 * @author qiuboboy@qq.com
 * @date 2017-11-27 23:44
 */
@Component
@Slf4j
public class ApiMetaParseApplicationListener implements ApplicationListener<ApplicationReadyEvent> {

    @Autowired
    ApiMetaServiceService apiMetaServiceService;

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        new Thread(
                () -> {
                    List<ApiService> apiServices =
                            Lists.newArrayList(
                                    event.getApplicationContext().getBeansOfType(ApiService.class).values());
                    apiServices.sort(Comparator.comparing(o -> o.getClass().getName()));
                    for (ApiService apiService : apiServices) {
                        OpenApiService openApiService =
                                apiService.getClass().getAnnotation(OpenApiService.class);
                        OpenApiNote apiNote = apiService.getClass().getAnnotation(OpenApiNote.class);

                        ApiMetaService entity = new ApiMetaService();

                        entity.setServiceName(openApiService.name());
                        entity.setVersion(openApiService.version());
                        entity.setBusiType(openApiService.busiType());
                        entity.setServiceDesc(openApiService.desc());
                        entity.setResponseType(openApiService.responseType());
                        entity.setOwner(openApiService.owner());
                        entity.setServiceClass(apiService.getClass().getName());
                        entity.setRequestClass(apiService.getRequestBean().getClass().getName());
                        entity.setResponseClass(apiService.getResponseBean().getClass().getName());
                        if (openApiService.responseType() == ResponseType.ASNY) {
                            entity.setNotifyClass(apiService.getApiNotifyBean().getClass().getName());
                        }
                        if (apiNote != null) {
                            entity.setNote(apiNote.value());
                        }
                        apiMetaServiceService.mergeSave(entity);
                    }
                    event.getApplicationContext().publishEvent(
                            new ApiMetaParseFinish(event.getSpringApplication(), event.getArgs(),apiServices));
                    log.info("api服务元数据解析完毕");
                })
                .start();
    }
}
