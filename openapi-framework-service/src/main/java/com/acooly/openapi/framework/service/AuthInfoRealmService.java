package com.acooly.openapi.framework.service;

import java.util.List;

/**
 * @author qiuboboy@qq.com
 * @date 2017-11-23 14:04
 */
public interface AuthInfoRealmService {
  /**
   * 通过accessKey获取sercretKey
   *
   * @param accessKey
   * @return
   */
  String getSercretKey(String accessKey);

  /**
   * 权限字符串格式为：partnerId:serviceName
   *
   * <p>这两部分可以用*号匹配
   *
   * <p>比如:配置*,代表拥有所有权限.
   *
   * <p>配置123:*,代表可以访问partnerId=123的所有服务
   *
   * <p>配置123:xxx*,代表可以访问partnerId=123,xxx开头的服务
   *
   * @param accessKey
   */
  List<String> getAuthorizationInfo(String accessKey);
}
