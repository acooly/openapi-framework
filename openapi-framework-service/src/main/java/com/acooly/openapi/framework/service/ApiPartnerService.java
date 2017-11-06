/*
 * acooly.cn Inc.
 * Copyright (c) 2016 All Rights Reserved.
 * create by acooly
 * date:2016-07-16
 *
 */
package com.acooly.openapi.framework.service;

import com.acooly.core.common.service.EntityService;
import com.acooly.openapi.framework.common.enums.SignType;
import com.acooly.openapi.framework.domain.ApiPartner;

/**
 * 合作方管理 Service接口
 *
 * <p>Date: 2016-07-16 02:05:01
 *
 * @author acooly
 */
public interface ApiPartnerService extends EntityService<ApiPartner> {

  String getPartnerSercretKey(String partnerId);

  /**
   * 生成商户号
   *
   * @return 商户号:partnerId
   */
  String generatePartnerid();

  /** @return */

  /**
   * 生成Digest秘钥
   *
   * @param signType 签名算法类型(MD5,SHA等),默认为MD5
   * @return 秘钥字符串
   */
  String generateDigestSecurityKey(SignType signType);
}
