package com.acooly.openapi.framework.service;

/**
 * @author qiuboboy@qq.com
 * @date 2017-11-23 14:20
 */
public interface AuthInfoRealmManageService extends AuthInfoRealmService {

  void createAuthenticationInfo(String accessKey, String secretKey);

  void createAuthorizationInfo(String accessKey, String authorizationInfo);

  void updateAuthenticationInfo(String accessKey, String sercretKey);
}
