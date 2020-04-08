/**
 * openapi-framework
 * <p>
 * Copyright 2014 Acooly.cn, Inc. All rights reserved.
 *
 * @author zhangpu
 * @date 2020-03-05 13:40
 */
package com.acooly.openapi.framework.demo.message.request;

import com.acooly.openapi.framework.common.annotation.OpenApiField;
import com.acooly.openapi.framework.common.message.PageApiRequest;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Size;

/**
 * @author zhangpu
 * @date 2020-03-05 13:40
 */
@Getter
@Setter
public class DemoGoodsListApiRequest extends PageApiRequest {

    @Size(max = 32)
    @OpenApiField(desc = "商品名称", constraint = "名称模糊查询条件", demo = "牛肉", ordinal = 1)
    private String name;
}
