/**
 * openapi-framework
 * <p>
 * Copyright 2014 Acooly.cn, Inc. All rights reserved.
 *
 * @author zhangpu
 * @date 2021-03-29 15:12
 */
package com.acooly.openapi.framework.common.utils;

import com.acooly.core.common.dao.support.PageInfo;
import com.acooly.core.common.facade.OrderBase;
import com.acooly.core.common.facade.PageOrder;
import com.acooly.core.common.facade.PageResult;
import com.acooly.core.common.facade.ResultBase;
import com.acooly.core.utils.Reflections;
import com.acooly.core.utils.mapper.BeanCopier;
import com.acooly.openapi.framework.common.context.ApiContext;
import com.acooly.openapi.framework.common.context.ApiContextHolder;
import com.acooly.openapi.framework.common.message.ApiRequest;
import com.acooly.openapi.framework.common.message.ApiResponse;
import com.acooly.openapi.framework.common.message.PageApiRequest;
import com.acooly.openapi.framework.common.message.PageApiResponse;
import lombok.extern.slf4j.Slf4j;

/**
 * OpenApi与Facade交互工具
 *
 * @author zhangpu
 * @date 2021-03-29 15:12
 */
@Slf4j
public class OpenApiFacades {

    /**
     * 通过Api请求对象创建facade的Order对象
     * <p>
     * <li>自动创建T对应的Order对象</li>
     * <li>自动填充gid,tenantId,partnerId</li>
     * <li>如果是分页查询，自动填充Order.PageInfo</li>
     * </p>
     *
     * @param clazz   order的类型
     * @param request 请求报文
     * @param <T>     order的泛型
     * @return order对象
     */
    public static <T extends OrderBase> T order(Class<T> clazz, ApiRequest request) {
        T order = Reflections.createObject(clazz);
        order.setPartnerId(request.getPartnerId());
        ApiContext apiContext = ApiContextHolder.getContext();
        if (apiContext != null) {
            order.setGid(apiContext.getGid());
            order.setTenantId(apiContext.getTenantId());
        }
        if (PageOrder.class.isAssignableFrom(clazz)
                && PageApiRequest.class.isAssignableFrom(request.getClass())) {
            PageOrder pageOrder = (PageOrder) order;
            PageApiRequest pageApiRequest = (PageApiRequest) request;
            pageOrder.setPageInfo(new PageInfo(pageApiRequest.getLimit(), pageApiRequest.getStart()));
        }
        BeanCopier.copy(request, order, BeanCopier.CopyStrategy.IGNORE_NULL, BeanCopier.NoMatchingRule.IGNORE);
        return order;
    }


    /**
     * Facade的result到Api的response/notify对象的组装填充
     * <p>
     * 特性：
     * <li>自动填充结果状态和detail信息</li>
     * <li>PageResult: 完全自动填充，包括:分页信息和当页列表数据</li>
     * </p>
     *
     * @param result   facade返回的result对象
     * @param response ApiResponse和ApiNotify对象
     */
    public static void result(ResultBase result, ApiResponse response) {
        // 设置结果
        response.setResult(result.getCode(), result.getMessage(), result.getDetail());
        if (result.failure()) {
            return;
        }
        // 针对PageResult的自动填充
        if (PageResult.class.isAssignableFrom(result.getClass())
                && PageApiResponse.class.isAssignableFrom(response.getClass())) {
            PageResult pageResult = (PageResult) result;
            PageApiResponse pageApiResponse = (PageApiResponse) response;
            PageInfo pageInfo = pageResult.getDto();
            pageApiResponse.setRows(pageInfo.getPageResults());
            pageApiResponse.setTotalRows(pageInfo.getTotalCount());
            pageApiResponse.setTotalPages(pageInfo.getTotalPage());
        }
    }

}
