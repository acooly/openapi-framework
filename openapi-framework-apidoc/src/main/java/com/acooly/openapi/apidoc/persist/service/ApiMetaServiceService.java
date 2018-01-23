/*
 * acooly.cn Inc.
 * Copyright (c) 2016 All Rights Reserved.
 * create by acooly
 * date:2016-07-16
 *
 */
package com.acooly.openapi.apidoc.persist.service;

import com.acooly.core.common.service.EntityService;
import com.acooly.openapi.framework.domain.ApiMetaService;

/**
 * 服务元数据 Service接口
 * <p>
 * <p>Date: 2016-07-16 01:57:14
 *
 * @author acooly
 */
public interface ApiMetaServiceService extends EntityService<ApiMetaService> {

    ApiMetaService mergeSave(ApiMetaService apiMetaService);

    ApiMetaService findByServiceNameAndVersion(String serviceName, String version);

}
