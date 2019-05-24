/*
 * www.acooly.cn Inc.
 * Copyright (c) 2019 All Rights Reserved
 */

/*
 * 修订记录:
 * zhangpu@acooly.cn 2019-01-07 17:59 创建
 */
package com.acooly.openapi.framework.service.test.api;

import com.acooly.openapi.framework.common.annotation.ApiDocNote;
import com.acooly.openapi.framework.common.annotation.ApiDocType;
import com.acooly.openapi.framework.common.annotation.OpenApiService;
import com.acooly.openapi.framework.common.enums.ApiBusiType;
import com.acooly.openapi.framework.common.enums.ResponseType;
import com.acooly.openapi.framework.core.service.base.BaseApiService;
import com.acooly.openapi.framework.service.test.request.TreeNodeListApiRequest;
import com.acooly.openapi.framework.service.test.response.TreeNodeListApiResponse;

/**
 * @author zhangpu 2019-01-07 17:59
 */
@ApiDocType(code = "test", name = "测试")
@ApiDocNote("树形递归结构报文测试")
@OpenApiService(
        name = "treeNodeList",
        desc = "测试：树形结构查询",
        responseType = ResponseType.SYN,
        owner = "openApi-arch",
        busiType = ApiBusiType.Trade
)
public class TreeNodeListApiService extends BaseApiService<TreeNodeListApiRequest, TreeNodeListApiResponse> {
    @Override
    protected void doService(TreeNodeListApiRequest request, TreeNodeListApiResponse response) {

    }
}
