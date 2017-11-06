/*
 * acooly.com Inc.
 * Copyright (c) 2016 All Rights Reserved
 */

/*
 * 修订记录:
 * kuli@yiji.com 2016-07-30 01:31 创建
 */
package com.acooly.openapi.framework.service.persistent.dao;

import com.acooly.openapi.framework.common.dto.ApiServiceDto;

import java.util.List;

/** @author acooly */
public interface ApiPartnerServiceCustomDao {

  List<ApiServiceDto> queryPartnerService(Long partnerId);

  List<String> getAuthorizedServices(String partnerId);
}
