package com.acooly.openapi.framework.service;

/**
 * @author qiuboboy@qq.com
 * @date 2017-11-23 14:20
 */
public interface AuthInfoRealmManageService extends AuthInfoRealmService {
  /**
   * 创建新认证信息
   */
  void createAuthenticationInfo(String accessKey, String secretKey);

  /**
   * 创建新的授权信息
   */
  void createAuthorizationInfo(String accessKey, String authorizationInfo);

  /**
   * 修改认证信息，用于每次登录时，下发新的认证信息
   */
  void updateAuthenticationInfo(String accessKey, String sercretKey);
}
