/*
 * acooly.cn Inc.
 * Copyright (c) 2018 All Rights Reserved.
 * create by shuijing
 * date:2018-08-14
 */
package com.acooly.openapi.mock.web;

import com.acooly.core.common.web.AbstractJsonEntityController;
import com.acooly.openapi.mock.entity.ApiMock;
import com.acooly.openapi.mock.service.ApiMockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 服务类 管理控制器
 *
 * @author shuijing
 * Date: 2018-08-14 17:35:43
 */
@Controller
@RequestMapping(value = "/manage/module/openapi/mock/apiMock")
public class ApiMockManagerController extends AbstractJsonEntityController<ApiMock, ApiMockService> {


    {
        allowMapping = "*";
    }

    @SuppressWarnings("unused")
    @Autowired
    private ApiMockService apiMockService;


}
