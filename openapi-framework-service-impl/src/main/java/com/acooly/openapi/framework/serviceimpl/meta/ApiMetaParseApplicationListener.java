package com.acooly.openapi.framework.serviceimpl.meta;

import com.acooly.openapi.framework.common.annotation.OpenApiNote;
import com.acooly.openapi.framework.common.annotation.OpenApiService;
import com.acooly.openapi.framework.common.enums.ResponseType;
import com.acooly.openapi.framework.common.event.ApiMetaParseFinish;
import com.acooly.openapi.framework.common.executor.ApiService;
import com.acooly.openapi.framework.serviceimpl.meta.dao.ApiMetaServiceEntityDao;
import com.acooly.openapi.framework.serviceimpl.meta.entity.ApiMetaServiceEntity;
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
  @Autowired ApiMetaServiceEntityDao apiMetaServiceEntityDao;

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

                ApiMetaServiceEntity entity = new ApiMetaServiceEntity();
                entity.setServiceName(openApiService.name());
                entity.setVersion(openApiService.version());
                entity.setBusiType(openApiService.busiType().toString());
                entity.setServiceDesc(openApiService.desc());
                entity.setResponseType(openApiService.responseType().toString());
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
                ApiMetaServiceEntity old =
                    apiMetaServiceEntityDao.findByServiceNameAndVersion(
                        entity.getServiceName(), entity.getVersion());
                if (old == null) {
                  apiMetaServiceEntityDao.create(entity);
                } else {
                  entity.setId(old.getId());
                  apiMetaServiceEntityDao.update(entity);
                }
              }
              event
                  .getApplicationContext()
                  .publishEvent(
                      new ApiMetaParseFinish(event.getSpringApplication(), event.getArgs()));
              log.info("api服务元数据解析完毕");
            })
        .start();
  }
}
