/*
 * acooly.cn Inc.
 * Copyright (c) 2018 All Rights Reserved.
 * create by shuijing
 * date:2018-08-14
 */
package com.acooly.openapi.mock.service.impl;

import org.springframework.stereotype.Service;

import com.acooly.core.common.service.EntityServiceImpl;
import com.acooly.openapi.mock.service.ApiMockService;
import com.acooly.openapi.mock.dao.ApiMockDao;
import com.acooly.openapi.mock.entity.ApiMock;

/**
 * 服务类 Service实现
 *
 * Date: 2018-08-14 17:35:43
 *
 * @author shuijing
 *
 */
@Service("apiMockService")
public class ApiMockServiceImpl extends EntityServiceImpl<ApiMock, ApiMockDao> implements ApiMockService {

}
