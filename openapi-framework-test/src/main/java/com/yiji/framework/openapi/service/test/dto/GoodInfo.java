/*
 * acooly.cn Inc.
 * Copyright (c) 2016 All Rights Reserved.
 * create by zhangpu 
 * date:2016年3月21日
 *
 */
package com.yiji.framework.openapi.service.test.dto;

import com.acooly.core.utils.Money;
import com.acooly.core.utils.ToString;
import com.acooly.core.utils.validate.jsr303.HttpUrl;
import com.acooly.core.utils.validate.jsr303.MoneyConstraint;
import com.yiji.framework.openapi.common.annotation.OpenApiField;
import com.yiji.framework.openapi.service.test.enums.GoodType;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * @author zhangpu
 */
public class GoodInfo {

    @NotEmpty
    @Size(max = 63)
    @OpenApiField(desc = "名称", demo = "牛肉干")
    private String name;

    @NotNull
    @OpenApiField(desc = "类型")
    private GoodType goodType;

    @Size(min = 1)
    @OpenApiField(desc = "数量", demo = "1")
    private int quantity;

    @MoneyConstraint(min = 1)
    @OpenApiField(desc = "价格", demo = "120.00")
    private Money price;

    @HttpUrl(blankable = true)
    @OpenApiField(desc = "介绍网址", demo = "http://www.merchant.com/goods.html")
    private String referUrl;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public GoodType getGoodType() {
        return goodType;
    }

    public void setGoodType(GoodType goodType) {
        this.goodType = goodType;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public Money getPrice() {
        return price;
    }

    public void setPrice(Money price) {
        this.price = price;
    }

    public String getReferUrl() {
        return referUrl;
    }

    public void setReferUrl(String referUrl) {
        this.referUrl = referUrl;
    }

    @Override
    public String toString() {
        return ToString.toString(this);
    }

}
