/*
 * www.yiji.com Inc.
 * Copyright (c) 2015 All Rights Reserved.
 */

package com.acooly.openapi.apidoc.generator.output;

import com.acooly.core.utils.enums.Messageable;
import com.google.common.collect.Maps;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by zhangpu on 2015/2/26.
 */
public enum ApiOutputerTypeEnum implements Messageable {

    html("html", "web"),

    database("database", "数据库"),

    word("word", "word"),

    pdf("pdf", "PDF"),

    console("console", "控制台");


    private final String code;
    private final String message;

    private ApiOutputerTypeEnum(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public String code() {
        return code;
    }

    @Override
    public String message() {
        return message;
    }

    public static Map<String, String> mapping() {
        Map<String, String> map = Maps.newLinkedHashMap();
        for (ApiOutputerTypeEnum type : values()) {
            map.put(type.getCode(), type.getMessage());
        }
        return map;
    }

    /**
     * 通过枚举值码查找枚举值。
     *
     * @param code 查找枚举值的枚举值码。
     * @return 枚举值码对应的枚举值。
     * @throws IllegalArgumentException 如果 code 没有对应的 Status 。
     */
    public static ApiOutputerTypeEnum find(String code) {
        for (ApiOutputerTypeEnum status : values()) {
            if (status.getCode().equals(code)) {
                return status;
            }
        }
        return null;
    }

    /**
     * 获取全部枚举值。
     *
     * @return 全部枚举值。
     */
    public static List<ApiOutputerTypeEnum> getAll() {
        List<ApiOutputerTypeEnum> list = new ArrayList<ApiOutputerTypeEnum>();
        for (ApiOutputerTypeEnum status : values()) {
            list.add(status);
        }
        return list;
    }

    /**
     * 获取全部枚举值码。
     *
     * @return 全部枚举值码。
     */
    public static List<String> getAllCode() {
        List<String> list = new ArrayList<String>();
        for (ApiOutputerTypeEnum status : values()) {
            list.add(status.code());
        }
        return list;
    }

}
