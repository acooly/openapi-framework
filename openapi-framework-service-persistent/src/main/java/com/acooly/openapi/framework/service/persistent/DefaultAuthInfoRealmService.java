package com.acooly.openapi.framework.service.persistent;

import com.acooly.openapi.framework.service.ApiPartnerService;
import com.acooly.openapi.framework.service.ApiPartnerServiceService;
import com.acooly.openapi.framework.service.AuthInfoRealmService;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author qiuboboy@qq.com
 * @date 2017-11-23 14:06
 */
public class DefaultAuthInfoRealmService implements AuthInfoRealmService {
  @Resource protected ApiPartnerServiceService apiPartnerServiceService;
  @Resource protected ApiPartnerService apiPartnerService;

  @Override
  public String getSercretKey(String accessKey) {
    return apiPartnerService.getPartnerSercretKey(accessKey);
  }

  @Override
  public List<String> getAuthorizationInfo(String accessKey) {
    return apiPartnerServiceService.getAuthorizedServices(accessKey);
  }
}
