/*
 * acooly.cn Inc.
 * Copyright (c) 2017 All Rights Reserved.
 * create by acooly
 * date:2017-12-05
 */
package com.acooly.openapi.apidoc.persist.service.impl;

import org.springframework.stereotype.Service;

import com.acooly.core.common.service.EntityServiceImpl;
import com.acooly.openapi.apidoc.persist.service.ApiDocMessageService;
import com.acooly.openapi.apidoc.persist.dao.ApiDocMessageDao;
import com.acooly.openapi.apidoc.persist.entity.ApiDocMessage;

/**
 * 服务报文 Service实现
 *
 * Date: 2017-12-05 12:34:39
 *
 * @author acooly
 *
 */
@Service("apiDocMessageService")
public class ApiDocMessageServiceImpl extends EntityServiceImpl<ApiDocMessage, ApiDocMessageDao> implements ApiDocMessageService {

}
