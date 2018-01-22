/*
 * www.acooly.cn Inc.
 * Copyright (c) 2017 All Rights Reserved
 */

/*
 * 修订记录:
 * zhangpu@acooly.cn 2017-12-14 16:55 创建
 */
package com.acooly.openapi.apidoc.generator.parser.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * @author zhangpu 2017-12-14 16:55
 */
@Getter
@Setter
public class ApiDataSize {

    private Integer min;

    private Integer max;

    public ApiDataSize() {
    }

    public ApiDataSize(Integer min, Integer max) {
        this.min = min;
        this.max = max;
    }
}
