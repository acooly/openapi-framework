/** create by zhangpu date:2015年5月12日 */
package com.acooly.openapi.framework.app.openapi.support;

import com.acooly.openapi.framework.app.AppProperties;
import com.acooly.openapi.framework.app.biz.domain.AppCustomer;
import com.acooly.openapi.framework.app.biz.enums.EntityStatus;
import com.acooly.openapi.framework.app.biz.service.AppCustomerService;
import com.acooly.openapi.framework.common.annotation.OpenApiListener;
import com.acooly.openapi.framework.common.event.dto.BeforeServiceExecuteEvent;
import com.acooly.openapi.framework.common.message.ApiRequest;
import com.acooly.openapi.framework.common.message.AppRequest;
import com.acooly.openapi.framework.core.exception.impl.ApiServiceAuthenticationException;
import com.acooly.openapi.framework.core.listener.AbstractListener;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.AbstractEnvironment;

/**
 * App-API服务前置处理
 *
 * <p>用途：处理设备绑定检查
 *
 * @author zhangpu
 */
@OpenApiListener(global = true, asyn = false)
public class AppApiBeforeExecuteListener extends AbstractListener<BeforeServiceExecuteEvent> {

  @Autowired private AppCustomerService appCustomerService;
  @Autowired private AppProperties appProperties;

  @Override
  public void onOpenApiEvent(BeforeServiceExecuteEvent event) {

    String activeProfile = System.getProperty(AbstractEnvironment.ACTIVE_PROFILES_PROPERTY_NAME);
    // 非生产环境不检查
    if (!"online".equals(activeProfile)) {
      return;
    }
    // 配置开关为关闭，则不检查
    if (!appProperties.isDeviceIdCheck()) {
      return;
    }

    ApiRequest request = event.getApiRequest();
    if (AppRequest.class.isAssignableFrom(request.getClass())) {
      AppCustomer appCustomer =
          appCustomerService.loadAppCustomer(request.getPartnerId(), EntityStatus.Enable);
      if (appCustomer == null) {
        logger.debug("{} 没有绑定设备", request.getPartnerId());
        throw new ApiServiceAuthenticationException("没有绑定设备");
      }
      AppRequest appRequest = (AppRequest) request;
      if (!StringUtils.equals(appRequest.getDeviceId(), appCustomer.getDeviceId())) {
        logger.debug("{} 设备没有绑定", request.getPartnerId());
        throw new ApiServiceAuthenticationException("非法设备请求，非原始注册设备。");
      }
    }
  }

  @Override
  public int getOrder() {
    return 0;
  }
}
