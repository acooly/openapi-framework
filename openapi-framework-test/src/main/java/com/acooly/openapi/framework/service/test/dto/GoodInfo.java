/*
 * acooly.cn Inc.
 * Copyright (c) 2016 All Rights Reserved.
 * create by zhangpu
 * date:2016年3月21日
 *
 */
package com.acooly.openapi.framework.service.test.dto;

import com.acooly.core.common.facade.InfoBase;
import com.acooly.core.utils.Money;
import com.acooly.core.utils.validate.jsr303.HttpUrl;
import com.acooly.core.utils.validate.jsr303.MoneyConstraint;
import com.acooly.openapi.framework.common.annotation.OpenApiField;
import com.acooly.openapi.framework.service.test.enums.GoodType;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * @author zhangpu
 */
@Getter
@Setter
public class GoodInfo extends InfoBase {

    @NotEmpty
    @Size(max = 63)
    @OpenApiField(desc = "商品名称", demo = "牛肉干")
    private String name;

    @NotNull
    @OpenApiField(desc = "商品类型")
    private GoodType goodType;

    @Size(min = 1)
    @OpenApiField(desc = "商品数量", demo = "1")
    private int quantity;

    @MoneyConstraint(min = 1)
    @OpenApiField(desc = "价格", demo = "120.00")
    private Money price;

    @HttpUrl(blankable = true)
    @OpenApiField(desc = "介绍网址", demo = "http://www.merchant.com/goods.html")
    private String referUrl;
}
