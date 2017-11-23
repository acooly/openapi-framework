/*
 * acooly.com Inc.
 * Copyright (c) 2014 All Rights Reserved
 */

/*
 * 修订记录:
 * qiubo@qq.com 2017-07-27 18:17 创建
 *
 */
package com.acooly.openapi.framework.core.auth.permission;

import com.acooly.openapi.framework.common.ApiConstants;
import com.google.common.base.Splitter;
import org.springframework.util.Assert;

import java.util.Iterator;
import java.util.regex.Pattern;

/**
 * 权限字符串格式为：partnerId:serviceName
 *
 * <p>这两部分可以用*号匹配
 *
 * <p>比如:配置*:*,代表拥有所有权限.
 *
 * <p>配置123:*,代表可以访问partnerId=123的所有服务
 *
 * <p>配置123:xxx*,代表可以访问partnerId=123,xxx开头的服务
 *
 * @author qiubo@qq.com
 */
public class DefaultPermission implements Permission {

  private static Pattern permPattern = Pattern.compile("\\S+:\\S+");

  /** 权限字符串 */
  private String perm;

  private String partnerIdPerm;
  private String servicePerm;

  public DefaultPermission(String perm) {
    Assert.state(
        permPattern.matcher(perm).matches(), "权限字符串的格式应该为partnerId:serviceName,可以支持*,匹配所有为*:*");
    this.perm = perm;
    Iterator<String> iterator = Splitter.on(":").split(perm).iterator();
    partnerIdPerm = iterator.next();
    servicePerm = iterator.next();
  }

  @Override
  public boolean implies(String resource) {
    if (resource.equals(perm) || resource.equals(ApiConstants.ALL_WILDCARD_TOKEN)) {
      return true;
    }
    Iterator<String> iterator = Splitter.on(":").split(resource).iterator();
    String partnerId = iterator.next();
    String serviceName = iterator.next();
    return match(partnerId, partnerIdPerm) && match(serviceName, servicePerm);
  }

  private boolean match(String res, String perm) {
    if (ApiConstants.WILDCARD_TOKEN.equals(perm)||res.equals(perm)) {
      return true;
    }
    int idx = perm.indexOf(ApiConstants.WILDCARD_TOKEN);
    if (idx < 0) {
      return false;
    } else {
      String pp = perm.substring(0, idx);
      String pe = perm.substring(idx + 1);
      return res.startsWith(pp) && res.endsWith(pe);
    }
  }
}
