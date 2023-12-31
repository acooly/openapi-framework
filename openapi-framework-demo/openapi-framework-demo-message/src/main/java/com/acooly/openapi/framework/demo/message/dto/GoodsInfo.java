/*
 * acooly.cn Inc.
 * Copyright (c) 2016 All Rights Reserved.
 * create by zhangpu
 * date:2016年3月21日
 *
 */
package com.acooly.openapi.framework.demo.message.dto;

import com.acooly.core.utils.Money;
import com.acooly.core.utils.ToString;
import com.acooly.core.utils.validate.jsr303.HttpUrl;
import com.acooly.openapi.framework.common.annotation.OpenApiField;
import com.acooly.openapi.framework.demo.message.enums.GoodType;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * @author zhangpu
 */
@Getter
@Setter
public class GoodsInfo {

    @ToString.Maskable
    @NotBlank
    @Size(max = 63)
    @OpenApiField(desc = "商品名称", demo = "牛肉干", ordinal = 1)
    private String name;

    @NotNull
    @OpenApiField(desc = "商品类型", ordinal = 2)
    private GoodType goodType;

    @Size(min = 1)
    @OpenApiField(desc = "商品数量", demo = "1", ordinal = 3)
    private int quantity;

    @OpenApiField(desc = "价格", demo = "120.00", ordinal = 4)
    private Money price;

    @ToString.Invisible
    @HttpUrl(blankable = true)
    @OpenApiField(desc = "介绍网址", demo = "http://www.merchant.com/goods.html", ordinal = 5)
    private String referUrl;

    public GoodsInfo() {
    }

    public GoodsInfo(@NotBlank @Size(max = 63) String name, @NotNull GoodType goodType, @Size(min = 1) int quantity, Money price) {
        this.name = name;
        this.goodType = goodType;
        this.quantity = quantity;
        this.price = price;
    }

    public GoodsInfo(String name, GoodType goodType, int quantity, Money price, String referUrl) {
        this.name = name;
        this.goodType = goodType;
        this.quantity = quantity;
        this.price = price;
        this.referUrl = referUrl;
    }

}
