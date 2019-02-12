/*
 * www.acooly.cn Inc.
 * Copyright (c) 2017 All Rights Reserved
 */

/*
 * 修订记录:
 * zhangpu@acooly.cn 2017-08-02 07:38 创建
 */
package com.acooly.openapi.apidoc.portal.dto;

import com.acooly.openapi.apidoc.enums.SchemeTypeEnum;
import com.google.common.collect.Maps;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

/**
 * scheme 实体 DTO
 *
 * @author zhangpu 2017-08-02 07:38
 */
@Getter
@Setter
public class SchemeDto {

    private Long id;
    private String name;
    private String schemeNo;
    private SchemeTypeEnum schemeTypeEnum;

    private Map<String, String> contents = Maps.newTreeMap();

    public SchemeDto() {
    }

    public SchemeDto(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public SchemeDto(Long id, String name, String schemeNo) {
        this.id = id;
        this.name = name;
        this.schemeNo = schemeNo;
    }

    public void put(Long id, String title) {
        contents.put(String.valueOf(id), title);
    }
}
