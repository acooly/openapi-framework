/*
 * acooly.cn Inc.
 * Copyright (c) 2016 All Rights Reserved.
 * create by zhangpu
 * date:2016年3月21日
 *
 */
package com.acooly.openapi.test.openapitools.message.dto;

import com.acooly.core.utils.Money;
import com.acooly.core.utils.validate.jsr303.HttpUrl;
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

    @NotBlank
    @Size(max = 63)
    private String name;

    @NotNull
    private GoodType goodType;

    @Size(min = 1)
    private int quantity;

    private Money price;

    @HttpUrl(blankable = true)
    private String referUrl;
}
