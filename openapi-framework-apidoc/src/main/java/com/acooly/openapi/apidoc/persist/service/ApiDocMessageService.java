/*
 * acooly.cn Inc.
 * Copyright (c) 2017 All Rights Reserved.
 * create by acooly
 * date:2017-12-05
 *
 */
package com.acooly.openapi.apidoc.persist.service;

import com.acooly.core.common.service.EntityService;
import com.acooly.openapi.apidoc.persist.entity.ApiDocMessage;

import java.util.List;

/**
 * 服务报文 Service接口
 * <p>
 * Date: 2017-12-05 12:34:39
 *
 * @author acooly
 */
public interface ApiDocMessageService extends EntityService<ApiDocMessage> {

    void merge(String serviceNo,List<ApiDocMessage> apiDocMessages);

    void cascadeDelete(String serviceNo);

    List<ApiDocMessage> loadApiDocMessages(String serviceNo);

}
