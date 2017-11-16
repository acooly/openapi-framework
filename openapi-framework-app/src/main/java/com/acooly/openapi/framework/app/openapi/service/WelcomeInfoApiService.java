package com.acooly.openapi.framework.app.openapi.service;

import com.acooly.core.utils.Collections3;
import com.acooly.module.ofile.OFileProperties;
import com.acooly.openapi.framework.app.biz.domain.AppStartGuide;
import com.acooly.openapi.framework.app.biz.domain.AppWelcome;
import com.acooly.openapi.framework.app.biz.enums.DeviceType;
import com.acooly.openapi.framework.app.biz.service.AppStartGuideService;
import com.acooly.openapi.framework.app.biz.service.AppWelcomeService;
import com.acooly.openapi.framework.app.openapi.enums.ApiOwners;
import com.acooly.openapi.framework.app.openapi.message.WelcomeInfoRequest;
import com.acooly.openapi.framework.app.openapi.message.WelcomeInfoResponse;
import com.acooly.openapi.framework.common.annotation.OpenApiService;
import com.acooly.openapi.framework.common.enums.ApiBusiType;
import com.acooly.openapi.framework.common.enums.ResponseType;
import com.acooly.openapi.framework.core.service.base.BaseApiService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * 欢迎信息服务 API
 *
 * @note
 *     <li>通过全站匿名获取当前最新的欢迎信息。
 *     <li>包括：启动界面图和向导图组
 *     <li>根据请求的设备类型，返回对应规格的图片
 * @author zhangpu
 */
@OpenApiService(
  name = "welcomeInfo",
  desc = "欢迎信息",
  responseType = ResponseType.SYN,
  owner = ApiOwners.COMMON,
  busiType = ApiBusiType.Query
)
public class WelcomeInfoApiService extends BaseApiService<WelcomeInfoRequest, WelcomeInfoResponse> {
  @Autowired private OFileProperties oFileProperties;
  @Autowired private AppWelcomeService appWelcomeService;

  @Autowired private AppStartGuideService appStartGuideService;

  @Override
  protected void doService(WelcomeInfoRequest request, WelcomeInfoResponse response) {
    List<AppStartGuide> guides = appStartGuideService.loadValidGuides();
    if (!Collections3.isEmpty(guides)) {
      String url = null;
      for (AppStartGuide guide : guides) {
        url = getImageUrl(guide, request.getDeviceType());
        if (StringUtils.isNotBlank(url)) {
          response.append(url);
        }
      }
    }
    AppWelcome welcome = appWelcomeService.getLatestOne();
    if (welcome != null) {
      response.setWelcome(getImageUrl(welcome, request.getDeviceType()));
    }
  }

  private String getImageUrl(AppWelcome guide, DeviceType deviceType) {
    String path = guide.getImageDefault();
    if (deviceType == DeviceType.ANDROID && StringUtils.isNotBlank(guide.getImageAndroid())) {
      path = guide.getImageAndroid();
    } else if (deviceType == DeviceType.IPHONE4
        && StringUtils.isNotBlank(guide.getImageIphone4())) {
      path = guide.getImageIphone4();
    } else if (deviceType == DeviceType.IPHONE5 || deviceType == DeviceType.IPHONE6) {
      if (StringUtils.isNotBlank(guide.getImageIphone4())) {
        path = guide.getImageIphone6();
      }
    }
    if (StringUtils.isBlank(path)) {
      return null;
    } else {
      return oFileProperties.getServerRoot() + '/' + path;
    }
  }

  private String getImageUrl(AppStartGuide guide, DeviceType deviceType) {
    String path = guide.getImageDefault();
    if (deviceType == DeviceType.ANDROID && StringUtils.isNotBlank(guide.getImageAndroid())) {
      path = guide.getImageAndroid();
    } else if (deviceType == DeviceType.IPHONE4
        && StringUtils.isNotBlank(guide.getImageIphone4())) {
      path = guide.getImageIphone4();
    } else if (deviceType == DeviceType.IPHONE5 || deviceType == DeviceType.IPHONE6) {
      if (StringUtils.isNotBlank(guide.getImageIphone4())) {
        path = guide.getImageIphone6();
      }
    }
    if (StringUtils.isBlank(path)) {
      return null;
    } else {
      return oFileProperties.getServerRoot() + '/' + path;
    }
  }
}
