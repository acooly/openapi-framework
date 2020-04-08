/**
 * openapi-framework
 * <p>
 * Copyright 2014 Acooly.cn, Inc. All rights reserved.
 *
 * @author zhangpu
 * @date 2020-03-05 13:50
 */
package com.acooly.openapi.framework.demo.service.api;

import com.acooly.openapi.framework.common.annotation.ApiDocNote;
import com.acooly.openapi.framework.common.annotation.ApiDocType;
import com.acooly.openapi.framework.common.annotation.OpenApiService;
import com.acooly.openapi.framework.common.enums.ApiBusiType;
import com.acooly.openapi.framework.common.enums.ResponseType;
import com.acooly.openapi.framework.core.service.base.BaseApiService;
import com.acooly.openapi.framework.demo.message.dto.GoodsInfo;
import com.acooly.openapi.framework.demo.message.request.GoodsListApiRequest;
import com.acooly.openapi.framework.demo.message.response.GoodsListApiResponse;
import com.acooly.openapi.framework.demo.service.DemoApiUtils;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * @author zhangpu
 * @date 2020-03-05 13:50
 */
@Slf4j
@ApiDocType(code = DemoApiUtils.API_DEMO_DOC_TYPE_CODE, name = DemoApiUtils.API_DEMO_DOC_TYPE_NAME)
@ApiDocNote("提供OpenApi的标准分页列表查询Demo：商品列表分页条件查询，注意：数据都是MOCK的，重点演示结构")
@OpenApiService(name = "goodsList", desc = "商品列表", responseType = ResponseType.SYN, owner = "zhangpu", busiType = ApiBusiType.Trade)
public class GoodsListApiService extends BaseApiService<GoodsListApiRequest, GoodsListApiResponse> {
    @Override
    protected void doService(GoodsListApiRequest request, GoodsListApiResponse response) {
        List<GoodsInfo> goodsInfoList = DemoApiUtils.buildGoodsList(request.getLimit(), request.getName());
        response.setRows(goodsInfoList);
        response.setTotalPages(1);
        response.setTotalPages(goodsInfoList.size());
    }
}