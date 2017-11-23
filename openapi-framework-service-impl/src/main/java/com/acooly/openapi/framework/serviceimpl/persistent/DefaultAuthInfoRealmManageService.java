package com.acooly.openapi.framework.serviceimpl.persistent;

import com.acooly.openapi.framework.common.context.ApiContextHolder;
import com.acooly.openapi.framework.common.enums.SecretType;
import com.acooly.openapi.framework.common.enums.SignType;
import com.acooly.openapi.framework.domain.ApiPartner;
import com.acooly.openapi.framework.domain.ApiPartnerService;
import com.acooly.openapi.framework.service.AuthInfoRealmManageService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * @author qiuboboy@qq.com
 * @date 2017-11-23 14:35
 */
public class DefaultAuthInfoRealmManageService implements AuthInfoRealmManageService {
  @Autowired ApiPartnerServiceImpl apiPartnerService;
  @Autowired ApiPartnerServiceServiceImpl apiPartnerServiceService;

  @Override
  public void createAuthenticationInfo(String accessKey, String secretKey) {
    ApiPartner apiPartner = new ApiPartner();
    apiPartner.setPartnerId(accessKey);
    apiPartner.setPartnerName(accessKey);
    apiPartner.setSecretKey(secretKey);
    apiPartner.setSignType(SignType.MD5);
    apiPartner.setSecretType(SecretType.digest);
    apiPartnerService.save(apiPartner);
    ApiContextHolder.getApiContext().ext("apipartnerid", apiPartner.getId());
  }

  @Override
  public void createAuthorizationInfo(String accessKey, String authorizationInfo) {
    ApiPartnerService apiPartnerService = new ApiPartnerService();
    apiPartnerService.setPartnerId(accessKey);
    apiPartnerService.setServiceName(authorizationInfo);
    Long apipartnerid = (Long) ApiContextHolder.getApiContext().ext("apipartnerid");
    apiPartnerService.setApipartnerid(apipartnerid);
    apiPartnerService.setApiserviceid(1l);
    apiPartnerService.setServiceTitle("app");
    apiPartnerService.setServiceVersion("1.0");
    apiPartnerServiceService.save(apiPartnerService);
  }

  @Override
  public void updateAuthenticationInfo(String accessKey, String sercretKey) {
    ApiPartner apiPartner = apiPartnerService.queryByPartnerId(accessKey);
    apiPartner.setSecretKey(sercretKey);
    apiPartnerService.save(apiPartner);
  }

  @Override
  public String getSercretKey(String accessKey) {
    return apiPartnerService.getPartnerSercretKey(accessKey);
  }

  @Override
  public List<String> getAuthorizationInfo(String accessKey) {
    return apiPartnerServiceService.getAuthorizedServices(accessKey);
  }
}
