package com.acooly.openapi.framework.service;

import java.util.List;

/**
 * @author qiuboboy@qq.com
 * @date 2017-11-23 14:04
 */
public interface AuthInfoRealmService {
  String getSercretKey(String accessKey);

  List<String> getAuthorizationInfo(String accessKey);
}
