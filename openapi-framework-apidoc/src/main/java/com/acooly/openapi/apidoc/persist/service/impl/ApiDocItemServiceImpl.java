/*
 * acooly.cn Inc.
 * Copyright (c) 2017 All Rights Reserved.
 * create by acooly
 * date:2017-12-05
 */
package com.acooly.openapi.apidoc.persist.service.impl;

import org.springframework.stereotype.Service;

import com.acooly.core.common.service.EntityServiceImpl;
import com.acooly.openapi.apidoc.persist.service.ApiDocItemService;
import com.acooly.openapi.apidoc.persist.dao.ApiDocItemDao;
import com.acooly.openapi.apidoc.persist.entity.ApiDocItem;

/**
 * 报文字段 Service实现
 *
 * Date: 2017-12-05 12:34:39
 *
 * @author acooly
 *
 */
@Service("apiDocItemService")
public class ApiDocItemServiceImpl extends EntityServiceImpl<ApiDocItem, ApiDocItemDao> implements ApiDocItemService {

}
