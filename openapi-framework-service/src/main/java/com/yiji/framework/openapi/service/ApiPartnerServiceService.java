/*
 * acooly.cn Inc.
 * Copyright (c) 2016 All Rights Reserved.
 * create by acooly
 * date:2016-07-28
 *
 */
package com.yiji.framework.openapi.service;

import com.acooly.core.common.service.EntityService;
import com.yiji.framework.openapi.domain.ApiPartnerService;
import com.yiji.framework.openapi.common.dto.ApiServiceDto;

import java.util.List;
import java.util.Map;

/**
 * api_partner_service Service接口
 * <p>
 * Date: 2016-07-28 15:33:42
 *
 * @author acooly
 */
public interface ApiPartnerServiceService extends EntityService<ApiPartnerService> {

    Map<String, List<ApiServiceDto>> searchServiceByPartner(Long id);

    boolean exsitService(List<Long> serviceIds);

    void batchSave(Long apiPartnerId, List<Long> serviceIds);

    List<String> getAuthorizedServices(String partnerId);

}
